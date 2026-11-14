# Moving to the regenerated Java client

The client follows the public API contract dated `2026-11-13-1`. Review generated request and response types when updating an existing integration. The package stays at the unreleased `0.0.0` version until a separate release decision sets a real version.

## Choosing an environment

Both synchronous and asynchronous builders use `environment(...)`. Replace earlier `.url(...)` calls with a built-in environment or an explicit API/vault pair:

```java
WhopApiClient client = WhopApiClient.builder()
        .token(apiKey)
        .environment(Environment.SANDBOX)
        .build();
```

```java
Environment environment = Environment.custom()
        .api("http://localhost:3000/api/v1")
        .vault("http://localhost:3100/api/v1")
        .build();

WhopApiClient client = WhopApiClient.builder()
        .token(apiKey)
        .environment(environment)
        .build();
```

Import `com.whop.api.core.Environment`. Use the same environment setter on `AsyncWhopApiClient.builder()`. Set both custom URLs: regular operations use `api`, and operations that accept raw card details use `vault`.

## OIDC authentication

Tristan's `OidcToken`, `IdentityTokens`, and `WhopOidc` entrypoints remain available. Apply API environment settings to the returned builder; an OIDC token exchange with a custom host also needs its own `OidcToken.Builder.baseUrl(...)`.

```java
try (AsyncWhopApiClient client = WhopOidc.asyncClientBuilder(token)
        .environment(Environment.SANDBOX)
        .build()) {
    client.users().retrieve("me").get();
}
```

Closing a client releases HTTP resources the SDK created. A supplied `OkHttpClient` remains owned by its caller. The OIDC async wrapper preserves this ownership distinction.

## Version pinning

The generated builder sends the API version matching its models. An explicit `apiVersionDate(...)` still overrides that pin; select an older API date only when your integration also expects its response shapes.

## Installing and releasing the package

This change builds and tests the SDK without publishing it. `gradle.properties` keeps `version=0.0.0`, and the release workflow refuses to publish that placeholder. Generated metadata records the generator's input version; the release workflow stamps the artifact, source headers, and metadata from `gradle.properties` before building them.

After this change is merged, release with a separate pull request that changes only `version=0.0.0` to the approved version in `gradle.properties` (for example, `version=2.8.1`). Merging that version-only change publishes to GitHub Packages by default. Pull request runs always rehearse the release without uploading. Java releases use an explicit version and are outside the shared SDK release train.

GitHub Packages consumers use `com.whop.api:whop-java:<version>` with the repository `https://maven.pkg.github.com/whopio/whopsdk-java` and GitHub package-read credentials. A public Maven Central release additionally needs verified ownership of the `com.whop` namespace and signing/publishing credentials in a main-only `maven-central` GitHub Environment. Select the Maven Central target only after those prerequisites are configured.
