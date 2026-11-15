# Install the Whop Java SDK

The public Maven repository needs no GitHub token or registry account. It serves `com.whop.api:whop-java`; Maven Central supplies the SDK's dependencies. The POM includes OkHttp's JVM artifact for Maven users; Gradle selects the platform variant through module metadata.

## Android and Gradle

Add to `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://whopio.github.io/whopsdk-java/")
            content { includeGroup("com.whop.api") }
        }
    }
}
```

Add to the app module's dependencies:

```kotlin
implementation("com.whop.api:whop-java:2.8.1")
```

The SDK's OkHttp 5 dependency requires Android API 21 or newer. Enable core library desugaring for `java.time` and Java 8 APIs on older Android versions:

```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}
```

Declare the `android.permission.INTERNET` permission in your manifest. R8 consumer rules ship inside the SDK JAR; no app-owned SDK keep rules are needed.

## Server-side Java and Maven

Use Java 8 or newer. Add this repository and dependency to `pom.xml`:

```xml
<repositories>
  <repository>
    <id>whop-public</id>
    <url>https://whopio.github.io/whopsdk-java/</url>
  </repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>com.whop.api</groupId>
    <artifactId>whop-java</artifactId>
    <version>2.8.1</version>
  </dependency>
</dependencies>
```

## Use the sandbox

```java
import com.whop.api.WhopApiClient;
import com.whop.api.core.Environment;

WhopApiClient client = WhopApiClient.builder()
    .token(sandboxToken)
    .environment(Environment.SANDBOX)
    .build();
```

Android callers must run synchronous SDK methods off the main thread or use `AsyncWhopApiClient`. Keep privileged business API keys on your server; do not embed them in a shipped mobile app. For personal sandbox testing, supply a scoped sandbox token at runtime.

Products, variants, checkout configurations, and payments are exposed as `client.products()`, `client.variants()`, `client.checkoutConfigurations()`, and `client.payments()`. See [reference.md](reference.md) for typed request builders and [MIGRATING.md](MIGRATING.md) for API naming changes.

## Release provenance

Each version directory includes `release.json` with the source commit and SHA-256 hashes of the artifacts. Releases retain their original bytes. The [release workflow](.github/workflows/publish-main.yml) verifies installation from this public repository before creating the version tag.
