# Whop Java SDK

Call the Whop API from Java or Android.

## Install

Use Java 8 or later. Add this repository to `settings.gradle.kts`:

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

Add this dependency to your app:

```kotlin
dependencies {
    implementation("com.whop.api:whop-java:2.8.1")
}
```

## Read products

This example reads public products from the test environment.

```java
import com.whop.api.WhopApiClient;
import com.whop.api.core.Environment;
import com.whop.api.resources.products.requests.ListProductsRequest;

public class ReadProducts {
    public static void main(String[] args) {
        try (WhopApiClient client = WhopApiClient.builder()
                .environment(Environment.SANDBOX)
                .build()) {
            int count = client.products()
                    .list(ListProductsRequest.builder().first(1).build())
                    .getData().size();
            System.out.println(count);
        }
    }
}
```

On Android, run these calls on a background thread.
Pass your API key to `.token(...)` to create products.
Keep business API keys on your server.

## Verify a webhook

`WebhookVerifier.unwrap` checks the signature Whop sends with every webhook delivery and returns the parsed body.

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.whop.api.helpers.WebhookVerifier;

JsonNode event = WebhookVerifier.unwrap(rawBody, headers, webhookSecret);
```

Pass the raw request body, its headers and the endpoint's `ws_` signing secret exactly as Whop shows it. A missing signature header, a signature that does not match, or a timestamp more than five minutes from now throws `WebhookVerificationException`.

## Watch a live read

A live read answers now and again each time its answer changes. `Whop` is a `WhopApiClient` whose resource clients carry `watch` and `waitFor` beside each live read; both take exactly the read's parameters.

```java
import com.whop.api.Whop;
import com.whop.api.WhopApiClient;
import com.whop.api.live.LiveStream;
import com.whop.api.resources.stats.timeseries.requests.RetrieveTimeSeriesRequest;
import com.whop.api.resources.stats.timeseries.types.RetrieveTimeSeriesResponse;

Whop whop = Whop.of(WhopApiClient.builder().token(apiKey).build());
RetrieveTimeSeriesRequest request = RetrieveTimeSeriesRequest.builder()
        .from("<from>")
        .to("<to>")
        .build();

try (LiveStream<RetrieveTimeSeriesResponse> answers = whop.stats().timeSeries().watch("<metric>", request)) {
    for (RetrieveTimeSeriesResponse answer : answers) {
        System.out.println(answer);
    }
}
```

The stream reconnects on its own and ends when the read stops being live. `close()` from another thread ends the loop and closes the connection. `waitFor` takes the same parameters, then `until`, `failWhen` and `timeout`, and returns the first answer `until` accepts.

[Android and Maven setup](INSTALL.md) · [API reference](reference.md)
