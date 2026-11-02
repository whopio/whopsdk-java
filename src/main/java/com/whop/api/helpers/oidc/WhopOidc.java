package com.whop.api.helpers.oidc;

import com.whop.api.AsyncWhopApiClientBuilder;
import com.whop.api.WhopApiClientBuilder;
import com.whop.api.core.ClientOptions;
import com.whop.api.core.LoggingInterceptor;
import com.whop.api.types.ApiKeyApiVersionDate;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Client builders that authenticate every request with an {@link OidcToken}. Configure them like the builders from
 * {@code WhopApiClient.builder()}, without calling {@code token(...)}:
 *
 * <pre>{@code
 * WhopApiClient whop = WhopOidc.clientBuilder(
 *         OidcToken.of("aurl_xxxxxxxxxxxxx", IdentityTokens.githubActions()))
 *     .build();
 * }</pre>
 *
 * <p>Unlike {@code WhopApiClient.builder()}, they send an {@code Api-Version-Date} header: the API version this SDK was
 * generated from, so responses match its types. A federated credential has no stored API version, so without the
 * header the API answers with its original 2025-01-01 shapes. Call {@code apiVersionDate(...)} to pin another version.
 * Without one, {@code build()} looks the generated version up and throws {@link IllegalStateException} if it can't.
 */
public final class WhopOidc {
    private static final String API_VERSION_HEADER = "Api-Version-Date";

    private WhopOidc() {}

    /**
     * The generator writes every API version the spec lists as a constant of {@code versions}, such as
     * {@link ApiKeyApiVersionDate}, so the newest is the one this client was generated from and moves with each
     * regeneration. Versions sort as strings, as the API sorts them.
     */
    static String newestApiVersion(Class<?> versions) {
        return Arrays.stream(versions.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && field.getType() == versions)
                .map(WhopOidc::versionOf)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException(versions.getSimpleName() + " lists no API versions."));
    }

    private static String versionOf(Field constant) {
        try {
            return constant.get(null).toString();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * A {@link WhopApiClientBuilder} whose requests carry the current access token from {@code token}. A failed
     * exchange throws its {@link OidcTokenException} from the call.
     */
    public static WhopApiClientBuilder clientBuilder(OidcToken token) {
        return clientBuilder(token, ApiKeyApiVersionDate.class);
    }

    static WhopApiClientBuilder clientBuilder(OidcToken token, Class<?> apiVersions) {
        return new WhopApiClientBuilder() {
            private boolean apiVersionPinned;

            @Override
            public WhopApiClientBuilder apiVersionDate(String apiVersionDate) {
                apiVersionPinned = true;
                return super.apiVersionDate(apiVersionDate);
            }

            @Override
            protected void setAuthentication(ClientOptions.Builder builder) {
                builder.addHeader("Authorization", () -> "Bearer " + token.get());
            }

            @Override
            protected void setCustomHeaders(ClientOptions.Builder builder) {
                super.setCustomHeaders(builder);
                if (!apiVersionPinned) {
                    builder.addHeader(API_VERSION_HEADER, newestApiVersion(apiVersions));
                }
            }
        };
    }

    /**
     * An {@link AsyncWhopApiClientBuilder} whose requests carry the current access token from {@code token}. A request
     * waits for an exchange on OkHttp's dispatcher thread, never the caller's, and a failed exchange fails the returned
     * future with the {@link OidcTokenException} in its cause chain.
     */
    public static AsyncWhopApiClientBuilder asyncClientBuilder(OidcToken token) {
        return asyncClientBuilder(token, ApiKeyApiVersionDate.class);
    }

    static AsyncWhopApiClientBuilder asyncClientBuilder(OidcToken token, Class<?> apiVersions) {
        return new AsyncWhopApiClientBuilder() {
            private boolean apiVersionPinned;

            @Override
            public AsyncWhopApiClientBuilder apiVersionDate(String apiVersionDate) {
                apiVersionPinned = true;
                return super.apiVersionDate(apiVersionDate);
            }

            @Override
            protected void setAuthentication(ClientOptions.Builder builder) {}

            @Override
            protected void setCustomHeaders(ClientOptions.Builder builder) {
                super.setCustomHeaders(builder);
                if (!apiVersionPinned) {
                    builder.addHeader(API_VERSION_HEADER, newestApiVersion(apiVersions));
                }
            }

            @Override
            protected ClientOptions buildClientOptions() {
                ClientOptions options = super.buildClientOptions();
                OkHttpClient.Builder http = options.httpClient().newBuilder();
                // ClientOptions.Builder.build() appends a LoggingInterceptor of its own, so drop the first one.
                http.interceptors().removeIf(LoggingInterceptor.class::isInstance);
                http.addInterceptor(authorization(token));
                return ClientOptions.Builder.from(options)
                        .httpClient(http.build())
                        .timeout(Optional.empty())
                        .build();
            }
        };
    }

    private static Interceptor authorization(OidcToken token) {
        return chain -> {
            Request request = chain.request();
            if (request.header("Authorization") != null) {
                return chain.proceed(request);
            }
            return chain.proceed(request.newBuilder()
                    .header("Authorization", "Bearer " + accessToken(token))
                    .build());
        };
    }

    private static String accessToken(OidcToken token) throws IOException {
        try {
            return token.get();
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
