import com.fasterxml.jackson.databind.ObjectMapper;
import com.whop.api.AsyncWhopApiClient;
import com.whop.api.WhopApiClient;
import com.whop.api.core.Environment;
import com.whop.api.helpers.oidc.OidcToken;
import com.whop.api.helpers.oidc.WhopOidc;
import com.whop.api.resources.stats.requests.RetrieveStatsRequest;
import com.whop.api.types.ApiKeyApiVersionDate;
import com.whop.api.types.FunnelStep;
import com.whop.api.types.RetrieveStatsRequestSteps;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class PackageConsumer {
    public static void main(String[] args) throws Exception {
        String version = args[0];
        require(
                version.equals(WhopApiClient.class.getPackage().getImplementationVersion()),
                "installed jar manifest has an incorrect version");
        try (java.io.InputStream rules =
                WhopApiClient.class.getResourceAsStream("/META-INF/proguard/whop-java.pro")) {
            require(rules != null, "installed jar is missing Android consumer rules");
            String contents = new String(rules.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            require(
                    contents.contains("-keep class com.whop.api.core.NullableNonemptyFilter { *; }"),
                    "installed jar does not preserve the reflective nullable filter");
            require(
                    contents.contains("com.whop.api.types.**")
                            && contents.contains("com.whop.api.resources.**.requests.**")
                            && contents.contains("-keepattributes Signature,*Annotation*,InnerClasses,EnclosingMethod"),
                    "installed jar does not preserve Jackson models and annotations");
        }
        String apiVersion = java.util.Arrays.stream(ApiKeyApiVersionDate.class.getFields())
                .filter(field ->
                        Modifier.isStatic(field.getModifiers()) && field.getType() == ApiKeyApiVersionDate.class)
                .map(field -> {
                    try {
                        return field.get(null).toString();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException(exception);
                    }
                })
                .max(String::compareTo)
                .orElseThrow(IllegalStateException::new);
        List<Request> requests = new ArrayList<>();
        OkHttpClient transport = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    requests.add(chain.request());
                    Object body = chain.request().url().encodedPath().contains("/stats/")
                            ? java.util.Collections.singletonMap(
                                    "data",
                                    java.util.Collections.singletonMap("points", java.util.Collections.emptyList()))
                            : java.util.Collections.singletonMap("id", "user_smoke");
                    return new Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .body(ResponseBody.create(
                                    new ObjectMapper().writeValueAsBytes(body), MediaType.parse("application/json")))
                            .build();
                })
                .build();
        try (WhopApiClient client = WhopApiClient.builder()
                        .environment(Environment.SANDBOX)
                        .token("package-smoke")
                        .httpClient(transport)
                        .build();
                AsyncWhopApiClient async = AsyncWhopApiClient.builder()
                        .environment(Environment.SANDBOX)
                        .token("package-smoke")
                        .httpClient(transport)
                        .build()) {
            client.users().retrieve("me");
            async.users().retrieve("me").get(10, TimeUnit.SECONDS);
            client.stats()
                    .retrieve(
                            "funnel",
                            RetrieveStatsRequest.builder()
                                    .from("2026-11-01")
                                    .to("2026-11-02")
                                    .steps(RetrieveStatsRequestSteps.builder()
                                            ._1(FunnelStep.builder()
                                                    .event("pixel.page")
                                                    .page("/pricing*")
                                                    .build())
                                            ._2(FunnelStep.builder()
                                                    .event("payment.completed")
                                                    .build())
                                            .build())
                                    .build());
            for (Request request : requests) {
                require(
                        version.equals(request.header("X-Fern-SDK-Version")),
                        "installed jar sent an incorrect SDK version");
                require("sandbox-api.whop.com".equals(request.url().host()), "sandbox client selected the wrong host");
                require(
                        "Bearer package-smoke".equals(request.header("Authorization")),
                        "authentication header is missing");
                require(
                        apiVersion.equals(request.header("Api-Version-Date")),
                        "API version pin does not match the generated models");
            }
            require(requests.size() == 3, "sync, async and stats calls did not all complete");
            okhttp3.HttpUrl funnelUrl = requests.get(2).url();
            require(
                    "pixel.page".equals(funnelUrl.queryParameter("steps[1][event]")),
                    "first funnel event lost its one-based deep-object key");
            require(
                    "/pricing*".equals(funnelUrl.queryParameter("steps[1][page]")),
                    "funnel filter lost its nested key");
            require(
                    "payment.completed".equals(funnelUrl.queryParameter("steps[2][event]")),
                    "second funnel event lost its one-based deep-object key");
        } finally {
            transport.dispatcher().executorService().shutdown();
            transport.connectionPool().evictAll();
        }
        try (WhopApiClient syncOidc = WhopOidc.clientBuilder(OidcToken.of("aurl_smoke", () -> "not-requested"))
                        .build();
                AsyncWhopApiClient asyncOidc = WhopOidc.asyncClientBuilder(
                                OidcToken.of("aurl_smoke", () -> "not-requested"))
                        .build()) {
            require(syncOidc != null && asyncOidc != null, "OIDC helpers are missing from the installed jar");
        }
        System.out.println(
                "Verified Maven consumer, transitive dependencies, sync/async routing, version headers and OIDC entrypoints for "
                        + version);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
