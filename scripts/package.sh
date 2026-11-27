#!/usr/bin/env bash
# whopsdk-java's release build: .github/workflows/publish-main.yml runs it when main carries a version Whop's
# public Maven repository does not have yet. It stages and verifies the Maven bundle in build/bundle/, which the
# maven job publishes.
set -euo pipefail
cd "$(dirname "$0")/.."

GROUP_ID=com.whop.api
ARTIFACT_ID=whop-java
GROUP_PATH=com/whop/api
RESOURCE_DIR=src/main/java/com/whop/api/resources
MIN_RESOURCE_MODULES=60
INIT_SCRIPT=.github/release.gradle
BUNDLE_DIR=build/bundle

# Refuse to build a tree Fern did not fill in
set -euo pipefail
count=$(find "$RESOURCE_DIR" -mindepth 1 -maxdepth 1 -type d 2>/dev/null | wc -l)
echo "${RESOURCE_DIR}: ${count} resource packages"
if [ "$count" -lt "$MIN_RESOURCE_MODULES" ]; then
  echo "::error::only ${count} resource packages, expected at least ${MIN_RESOURCE_MODULES}. Fern emits an empty SDK and exits 0 when it cannot parse the spec, and a published version cannot be replaced."
  exit 1
fi

# CI, as ci.yml runs it
bash scripts/ci.sh

# Stage the Maven bundle
./gradlew --no-daemon -I "$INIT_SCRIPT" publishMavenPublicationToBundleRepository

# Verify the staged bundle
set -euo pipefail

dir="${BUNDLE_DIR}/${GROUP_PATH}/${ARTIFACT_ID}/${VERSION}"
if [ ! -d "$dir" ]; then
  echo "::error::${dir} does not exist; Gradle staged the wrong coordinate."
  find "$BUNDLE_DIR" -maxdepth 6 -type d || true
  exit 1
fi

find "$BUNDLE_DIR" -name 'maven-metadata.xml*' -delete

for suffix in ".jar" "-sources.jar" "-javadoc.jar" ".pom"; do
  file="${dir}/${ARTIFACT_ID}-${VERSION}${suffix}"
  if [ ! -s "$file" ]; then
    echo "::error::${file} is missing or empty. Maven Central requires the main, sources and javadoc jars plus a POM."
    ls -l "$dir" || true
    exit 1
  fi
  echo "present: $(ls -l "$file")"
done

pom="${dir}/${ARTIFACT_ID}-${VERSION}.pom"
python3 - "$pom" "$GROUP_ID" "$ARTIFACT_ID" "$VERSION" build.gradle <<'PY'
import re
import sys
import xml.etree.ElementTree as ET

pom, group, artifact, version, build_file = sys.argv[1:6]
ns = {"m": "http://maven.apache.org/POM/4.0.0"}
root = ET.parse(pom).getroot()


def text(tag, node=root):
    found = node.find("m:" + tag, ns)
    return None if found is None else (found.text or "").strip()


problems = []
for tag, expected in (("groupId", group), ("artifactId", artifact), ("version", version)):
    if text(tag) != expected:
        problems.append(tag + " is " + repr(text(tag)) + ", expected " + repr(expected))

for tag in ("name", "description", "url"):
    if not text(tag):
        problems.append(tag + " is empty; Maven Central rejects the deployment")

for tag in ("licenses", "developers", "scm"):
    if root.find("m:" + tag, ns) is None:
        problems.append(tag + " is absent; Maven Central rejects the deployment")

declared = {}
for node in root.findall("m:dependencies/m:dependency", ns):
    key = text("groupId", node) + ":" + text("artifactId", node)
    declared[key] = (text("version", node), text("scope", node))

print("POM dependencies:")
for key in sorted(declared):
    print("  " + key + ":" + str(declared[key][0]) + " (" + str(declared[key][1]) + ")")

