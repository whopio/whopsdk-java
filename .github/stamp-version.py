"""Stamp one version across every file in the tree that reports one.

Two jobs publish the same commit to two registries, so the stamp has to be a single
artifact both can run rather than a script inlined into one of them. It is deliberately
fail-loud: Fern owns every file it touches, so a rename or a reshaped header should stop
a release rather than ship a jar that reports the generator's constant on the wire.
"""

import json
import pathlib
import re
import sys

HEADER = "X-Fern-SDK-Version"


def fail(message):
    raise SystemExit("::error::" + message)


def stamp_version_file(path, version):
    text = path.read_text()
    text, count = re.subn(
        r"(?m)^([ \t]*version[ \t]*=[ \t]*).*$", r"\g<1>" + version, text, count=1
    )
    if count == 0:
        fail("no version property found in " + str(path))
    path.write_text(text)
    print("stamped " + str(path))


def stamp_sources(version):
    carriers = [
        path
        for path in pathlib.Path("src/main").rglob("*.java")
        if HEADER in path.read_text()
    ]

    if not carriers:
        fail(
            "nothing under src/main carries "
            + HEADER
            + "; the published jar would report the generator's constant"
        )

    for path in carriers:
        source = path.read_text()
        source, hits = re.subn(
            r'("' + HEADER + r'"\s*,\s*")[^"]*', r"\g<1>" + version, source
        )
        if hits == 0:
            fail(
                str(path)
                + " names "
                + HEADER
                + " but the rewrite matched nothing; the header shape changed"
            )
        path.write_text(source)
        print("stamped " + str(path))


def stamp_metadata(path, version):
    if not path.exists():
        return
    metadata = json.loads(path.read_text())
    for key in ("requestedVersion", "sdkVersion"):
        if key in metadata:
            metadata[key] = version
    path.write_text(json.dumps(metadata, indent=2) + "\n")
    print("stamped " + str(path))


def assert_no_stale_version(version):
    semver = re.compile(r"\d+\.\d+\.\d+")
    stale = []
    for path in pathlib.Path("src/main").rglob("*.java"):
        for number, line in enumerate(path.read_text().splitlines(), 1):
            if HEADER in line and semver.search(line) and version not in line:
                stale.append(str(path) + ":" + str(number) + ": " + line.strip())

    if stale:
        fail(
            "the source tree still reports a version other than "
            + version
            + ":\n"
            + "\n".join(stale)
        )


def main():
    version, version_file, metadata_file = sys.argv[1:4]

    stamp_version_file(pathlib.Path(version_file), version)
    stamp_sources(version)
    stamp_metadata(pathlib.Path(metadata_file), version)
    assert_no_stale_version(version)
    print("every version stamp reports " + version)


if __name__ == "__main__":
    main()
