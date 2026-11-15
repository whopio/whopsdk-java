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
                    .getItems().size();
            System.out.println(count);
        }
    }
}
```

On Android, run these calls on a background thread.
Pass your API key to `.token(...)` to create products.
Keep business API keys on your server.

[Android and Maven setup](INSTALL.md) · [API reference](reference.md)