expected_api = re.findall(
    r"""(?m)^\s*api\s+['"]([^'":]+):([^'":]+):([^'"]+)['"]""",
    open(build_file).read(),
)
if not expected_api:
    problems.append(build_file + " declares no api dependencies; the parser no longer matches")

for dep_group, dep_artifact, dep_version in expected_api:
    key = dep_group + ":" + dep_artifact
    if key not in declared:
        problems.append(
            key
            + " is an api dependency in "
            + build_file
            + " but is absent from the POM; consumers would not resolve it"
        )
        continue
    found_version, scope = declared[key]
    if found_version != dep_version:
        problems.append(key + " is " + str(found_version) + " in the POM, " + dep_version + " in " + build_file)
    if scope != "compile":
        problems.append(
            key
            + " is scope "
            + str(scope)
            + " in the POM; an api dependency has to reach the consumer's compile classpath"
        )

okhttp = declared.get("com.squareup.okhttp3:okhttp")
if not okhttp or declared.get("com.squareup.okhttp3:okhttp-jvm") != (okhttp[0], "compile"):
    problems.append("Maven consumers need okhttp-jvm at the generated OkHttp version and compile scope")

if problems:
    raise SystemExit("::error::" + "; ".join(problems))

print(
    "POM declares "
    + group + ":" + artifact + ":" + version
    + " with the metadata Central requires and all "
    + str(len(expected_api))
    + " api dependencies at compile scope"
)
PY

unzip -p "${dir}/${ARTIFACT_ID}-${VERSION}-sources.jar" com/whop/api/core/ClientOptions.java > "${RUNNER_TEMP}/ClientOptions.java"
if ! grep -q "\"X-Fern-SDK-Version\", \"${VERSION}\"" "${RUNNER_TEMP}/ClientOptions.java"; then
  echo "::error::the sources jar does not report ${VERSION} on the wire."
  grep -n 'X-Fern-SDK-Version' "${RUNNER_TEMP}/ClientOptions.java" || true
  exit 1
fi

rm -rf "${RUNNER_TEMP}/jar"
mkdir -p "${RUNNER_TEMP}/jar"
unzip -q "${dir}/${ARTIFACT_ID}-${VERSION}.jar" -d "${RUNNER_TEMP}/jar"

carriers=$(grep -rlaF 'X-Fern-SDK-Version' "${RUNNER_TEMP}/jar" || true)
if [ -z "$carriers" ]; then
  echo "::error::no class in the main jar names X-Fern-SDK-Version. The header moved, and the build no longer stamps what ships."
  exit 1
fi

echo "compiled carriers of X-Fern-SDK-Version:"
printf '%s\n' "$carriers"

while IFS= read -r carrier; do
  if ! grep -qaF "$VERSION" "$carrier"; then
    echo "::error::${carrier} names X-Fern-SDK-Version but does not carry ${VERSION}; the published jar would report a different version on the wire."
    exit 1
  fi
done <<< "$carriers"

echo "bundle verified:"
find "$BUNDLE_DIR" -type f | sort

# Verify the public repository update
set -euo pipefail
python3 -m unittest discover -s .github -p 'test_public_maven.py'
python3 .github/public-maven.py "$BUNDLE_DIR" "${RUNNER_TEMP}/public-site" "$VERSION" "$GITHUB_SHA"

# Verify an installed Gradle consumer
./gradlew --no-daemon -p .github/package-smoke run \
  -PsdkRepository="$PWD/$BUNDLE_DIR" -PsdkVersion="$VERSION" --args="$VERSION"

# Verify an installed Maven consumer
mvn --batch-mode --no-transfer-progress -f .github/package-smoke/pom.xml \
  -Dmaven.repo.local="${RUNNER_TEMP}/maven-consumer" \
  -DsdkRepository="file://$PWD/$BUNDLE_DIR" -DsdkVersion="$VERSION" \
  compile exec:java -Dexec.args="$VERSION"
