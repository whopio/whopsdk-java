import com.fasterxml.jackson.databind.ObjectMapper;
import com.whop.api.AsyncWhopApiClient;
import com.whop.api.WhopApiClient;
import com.whop.api.core.Environment;
import com.whop.api.helpers.oidc.OidcToken;
import com.whop.api.helpers.oidc.WhopOidc;
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
        List<Request> requests = new ArrayList<>();
        OkHttpClient transport = new OkHttpClient.Builder().addInterceptor(chain -> {
            requests.add(chain.request());
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1)
                    .code(200).message("OK").body(ResponseBody.create(
                            new ObjectMapper().writeValueAsBytes(java.util.Collections.singletonMap("id", "user_smoke")),
                            MediaType.parse("application/json"))).build();
        }).build();
        try (WhopApiClient client = WhopApiClient.builder()
                .environment(Environment.SANDBOX).token("package-smoke").httpClient(transport).build();
             AsyncWhopApiClient async = AsyncWhopApiClient.builder()
                .environment(Environment.SANDBOX).token("package-smoke").httpClient(transport).build()) {
            client.users().retrieve("me");
            async.users().retrieve("me").get(10, TimeUnit.SECONDS);
            for (Request request : requests) {
                require(version.equals(request.header("X-Fern-SDK-Version")), "installed jar sent an incorrect SDK version");
                require("sandbox-api.whop.com".equals(request.url().host()), "sandbox client selected the wrong host");
                require("Bearer package-smoke".equals(request.header("Authorization")), "authentication header is missing");
                require(request.header("Api-Version-Date") != null, "API version pin is missing");
            }
            require(requests.size() == 2, "sync and async calls did not both complete");
        } finally {
            transport.dispatcher().executorService().shutdown();
            transport.connectionPool().evictAll();
        }
        try (WhopApiClient syncOidc = WhopOidc.clientBuilder(OidcToken.of("aurl_smoke", () -> "not-requested")).build();
             AsyncWhopApiClient asyncOidc = WhopOidc.asyncClientBuilder(OidcToken.of("aurl_smoke", () -> "not-requested")).build()) {
            require(syncOidc != null && asyncOidc != null, "OIDC helpers are missing from the installed jar");
        }
        System.out.println("Verified Maven consumer, transitive dependencies, sync/async routing, version headers and OIDC entrypoints for " + version);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
