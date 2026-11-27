# Moving to the regenerated Java client

The client follows the public API contract dated `2026-11-13-1`. Review generated request and response types when updating an existing integration.

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

The `OidcToken`, `IdentityTokens`, and `WhopOidc` entrypoints remain available. Apply API environment settings to the returned builder; an OIDC token exchange with a custom host also needs its own `OidcToken.Builder.baseUrl(...)`.

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

## Android release builds

The SDK JAR includes consumer rules in `META-INF/proguard/whop-java.pro`. Android builds consume these automatically when R8 is enabled. They preserve Jackson model annotations, generated request and response models, and the `NullableNonemptyFilter` constructor and methods used through reflection.

Without the filter rule, an optimized app can read API responses but fail while serializing a request with nullable fields. A debug build does not exercise that shrinker behavior. Validate a minified app when changing these rules.

## Installing and releasing the package

See [INSTALL.md](INSTALL.md) for public Gradle and Maven installation. The coordinate is `com.whop.api:whop-java`, served without credentials from `https://whopio.github.io/whopsdk-java/`.

This repository is built and delivered by the SDK pipeline in whop-monorepo (`sdks/java` there), one commit per delivery to `main`. A release is a reviewed bump of `sdks/fern/sdk-version.txt` in whop-monorepo: the delivery carries the version into `build.gradle`, and `publish-main.yml` publishes a version this Maven repository does not have yet. It runs the SDK tests and an installed Maven consumer, preserves the immutable bundle on the `maven` branch, deploys the repository to GitHub Pages from the `main`-only `github-pages` Environment, and verifies an anonymous consumer before `.github/release/cli.ts` creates the GitHub release and tag. A push that changes no version publishes nothing.

A retry from the same source commit reuses verified stored artifact bytes. The workflow refuses to replace an existing version from another commit or publish a version older than an existing one.
