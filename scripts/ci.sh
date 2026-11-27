#!/usr/bin/env bash
# whopsdk-java's CI: .github/workflows/ci.yml runs it, publish-main.yml runs it before it stages a
# release, and whop-monorepo's SDK pipeline runs it over every build it delivers.
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew --no-daemon compileJava
./gradlew --no-daemon test
