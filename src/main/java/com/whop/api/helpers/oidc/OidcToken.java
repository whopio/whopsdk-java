package com.whop.api.helpers.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A Whop access token that comes from your cloud provider's OIDC identity token instead of an API key.
 *
 * <p>Each {@link #get()} returns the current access token. The first call exchanges an identity token for one; later
 * calls reuse it until a minute before it expires, then exchange a fresh identity token. Concurrent callers share one
 * exchange and its outcome: when it fails, each of them gets the same {@link OidcTokenException}, and the next call
 * tries again. It uses OkHttp and Jackson rather than generated client code, so it survives the client being
 * regenerated. Build the client with {@link WhopOidc}:
 *
 * <pre>{@code
 * WhopApiClient whop = WhopOidc.clientBuilder(
 *         OidcToken.of("aurl_xxxxxxxxxxxxx", IdentityTokens.githubActions()))
 *     .build();
 * }</pre>
 */
public final class OidcToken implements Supplier<String> {
    private static final String DEFAULT_BASE_URL = "https://api.whop.com/api/v1";
    private static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
    private static final String ID_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:id_token";
    private static final Duration RENEW_BEFORE_EXPIRY = Duration.ofMinutes(1);
    private static final MediaType JSON = MediaType.get("application/json");
    static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private final String roleId;
    private final Supplier<String> identityToken;
    private final String exchangeUrl;
    private final OkHttpClient http;
    private final Clock clock;

    private final Object lock = new Object();
    private String current;
    private Instant renewAt = Instant.MIN;
    private CompletableFuture<String> pending;

    private OidcToken(Builder builder) {
        if (builder.roleId == null || builder.roleId.isEmpty()) {
            throw new OidcTokenException("Pass the role to assume as roleId.");
        }
        if (builder.identityToken == null) {
            throw new OidcTokenException("Pass a source of identity tokens as identityToken.");
        }
        this.roleId = builder.roleId;
        this.identityToken = builder.identityToken;
        this.exchangeUrl = builder.baseUrl.replaceAll("/+$", "") + "/access_tokens/exchange";
        this.http = builder.httpClient == null ? defaultHttpClient() : builder.httpClient;
        this.clock = builder.clock;
    }

    /** An access token that assumes {@code roleId}, prefixed {@code aurl_}, exchanged from {@code identityToken}. */
    public static OidcToken of(String roleId, Supplier<String> identityToken) {
        return builder().roleId(roleId).identityToken(identityToken).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /** The current access token, exchanging a fresh identity token when the last one is about to expire. */
    @Override
    public String get() {
        CompletableFuture<String> shared;
        CompletableFuture<String> started = null;
        synchronized (lock) {
            if (current != null && clock.instant().isBefore(renewAt)) {
                return current;
            }
            if (pending == null) {
                started = new CompletableFuture<>();
                pending = started;
            }
            shared = pending;
        }
        if (started != null) {
            try {
                started.complete(exchange());
            } catch (Throwable error) {
                started.completeExceptionally(error);
            } finally {
                synchronized (lock) {
                    pending = null;
                }
            }
        }
        return await(shared);
    }

    private static String await(CompletableFuture<String> exchange) {
        try {
            return exchange.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new OidcTokenException("The token exchange failed.", null, null, cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OidcTokenException("Interrupted while waiting for the token exchange.", null, null, e);
        }
    }

    private String exchange() {
        ObjectNode body = JSON_MAPPER.createObjectNode();
        body.put("grant_type", GRANT_TYPE);
        body.put("subject_token_type", ID_TOKEN_TYPE);
        body.put("subject_token", identityToken.get());
        body.put("role_id", roleId);

        Request request = new Request.Builder()
                .url(exchangeUrl)
                .post(RequestBody.create(body.toString(), JSON))
                .build();
        try (Response response = http.newCall(request).execute()) {
            JsonNode payload = parse(IdentityTokens.bodyText(response));
            if (!response.isSuccessful()) {
                JsonNode error = payload.path("error");
                String message = error.path("message").isTextual()
                        ? error.path("message").asText()
                        : "The token exchange failed with " + response.code() + ".";
                String code =
                        error.path("code").isTextual() ? error.path("code").asText() : null;
                throw new OidcTokenException(message, response.code(), code, null);
            }
            JsonNode accessToken = payload.path("access_token");
            JsonNode expiresIn = payload.path("expires_in");
            if (!accessToken.isTextual() || !expiresIn.canConvertToLong()) {
                throw new OidcTokenException(
                        "The token exchange returned no access token.", response.code(), null, null);
            }
            synchronized (lock) {
                current = accessToken.asText();
                renewAt = clock.instant().plusSeconds(expiresIn.asLong()).minus(RENEW_BEFORE_EXPIRY);
            }
            return accessToken.asText();
        } catch (IOException e) {
            throw new OidcTokenException("The token exchange request failed.", null, null, e);
        }
    }

    private static JsonNode parse(String body) {
        try {
            return JSON_MAPPER.readTree(body);
        } catch (IOException e) {
            return JSON_MAPPER.createObjectNode();
        }
    }

    static OkHttpClient defaultHttpClient() {
        return new OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build();
    }

    public static final class Builder {
        private String roleId;
        private Supplier<String> identityToken;
        private String baseUrl = DEFAULT_BASE_URL;
        private OkHttpClient httpClient;
        private Clock clock = Clock.systemUTC();

        private Builder() {}

        /** The role to assume, prefixed {@code aurl_}. A trust linked to it must accept the identity token. */
        public Builder roleId(String roleId) {
            this.roleId = roleId;
            return this;
        }

        /**
         * Returns an identity token from your cloud provider. It is called again for every exchange, so return a fresh
         * token rather than a stored one. See {@link IdentityTokens} for GitHub Actions and Google Cloud.
         */
        public Builder identityToken(Supplier<String> identityToken) {
            this.identityToken = identityToken;
            return this;
        }

        /** The API's base URL, for example {@code https://sandbox-api.whop.com/api/v1}. Defaults to production. */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /** The HTTP client the exchange uses. Defaults to one with a 10 second timeout. */
        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        Builder clock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public OidcToken build() {
            return new OidcToken(this);
        }
    }
}
