#!/usr/bin/env bash
# whopsdk-java's release check: .github/workflows/publish-main.yml runs it once the maven job has published
# the version, to install it from Whop's public Maven repository as an anonymous consumer would; the github
# job tags the version only after it passes.
set -euo pipefail
cd "$(dirname "$0")/.."

PUBLIC_BASE=https://whopio.github.io/whopsdk-java
export GRADLE_USER_HOME="${RUNNER_TEMP}/public-consumer-gradle"

# Verify an anonymous installed consumer
set -euo pipefail
./gradlew --no-daemon --refresh-dependencies -p .github/package-smoke run \
  -PsdkRepository="$PUBLIC_BASE" -PsdkVersion="$VERSION" --args="$VERSION"
mvn --batch-mode --no-transfer-progress -f .github/package-smoke/pom.xml \
  -Dmaven.repo.local="${RUNNER_TEMP}/public-consumer-maven" \
  -DsdkRepository="$PUBLIC_BASE" -DsdkVersion="$VERSION" \
  compile exec:java -Dexec.args="$VERSION"
