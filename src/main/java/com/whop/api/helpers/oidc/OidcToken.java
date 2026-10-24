package com.whop.api.helpers.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
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
 * calls reuse it until a random point two to ten minutes before it expires, so processes that started together don't
 * renew together, then exchange a fresh identity token. Concurrent callers share one exchange and its outcome.
 *
 * <p>After a failed exchange it waits before exchanging again: as long as the API's {@code Retry-After} or else
 * {@code RateLimit-Reset} header asks, up to a minute, or otherwise about 2 seconds, doubling with each failure in a row
 * up to a minute, give or take 10%. Meanwhile a failed renewal keeps returning the current token while it has more than
 * 30 seconds left. Once none does, every call throws the failure's {@link OidcTokenException}, whose
 * {@link OidcTokenException#getRetryAt()} says when it exchanges again.
 *
 * <p>It uses OkHttp and Jackson rather than generated client code, so it survives the client being regenerated. Build
 * the client with {@link WhopOidc}:
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
    private static final Duration RENEW_BEFORE_EXPIRY_MIN = Duration.ofMinutes(2);
    private static final Duration RENEW_BEFORE_EXPIRY_MAX = Duration.ofMinutes(10);
    private static final Duration UNUSABLE_BEFORE_EXPIRY = Duration.ofSeconds(30);
    private static final Duration FIRST_BACKOFF = Duration.ofSeconds(2);
    private static final Duration MAX_BACKOFF = Duration.ofMinutes(1);
    private static final double BACKOFF_JITTER = 0.1;
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
    private Instant usableUntil = Instant.MIN;
    private CompletableFuture<String> pending;
    private OidcTokenException failure;
    private Duration backoff = Duration.ZERO;

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
            Instant now = clock.instant();
            if (current != null && now.isBefore(renewAt)) {
                return current;
            }
            if (failure != null && now.isBefore(failure.getRetryAt())) {
                return currentOrFailure(now);
            }
            if (pending == null) {
                started = new CompletableFuture<>();
                pending = started;
            }
            shared = pending;
        }
        if (started != null) {
            try {
                started.complete(renew());
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

    private String renew() {
        try {
            return exchange();
        } catch (RuntimeException error) {
            synchronized (lock) {
                Instant now = clock.instant();
                backoff = backoff.isZero() ? FIRST_BACKOFF : shorter(backoff.multipliedBy(2), MAX_BACKOFF);
                failure = retryableFailure(error, now.plus(jittered(backoff)));
                return currentOrFailure(now);
            }
        }
    }

    private String currentOrFailure(Instant now) {
        if (now.isBefore(usableUntil)) {
            return current;
        }
        throw failure;
    }

    private static OidcTokenException retryableFailure(RuntimeException error, Instant backoffEnds) {
        if (!(error instanceof OidcTokenException)) {
            return new OidcTokenException("The token exchange failed.", null, null, backoffEnds, error);
        }
        OidcTokenException failed = (OidcTokenException) error;
        return failed.getRetryAt() != null ? failed : failed.withRetryAt(backoffEnds);
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
        Instant sentAt = clock.instant();
        try (Response response = http.newCall(request).execute()) {
            JsonNode payload = parse(IdentityTokens.bodyText(response));
            if (!response.isSuccessful()) {
                JsonNode error = payload.path("error");
                String message = error.path("message").isTextual()
                        ? error.path("message").asText()
                        : "The token exchange failed with " + response.code() + ".";
                String code =
                        error.path("code").isTextual() ? error.path("code").asText() : null;
                throw new OidcTokenException(message, response.code(), code, requestedRetryAt(response), null);
            }
            JsonNode accessToken = payload.path("access_token");
            JsonNode expiresIn = payload.path("expires_in");
            if (!accessToken.isTextual() || !expiresIn.canConvertToLong()) {
                throw new OidcTokenException(
                        "The token exchange returned no access token.", response.code(), null, null);
            }
            Duration lifetime = Duration.ofSeconds(expiresIn.asLong());
            synchronized (lock) {
                current = accessToken.asText();
                renewAt = renewAt(sentAt, lifetime);
                usableUntil = sentAt.plus(lifetime).minus(UNUSABLE_BEFORE_EXPIRY);
                failure = null;
                backoff = Duration.ZERO;
            }
            return accessToken.asText();
        } catch (IOException e) {
            throw new OidcTokenException("The token exchange request failed.", null, null, e);
        }
    }

    private static Instant renewAt(Instant sentAt, Duration lifetime) {
        Duration beforeExpiry =
                RENEW_BEFORE_EXPIRY_MIN.plus(randomUpTo(RENEW_BEFORE_EXPIRY_MAX.minus(RENEW_BEFORE_EXPIRY_MIN)));
        return sentAt.plus(lifetime).minus(shorter(beforeExpiry, lifetime.dividedBy(2)));
    }

    /**
     * When the API asked to be retried: {@code Retry-After} in seconds or as an HTTP date, or else
     * {@code RateLimit-Reset} in epoch seconds, as the API's network limit sends it, never more than a minute away.
     * {@code null} when neither names a time still to come.
     */
    private Instant requestedRetryAt(Response response) {
        Instant now = clock.instant();
        Duration wait = retryAfter(response.header("Retry-After"), now);
        if (!isPositive(wait)) {
            wait = untilEpochSecond(response.header("RateLimit-Reset"), now);
        }
        return isPositive(wait) ? now.plus(shorter(wait, MAX_BACKOFF)) : null;
    }

    private static Duration retryAfter(String header, Instant now) {
        if (header == null) {
            return null;
        }
        try {
            return Duration.ofSeconds(Long.parseLong(header));
        } catch (NumberFormatException e) {
            return untilHttpDate(header, now);
        }
    }

    private static Duration untilHttpDate(String header, Instant now) {
        try {
            return Duration.between(
                    now,
                    ZonedDateTime.parse(header, DateTimeFormatter.RFC_1123_DATE_TIME)
                            .toInstant());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static Duration untilEpochSecond(String header, Instant now) {
        try {
            return Duration.ofSeconds(Long.parseLong(header)).minusMillis(now.toEpochMilli());
        } catch (NumberFormatException | ArithmeticException e) {
            return null;
        }
    }

    private static boolean isPositive(Duration duration) {
        return duration != null && !duration.isNegative() && !duration.isZero();
    }

    private static Duration jittered(Duration duration) {
        double factor = 1 + BACKOFF_JITTER * (2 * ThreadLocalRandom.current().nextDouble() - 1);
        return Duration.ofMillis((long) (duration.toMillis() * factor));
    }

    private static Duration shorter(Duration a, Duration b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    private static Duration randomUpTo(Duration bound) {
        return Duration.ofMillis(ThreadLocalRandom.current().nextLong(bound.toMillis()));
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
