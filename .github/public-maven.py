import datetime
import hashlib
import json
import pathlib
import re
import shutil
import sys
import xml.etree.ElementTree as ET

COORDINATE = pathlib.Path('com/whop/api/whop-java')
VERSION = re.compile(r'(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-([0-9A-Za-z]+(?:[.-][0-9A-Za-z]+)*))?')


def version_key(version):
    match = VERSION.fullmatch(version)
    if not match:
        raise ValueError('invalid public Maven version: ' + version)
    prerelease = match[4]
    rank = (1,) if prerelease is None else (0, tuple(
        (0, int(part)) if part.isdigit() else (1, part)
        for part in prerelease.split('.')
    ))
    return tuple(map(int, match.group(1, 2, 3))), rank


def hashes(directory):
    result = {}
    for file in sorted(directory.iterdir()):
        if file.is_symlink() or not file.is_file():
            raise ValueError('release entries must be regular files: ' + str(file))
        if file.name != 'release.json':
            result[file.name] = hashlib.sha256(file.read_bytes()).hexdigest()
    return result


def prepare(bundle, site, version, commit):
    version_key(version)
    if not re.fullmatch(r'[0-9a-f]{40}', commit):
        raise ValueError('the source commit must be a full Git SHA')
    incoming = bundle / COORDINATE / version
    for suffix in ('.jar', '-sources.jar', '-javadoc.jar', '.pom'):
        if not (incoming / ('whop-java-' + version + suffix)).is_file():
            raise ValueError('incomplete release bundle: ' + str(incoming))
    root = ET.parse(incoming / ('whop-java-' + version + '.pom')).getroot()
    ns = {'m': 'http://maven.apache.org/POM/4.0.0'}
    for key, expected in (('groupId', 'com.whop.api'), ('artifactId', 'whop-java'), ('version', version)):
        if root.findtext('m:' + key, namespaces=ns) != expected:
            raise ValueError('the release POM has the wrong ' + key)
    incoming_hashes = hashes(incoming)
    target = site / COORDINATE / version
    if target.exists():
        record = json.loads((target / 'release.json').read_text())
        if record.get('sourceCommit') != commit or record.get('version') != version:
            raise ValueError('refusing to replace an immutable release from a different commit')
        if record.get('sha256') != hashes(target):
            raise ValueError('the stored immutable release does not match its checksums')
        if (target / ('whop-java-' + version + '.pom')).read_bytes() != (incoming / ('whop-java-' + version + '.pom')).read_bytes():
            raise ValueError('the retry changes the immutable release POM')
    else:
        existing = [path.name for path in target.parent.iterdir() if path.is_dir()] if target.parent.exists() else []
        if any(version_key(other) > version_key(version) for other in existing):
            raise ValueError('refusing to publish a version older than an existing release')
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copytree(incoming, target)
        (target / 'release.json').write_text(json.dumps({
            'coordinate': 'com.whop.api:whop-java:' + version,
            'version': version,
            'sourceCommit': commit,
            'sha256': incoming_hashes,
        }, indent=2, sort_keys=True) + '\n')
    versions = sorted((path.name for path in target.parent.iterdir() if path.is_dir()), key=version_key)
    metadata = ET.Element('metadata')
    ET.SubElement(metadata, 'groupId').text = 'com.whop.api'
    ET.SubElement(metadata, 'artifactId').text = 'whop-java'
    versioning = ET.SubElement(metadata, 'versioning')
    ET.SubElement(versioning, 'latest').text = versions[-1]
    stable = [value for value in versions if VERSION.fullmatch(value)[4] is None]
    if stable:
        ET.SubElement(versioning, 'release').text = stable[-1]
    entries = ET.SubElement(versioning, 'versions')
    for value in versions:
        ET.SubElement(entries, 'version').text = value
    ET.SubElement(versioning, 'lastUpdated').text = datetime.datetime.now(datetime.timezone.utc).strftime('%Y%m%d%H%M%S')
    ET.indent(metadata)
    data = ET.tostring(metadata, encoding='utf-8', xml_declaration=True) + b'\n'
    path = target.parent / 'maven-metadata.xml'
    path.write_bytes(data)
    for algorithm in ('md5', 'sha1', 'sha256', 'sha512'):
        pathlib.Path(str(path) + '.' + algorithm).write_text(hashlib.new(algorithm, data).hexdigest())
    (site / '.nojekyll').touch()
    (site / 'index.html').write_text('<!doctype html><title>Whop Java SDK Maven repository</title><h1>Whop Java SDK</h1><p>Public Maven repository for com.whop.api:whop-java.</p><p><a href="https://github.com/whopio/whopsdk-java/blob/main/INSTALL.md">Installation instructions</a></p>\n')
    print('Prepared immutable com.whop.api:whop-java:' + version + ' from ' + commit)


if __name__ == '__main__':
    try:
        prepare(pathlib.Path(sys.argv[1]), pathlib.Path(sys.argv[2]), sys.argv[3], sys.argv[4])
    except (ValueError, OSError, KeyError, ET.ParseError) as error:
        raise SystemExit('::error::' + str(error))
