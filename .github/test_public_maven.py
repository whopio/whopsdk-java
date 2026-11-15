import hashlib
import importlib.util
import json
import pathlib
import tempfile
import unittest
import xml.etree.ElementTree as ET

spec = importlib.util.spec_from_file_location('public_maven', pathlib.Path(__file__).with_name('public-maven.py'))
public_maven = importlib.util.module_from_spec(spec)
spec.loader.exec_module(public_maven)


class PublicMavenTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.root = pathlib.Path(self.temporary.name)
        self.bundle = self.root / 'bundle'
        self.site = self.root / 'site'
        self.commit = 'a' * 40

    def bundle_version(self, version):
        directory = self.bundle / public_maven.COORDINATE / version
        directory.mkdir(parents=True, exist_ok=True)
        for suffix in ('.jar', '-sources.jar', '-javadoc.jar'):
            (directory / ('whop-java-' + version + suffix)).write_bytes(b'jar-' + version.encode())
        (directory / ('whop-java-' + version + '.pom')).write_text(
            '<project xmlns="http://maven.apache.org/POM/4.0.0"><groupId>com.whop.api</groupId><artifactId>whop-java</artifactId><version>' + version + '</version></project>'
        )
        return directory

    def publish(self, version, commit=None):
        public_maven.prepare(self.bundle, self.site, version, commit or self.commit)
        return self.site / public_maven.COORDINATE / version

    def test_preserves_old_versions_and_updates_metadata(self):
        self.bundle_version('2.8.1')
        first = self.publish('2.8.1')
        before = public_maven.hashes(first)
        self.bundle_version('2.9.0')
        self.publish('2.9.0', 'b' * 40)
        self.assertEqual(before, public_maven.hashes(first))
        metadata = first.parent / 'maven-metadata.xml'
        root = ET.parse(metadata).getroot()
        self.assertEqual('2.9.0', root.findtext('versioning/release'))
        self.assertEqual(['2.8.1', '2.9.0'], [node.text for node in root.findall('versioning/versions/version')])
        self.assertEqual(hashlib.sha256(metadata.read_bytes()).hexdigest(), pathlib.Path(str(metadata) + '.sha256').read_text())

    def test_retry_reuses_original_artifact_bytes(self):
        incoming = self.bundle_version('2.8.1')
        target = self.publish('2.8.1')
        (incoming / 'whop-java-2.8.1.jar').write_bytes(b'rebuilt-with-different-zip-times')
        self.publish('2.8.1')
        self.assertEqual(b'jar-2.8.1', (target / 'whop-java-2.8.1.jar').read_bytes())

    def test_rejects_new_commit_for_existing_version(self):
        self.bundle_version('2.8.1')
        self.publish('2.8.1')
        with self.assertRaisesRegex(ValueError, 'different commit'):
            self.publish('2.8.1', 'b' * 40)

    def test_rejects_corrupt_stored_artifact(self):
        self.bundle_version('2.8.1')
        target = self.publish('2.8.1')
        (target / 'whop-java-2.8.1.jar').write_bytes(b'corrupt')
        with self.assertRaisesRegex(ValueError, 'checksums'):
            self.publish('2.8.1')

    def test_rejects_pom_changes_on_retry(self):
        incoming = self.bundle_version('2.8.1')
        self.publish('2.8.1')
        pom = incoming / 'whop-java-2.8.1.pom'
        pom.write_text(pom.read_text().replace('</project>', '<description>changed</description></project>'))
        with self.assertRaisesRegex(ValueError, 'POM'):
            self.publish('2.8.1')

    def test_rejects_version_regression(self):
        self.bundle_version('2.9.0')
        self.publish('2.9.0')
        self.bundle_version('2.8.1')
        with self.assertRaisesRegex(ValueError, 'older'):
            self.publish('2.8.1')

    def test_rejects_incomplete_and_wrong_coordinate_bundles(self):
        incoming = self.bundle_version('2.8.1')
        (incoming / 'whop-java-2.8.1-sources.jar').unlink()
        with self.assertRaisesRegex(ValueError, 'incomplete'):
            self.publish('2.8.1')
        self.bundle_version('2.8.1')
        pom = incoming / 'whop-java-2.8.1.pom'
        pom.write_text(pom.read_text().replace('com.whop.api', 'other.group'))
        with self.assertRaisesRegex(ValueError, 'groupId'):
            self.publish('2.8.1')

    def test_rejects_unsafe_version_paths(self):
        for version in ('../../other', '2.8.1/other', '02.8.1'):
            with self.assertRaisesRegex(ValueError, 'invalid'):
                self.publish(version)

    def test_semver_precedence(self):
        versions = ['2.8.1', '2.8.1-rc.10', '2.8.1-rc.2', '2.8.0', '2.8.1-alpha']
        self.assertEqual(['2.8.0', '2.8.1-alpha', '2.8.1-rc.2', '2.8.1-rc.10', '2.8.1'], sorted(versions, key=public_maven.version_key))
