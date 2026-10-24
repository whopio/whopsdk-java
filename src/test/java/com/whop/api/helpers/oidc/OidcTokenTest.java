package com.whop.api.helpers.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.whop.api.AsyncWhopApiClient;
import com.whop.api.WhopApiClient;
import com.whop.api.core.ILogger;
import com.whop.api.core.LogConfig;
import com.whop.api.core.LogLevel;
import com.whop.api.types.ApiKeyApiVersionDate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OidcTokenTest {
    private static final String ROLE_ID = "aurl_xxxxxxxxxxxxx";
    private static final String NO_MATCH =
            "{\"error\":{\"type\":\"bad_request\",\"message\":\"No trust linked to this role accepts this token.\",\"code\":\"oidc_exchange_no_match\"}}";
    private static final String TRUST_RATE_LIMITED =
            "{\"error\":{\"type\":\"too_many_requests\",\"message\":\"Too many token exchanges for this trust. Try again in a minute.\",\"code\":\"oidc_exchange_rate_limited\"}}";
    private static final String NETWORK_RATE_LIMITED = "{\"message\":\"You are being rate limited\"}";
    private static final String TOKEN_1 = "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}";
    private static final String TOKEN_2 = "{\"access_token\":\"whop-token-2\",\"expires_in\":3600}";
    private static final Instant START = Instant.parse("2026-10-23T12:00:00Z");
    private static final ObjectMapper JSON = new ObjectMapper();

    private HttpServer server;
    private volatile long responseDelayMillis;
    private final Queue<Integer> statuses = new ConcurrentLinkedQueue<>();
    private final Map<String, String> responseHeaders = new ConcurrentHashMap<>();
    private final List<Recorded> recorded = Collections.synchronizedList(new ArrayList<>());

    private static final class Recorded {
        final String method;
        final String path;
        final String query;
        final String authorization;
        final String metadataFlavor;
        final String apiVersionDate;
        final String body;

        Recorded(
                String method,
                String path,
                String query,
                String authorization,
                String metadataFlavor,
                String apiVersionDate,
                String body) {
            this.method = method;
            this.path = path;
            this.query = query;
            this.authorization = authorization;
            this.metadataFlavor = metadataFlavor;
            this.apiVersionDate = apiVersionDate;
            this.body = body;
        }
    }

    private String serve(int status, String... bodies) throws IOException {
        Iterator<String> responses = java.util.Arrays.asList(bodies).iterator();
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            recorded.add(new Recorded(
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getPath(),
                    exchange.getRequestURI().getRawQuery(),
                    exchange.getRequestHeaders().getFirst("Authorization"),
                    exchange.getRequestHeaders().getFirst("Metadata-Flavor"),
                    exchange.getRequestHeaders().getFirst("Api-Version-Date"),
                    read(exchange.getRequestBody())));
            pause(responseDelayMillis);
            byte[] response = (responses.hasNext() ? responses.next() : "{}").getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            responseHeaders.forEach(exchange.getResponseHeaders()::add);
            Integer next = statuses.poll();
            exchange.sendResponseHeaders(next == null ? status : next, response.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(response);
            }
        });
        server.start();
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private static String read(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int read;
        while ((read = in.read(chunk)) != -1) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toString("UTF-8");
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static <T extends Throwable> T causeOfType(Throwable error, Class<T> type) {
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            if (type.isInstance(cause)) {
                return type.cast(cause);
            }
        }
        return fail("No " + type.getSimpleName() + " in the cause chain of " + error);
    }

    private static Duration assertBacksOffAboutSeconds(long seconds, Instant failedAt, Instant retryAt) {
        Duration backoff = Duration.between(failedAt, retryAt);
        Duration base = Duration.ofSeconds(seconds);
        assertFalse(backoff.compareTo(base.multipliedBy(9).dividedBy(10)) < 0, backoff + " is under 90% of " + base);
        assertTrue(backoff.compareTo(base.multipliedBy(11).dividedBy(10)) < 0, backoff + " is 110% of " + base);
        return backoff;
    }

    private OidcToken token(String address, Clock clock) {
        return OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .clock(clock)
                .build();
    }

    private Instant retryAtAfterARefusal(String address, String... headerNamesAndValues) {
        responseHeaders.clear();
        for (int i = 0; i < headerNamesAndValues.length; i += 2) {
            responseHeaders.put(headerNamesAndValues[i], headerNamesAndValues[i + 1]);
        }
        OidcToken token = token(address, new MutableClock(START));
        return assertThrows(OidcTokenException.class, token::get).getRetryAt();
    }

    private static void awaitRenewal(OidcToken token) throws Exception {
        CompletableFuture<String> renewal = token.renewal();
        if (renewal != null) {
            renewal.handle((accessToken, error) -> null).get(10, TimeUnit.SECONDS);
        }
    }

    private static List<String> getConcurrently(OidcToken token, int callers) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(callers);
        try {
            CountDownLatch start = new CountDownLatch(1);
            List<Future<String>> results = new ArrayList<>();
            for (int i = 0; i < callers; i++) {
                results.add(pool.submit(() -> {
                    start.await();
                    return token.get();
                }));
            }
            start.countDown();
            List<String> accessTokens = new ArrayList<>();
            for (Future<String> result : results) {
                accessTokens.add(result.get(10, TimeUnit.SECONDS));
            }
            return accessTokens;
        } finally {
            pool.shutdownNow();
        }
    }

    private static String newestGeneratedApiVersion() throws ReflectiveOperationException {
        String newest = null;
        for (ApiKeyApiVersionDate.Value value : ApiKeyApiVersionDate.Value.values()) {
            if (value != ApiKeyApiVersionDate.Value.UNKNOWN) {
                String version = ApiKeyApiVersionDate.class
                        .getField(value.name())
                        .get(null)
                        .toString();
                assertTrue(version.matches("\\d{4}-\\d{2}-\\d{2}(-\\d+)?"), version);
                if (newest == null || version.compareTo(newest) > 0) {
                    newest = version;
                }
            }
        }
        return newest;
    }

    private static void requestUserIgnoringTheResponse(WhopApiClient whop) {
        try {
            whop.users().retrieve("user_xxxxxxxxxxxxx");
        } catch (RuntimeException ignored) {
            // The test servers answer with partial users; only the request matters.
        }
    }

    @AfterEach
    void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void exchangesTheIdentityTokenForAnAccessTokenAssumingTheRole() throws IOException {
        String address = serve(200, "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}");

        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address + "/api/v1/")
                .build();

        assertEquals("whop-token-1", token.get());
        Recorded request = recorded.get(0);
        assertEquals("POST", request.method);
        assertEquals("/api/v1/access_tokens/exchange", request.path);
        JsonNode body = JSON.readTree(request.body);
        assertEquals(
                "urn:ietf:params:oauth:grant-type:token-exchange",
                body.get("grant_type").asText());
        assertEquals(
                "urn:ietf:params:oauth:token-type:id_token",
                body.get("subject_token_type").asText());
        assertEquals("id-token", body.get("subject_token").asText());
        assertEquals(ROLE_ID, body.get("role_id").asText());
    }

    @Test
    void reusesTheTokenUntilTwoToTenMinutesBeforeExpiryThenExchangesAFreshIdentityToken() throws Exception {
        String address = serve(
                200,
                "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}",
                "{\"access_token\":\"whop-token-2\",\"expires_in\":3600}");
        MutableClock clock = new MutableClock(Instant.parse("2026-10-23T12:00:00Z"));
        Iterator<String> identities =
                java.util.Arrays.asList("id-token-1", "id-token-2").iterator();
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(identities::next)
                .baseUrl(address)
                .clock(clock)
                .build();

        assertEquals("whop-token-1", token.get());
        clock.advance(Duration.ofMinutes(50).minusMillis(1));
        assertEquals("whop-token-1", token.get());
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofMinutes(8).plusMillis(1));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
        assertEquals(
                "id-token-2",
                JSON.readTree(recorded.get(1).body).get("subject_token").asText());
    }

    @Test
    void aShortLivedTokenRenewsHalfwayThroughItsLifetime() throws Exception {
        String address = serve(
                200,
                "{\"access_token\":\"whop-token-1\",\"expires_in\":240}",
                "{\"access_token\":\"whop-token-2\",\"expires_in\":240}");
        MutableClock clock = new MutableClock(Instant.parse("2026-10-23T12:00:00Z"));
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .clock(clock)
                .build();

        assertEquals("whop-token-1", token.get());
        clock.advance(Duration.ofSeconds(119));
        assertEquals("whop-token-1", token.get());
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofSeconds(1));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
    }

    @Test
    void concurrentCallersShareOneExchange() throws Exception {
        String address = serve(200, "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}");
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .build();

        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            results.add(pool.submit(token::get));
        }
        for (Future<String> result : results) {
            assertEquals("whop-token-1", result.get());
        }
        pool.shutdown();

        assertEquals(1, recorded.size());
    }

    @Test
    void concurrentCallersShareAFailedExchangeAndItsErrorUntilTheBackoffEnds() throws Exception {
        responseDelayMillis = 500;
        String address = serve(400, NO_MATCH);
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);

        ExecutorService pool = Executors.newFixedThreadPool(5);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            results.add(pool.submit(() -> {
                start.await();
                return token.get();
            }));
        }
        start.countDown();
        List<OidcTokenException> errors = new ArrayList<>();
        for (Future<String> result : results) {
            ExecutionException error = assertThrows(ExecutionException.class, result::get);
            errors.add(causeOfType(error, OidcTokenException.class));
        }
        pool.shutdown();

        assertEquals(1, recorded.size());
        OidcTokenException shared = errors.get(0);
        for (OidcTokenException error : errors) {
            assertSame(shared, error);
        }
        assertEquals("oidc_exchange_no_match", shared.getCode());
        Duration backoff = assertBacksOffAboutSeconds(2, START, shared.getRetryAt());

        responseDelayMillis = 0;
        clock.advance(backoff.minusMillis(1));
        assertSame(shared, assertThrows(OidcTokenException.class, token::get));
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofMillis(1));
        assertNotSame(shared, assertThrows(OidcTokenException.class, token::get));
        assertEquals(2, recorded.size());
    }

    @Test
    void concurrentCallersShareAFailedRenewalAndKeepTheCurrentToken() throws Exception {
        statuses.addAll(java.util.Arrays.asList(200, 503));
        String address = serve(200, TOKEN_1, "{}");
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);
        assertEquals("whop-token-1", token.get());

        responseDelayMillis = 500;
        clock.advance(Duration.ofMinutes(58));
        assertEquals(Collections.nCopies(5, "whop-token-1"), getConcurrently(token, 5));
        awaitRenewal(token);
        assertEquals("whop-token-1", token.get());

        assertEquals(2, recorded.size());
    }

    @Test
    void pastItsRenewalPointTheCurrentTokenReturnsAtOnceWhileASlowRenewalRuns() throws Exception {
        String address = serve(200, TOKEN_1, TOKEN_2);
        MutableClock clock = new MutableClock(START);
        CountDownLatch identityRequested = new CountDownLatch(2);
        CountDownLatch releaseIdentity = new CountDownLatch(1);
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> {
                    identityRequested.countDown();
                    if (identityRequested.getCount() == 0) {
                        try {
                            releaseIdentity.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return "id-token";
                })
                .baseUrl(address)
                .clock(clock)
                .build();
        assertEquals("whop-token-1", token.get());

        clock.advance(Duration.ofMinutes(58));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals("whop-token-1", token.get());
            assertTrue(identityRequested.await(2, TimeUnit.SECONDS));
            assertEquals(Collections.nCopies(5, "whop-token-1"), getConcurrently(token, 5));
            assertEquals("whop-token-1", token.get());
        });
        assertEquals(1, recorded.size());

        releaseIdentity.countDown();
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
        assertEquals(2, recorded.size());
    }

    @Test
    void aHungIdentitySourceTimesOutAtTheRenewalDeadlineAndLaterRenewalsProceed() throws Exception {
        String address = serve(200, TOKEN_1, TOKEN_2);
        MutableClock clock = new MutableClock(START);
        AtomicInteger requested = new AtomicInteger();
        CountDownLatch abandonedCallInterrupted = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        CountDownLatch lateReturned = new CountDownLatch(1);
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> {
                    if (requested.incrementAndGet() == 1) {
                        while (release.getCount() > 0) {
                            try {
                                release.await();
                            } catch (InterruptedException e) {
                                abandonedCallInterrupted.countDown();
                            }
                        }
                        lateReturned.countDown();
                        return "late-id-token";
                    }
                    return "id-token";
                })
                .baseUrl(address)
                .renewalTimeout(Duration.ofMillis(300))
                .clock(clock)
                .build();

        long startedAt = System.nanoTime();
        OidcTokenException timedOut = assertTimeoutPreemptively(
                Duration.ofSeconds(5), () -> assertThrows(OidcTokenException.class, token::get));
        assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt) >= 300);
        assertEquals("The token renewal timed out after 30 seconds.", timedOut.getMessage());
        assertTrue(timedOut.getCause() instanceof TimeoutException, String.valueOf(timedOut.getCause()));
        Duration backoff = assertBacksOffAboutSeconds(2, START, timedOut.getRetryAt());
        assertTrue(abandonedCallInterrupted.await(5, TimeUnit.SECONDS));
        assertSame(timedOut, assertThrows(OidcTokenException.class, token::get));
        assertEquals(1, requested.get());

        release.countDown();
        assertTrue(lateReturned.await(5, TimeUnit.SECONDS));
        pause(300);
        assertEquals(0, recorded.size());

        clock.advance(backoff);
        assertEquals("whop-token-1", token.get());
        assertEquals(2, requested.get());
        assertEquals(
                "id-token",
                JSON.readTree(recorded.get(0).body).get("subject_token").asText());

        clock.advance(Duration.ofMinutes(58));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
        assertEquals(3, requested.get());
        assertEquals(2, recorded.size());
    }

    @Test
    void aHungRenewalPastTheRenewalPointKeepsTheCurrentTokenThenRenewsAfterTheBackoff() throws Exception {
        String address = serve(200, TOKEN_1, TOKEN_2);
        MutableClock clock = new MutableClock(START);
        AtomicInteger requested = new AtomicInteger();
        CountDownLatch never = new CountDownLatch(1);
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> {
                    if (requested.incrementAndGet() == 2) {
                        try {
                            never.await();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                    return "id-token";
                })
                .baseUrl(address)
                .renewalTimeout(Duration.ofMillis(300))
                .clock(clock)
                .build();
        assertEquals("whop-token-1", token.get());

        clock.advance(Duration.ofMinutes(58));
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> assertEquals("whop-token-1", token.get()));
        awaitRenewal(token);
        assertEquals("whop-token-1", token.get());
        assertEquals(2, requested.get());
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofMillis(2201));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
        assertEquals(3, requested.get());
        assertEquals(2, recorded.size());
    }

    @Test
    void callersWithNoUsableTokenWaitForTheRenewalAndShareItsToken() throws Exception {
        String address = serve(200, TOKEN_1, TOKEN_2);
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);
        assertEquals("whop-token-1", token.get());

        responseDelayMillis = 500;
        clock.advance(Duration.ofMinutes(59).plusSeconds(30));
        assertEquals(Collections.nCopies(5, "whop-token-2"), getConcurrently(token, 5));
        assertEquals(2, recorded.size());
    }

    @Test
    void aFailedRenewalKeepsReturningTheCurrentTokenAndRenewsAgainAfterTheBackoff() throws Exception {
        statuses.addAll(java.util.Arrays.asList(200, 503, 503, 200));
        String address = serve(200, TOKEN_1, "{}", "{}", TOKEN_2);
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);
        assertEquals("whop-token-1", token.get());

        clock.advance(Duration.ofMinutes(58));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-1", token.get());
        assertEquals(2, recorded.size());

        clock.advance(Duration.ofMillis(1799));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals(2, recorded.size());
        clock.advance(Duration.ofMillis(402));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals(3, recorded.size());

        clock.advance(Duration.ofMillis(3599));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals(3, recorded.size());
        clock.advance(Duration.ofMillis(802));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals("whop-token-2", token.get());
        assertEquals(4, recorded.size());
    }

    @Test
    void aFailedRenewalThrowsItsErrorOnceTheTokenHasThirtySecondsLeft() throws Exception {
        statuses.addAll(java.util.Arrays.asList(200, 503));
        responseHeaders.put("Retry-After", "30");
        String address = serve(
                200,
                "{\"access_token\":\"whop-token-1\",\"expires_in\":80}",
                "{}",
                "{\"access_token\":\"whop-token-2\",\"expires_in\":80}");
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);
        assertEquals("whop-token-1", token.get());

        clock.advance(Duration.ofSeconds(40));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals(2, recorded.size());
        clock.advance(Duration.ofSeconds(10).minusMillis(1));
        assertEquals("whop-token-1", token.get());

        clock.advance(Duration.ofMillis(1));
        OidcTokenException error = assertThrows(OidcTokenException.class, token::get);
        assertEquals(503, error.getStatus());
        assertEquals(START.plusSeconds(70), error.getRetryAt());
        assertSame(error, assertThrows(OidcTokenException.class, token::get));
        assertEquals(2, recorded.size());

        clock.advance(Duration.ofSeconds(20));
        assertEquals("whop-token-2", token.get());
        assertEquals(3, recorded.size());
    }

    @Test
    void theBackoffStartsAtTwoSecondsAndDoublesToAMinuteThenRestartsAfterASuccess() throws Exception {
        for (int i = 0; i < 7; i++) {
            statuses.add(503);
        }
        statuses.addAll(java.util.Arrays.asList(200, 503, 503));
        String address = serve(200, "{}", "{}", "{}", "{}", "{}", "{}", "{}", TOKEN_1, "{}", "{}");
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);

        long[] seconds = {2, 4, 8, 16, 32, 60, 60};
        for (int failures = 1; failures <= seconds.length; failures++) {
            Instant failedAt = clock.instant();
            OidcTokenException error = assertThrows(OidcTokenException.class, token::get);
            assertEquals(failures, recorded.size());
            Duration backoff = assertBacksOffAboutSeconds(seconds[failures - 1], failedAt, error.getRetryAt());
            clock.advance(backoff.minusMillis(1));
            assertSame(error, assertThrows(OidcTokenException.class, token::get));
            assertEquals(failures, recorded.size());
            clock.advance(Duration.ofMillis(1));
        }
        assertEquals("whop-token-1", token.get());
        assertEquals(8, recorded.size());

        clock.advance(Duration.ofMinutes(58));
        assertEquals("whop-token-1", token.get());
        awaitRenewal(token);
        assertEquals(9, recorded.size());
        clock.advance(Duration.ofMillis(1799));
        token.get();
        awaitRenewal(token);
        assertEquals(9, recorded.size());
        clock.advance(Duration.ofMillis(402));
        token.get();
        awaitRenewal(token);
        assertEquals(10, recorded.size());
    }

    @Test
    void aRefusedExchangeWaitsAsLongAsRetryAfterOrElseRateLimitResetAsksUpToAMinute() throws IOException {
        String address = serve(429, TRUST_RATE_LIMITED);
        String in37Seconds = Long.toString(START.plusSeconds(37).getEpochSecond());

        assertEquals(START.plusSeconds(30), retryAtAfterARefusal(address, "Retry-After", "30"));
        assertEquals(
                START.plusSeconds(45),
                retryAtAfterARefusal(
                        address,
                        "Retry-After",
                        DateTimeFormatter.RFC_1123_DATE_TIME.format(
                                ZonedDateTime.ofInstant(START.plusSeconds(45), ZoneOffset.UTC))));
        assertEquals(START.plusSeconds(37), retryAtAfterARefusal(address, "RateLimit-Reset", in37Seconds));
        assertEquals(
                START.plusSeconds(20),
                retryAtAfterARefusal(address, "Retry-After", "20", "RateLimit-Reset", in37Seconds));
        assertEquals(
                START.plusSeconds(37),
                retryAtAfterARefusal(address, "Retry-After", "soon", "RateLimit-Reset", in37Seconds));
        assertEquals(START.plusSeconds(60), retryAtAfterARefusal(address, "Retry-After", "120"));
        assertEquals(
                START.plusSeconds(60),
                retryAtAfterARefusal(
                        address,
                        "RateLimit-Reset",
                        Long.toString(START.plusSeconds(600).getEpochSecond())));
        assertBacksOffAboutSeconds(
                2,
                START,
                retryAtAfterARefusal(
                        address,
                        "Retry-After",
                        "0",
                        "RateLimit-Reset",
                        Long.toString(START.minusSeconds(5).getEpochSecond())));
        assertBacksOffAboutSeconds(2, START, retryAtAfterARefusal(address));
    }

    @Test
    void aRateLimitedExchangeIsNotRepeatedOnEveryCall() throws IOException {
        Instant reset = START.plusSeconds(30);
        responseHeaders.put("RateLimit-Reset", Long.toString(reset.getEpochSecond()));
        String address = serve(429, NETWORK_RATE_LIMITED);
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);

        for (int i = 0; i < 10; i++) {
            OidcTokenException error = assertThrows(OidcTokenException.class, token::get);
            assertEquals(429, error.getStatus());
            assertEquals(reset, error.getRetryAt());
        }
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofSeconds(30).minusMillis(1));
        assertThrows(OidcTokenException.class, token::get);
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofMillis(1));
        assertThrows(OidcTokenException.class, token::get);
        assertEquals(2, recorded.size());
    }

    @Test
    void anIdentityTokenFailureIsCachedAsAnOidcTokenExceptionWithItsRetryTime() throws IOException {
        String address = serve(200, TOKEN_1);
        AtomicInteger requested = new AtomicInteger();
        IllegalStateException unreachable = new IllegalStateException("The metadata server is unreachable.");
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> {
                    requested.incrementAndGet();
                    throw unreachable;
                })
                .baseUrl(address)
                .clock(new MutableClock(START))
                .build();

        OidcTokenException error = assertThrows(OidcTokenException.class, token::get);
        assertSame(unreachable, error.getCause());
        assertBacksOffAboutSeconds(2, START, error.getRetryAt());
        assertSame(error, assertThrows(OidcTokenException.class, token::get));
        assertEquals(1, requested.get());

        OidcTokenException refused = new OidcTokenException("The GitHub Actions identity token request failed.");
        OidcToken github = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> {
                    throw refused;
                })
                .baseUrl(address)
                .clock(new MutableClock(START))
                .build();

        OidcTokenException cached = assertThrows(OidcTokenException.class, github::get);
        assertEquals(refused.getMessage(), cached.getMessage());
        assertBacksOffAboutSeconds(2, START, cached.getRetryAt());
        assertNull(refused.getRetryAt());
        assertEquals(0, recorded.size());
    }

    @Test
    void aSuccessfulExchangeAfterAFailureIsReusedUntilItsRenewal() throws IOException {
        statuses.add(503);
        String address = serve(200, "{}", TOKEN_1);
        MutableClock clock = new MutableClock(START);
        OidcToken token = token(address, clock);

        assertEquals(503, assertThrows(OidcTokenException.class, token::get).getStatus());
        clock.advance(Duration.ofSeconds(3));
        assertEquals("whop-token-1", token.get());
        clock.advance(Duration.ofMinutes(10));
        assertEquals("whop-token-1", token.get());
        assertEquals(2, recorded.size());
    }

    @Test
    void surfacesTheApiErrorAndCode() throws IOException {
        String address = serve(400, NO_MATCH);
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .build();

        OidcTokenException error = assertThrows(OidcTokenException.class, token::get);

        assertEquals("No trust linked to this role accepts this token.", error.getMessage());
        assertEquals(400, error.getStatus());
        assertEquals("oidc_exchange_no_match", error.getCode());
        assertNotNull(error.getRetryAt());
    }

    @Test
    void requiresARoleAndAnIdentitySource() {
        assertThrows(
                OidcTokenException.class,
                () -> OidcToken.builder().identityToken(() -> "id-token").build());
        assertThrows(
                OidcTokenException.class,
                () -> OidcToken.builder().roleId(ROLE_ID).build());
    }

    @Test
    void theClientSendsTheExchangedToken() throws IOException {
        String address = serve(
                200, "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}", "{\"id\":\"user_xxxxxxxxxxxxx\"}");
        WhopApiClient whop = WhopOidc.clientBuilder(OidcToken.builder()
                        .roleId(ROLE_ID)
                        .identityToken(() -> "id-token")
                        .baseUrl(address)
                        .build())
                .url(address)
                .maxRetries(0)
                .build();

        requestUserIgnoringTheResponse(whop);

        Recorded apiCall = recorded.get(1);
        assertTrue(apiCall.path.startsWith("/users/user_xxxxxxxxxxxxx"), apiCall.path);
        assertEquals("Bearer whop-token-1", apiCall.authorization);
    }

    @Test
    void theClientsSendTheNewestApiVersionOfTheGeneratedClientUnlessTheCallerPinsAnother() throws Exception {
        String address = serve(200, TOKEN_1);
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .build();

        requestUserIgnoringTheResponse(
                WhopOidc.clientBuilder(token).url(address).maxRetries(0).build());
        WhopOidc.asyncClientBuilder(token)
                .url(address)
                .maxRetries(0)
                .build()
                .users()
                .retrieve("user_xxxxxxxxxxxxx")
                .handle((user, error) -> null)
                .get(10, TimeUnit.SECONDS);
        requestUserIgnoringTheResponse(WhopOidc.clientBuilder(token)
                .apiVersionDate("2026-07-08")
                .url(address)
                .maxRetries(0)
                .build());

        String newest = newestGeneratedApiVersion();
        assertNotNull(newest);
        assertEquals(newest, recorded.get(1).apiVersionDate);
        assertEquals(newest, recorded.get(2).apiVersionDate);
        assertEquals("2026-07-08", recorded.get(3).apiVersionDate);
    }

    @Test
    void anEmptyListOfApiVersionsFailsOnlyTheBuildsThatDoNotPinOne() throws Exception {
        String address = serve(200, TOKEN_1);
        OidcToken token = token(address, Clock.systemUTC());

        IllegalStateException unlisted =
                assertThrows(IllegalStateException.class, () -> WhopOidc.clientBuilder(token, NoApiVersions.class)
                        .url(address)
                        .build());
        assertEquals("NoApiVersions lists no API versions.", unlisted.getMessage());
        assertThrows(IllegalStateException.class, () -> WhopOidc.asyncClientBuilder(token, NoApiVersions.class)
                .url(address)
                .build());

        requestUserIgnoringTheResponse(WhopOidc.clientBuilder(token, NoApiVersions.class)
                .apiVersionDate("2026-07-08")
                .url(address)
                .maxRetries(0)
                .build());
        WhopOidc.asyncClientBuilder(token, NoApiVersions.class)
                .apiVersionDate("2026-07-08")
                .url(address)
                .maxRetries(0)
                .build()
                .users()
                .retrieve("user_xxxxxxxxxxxxx")
                .handle((user, error) -> null)
                .get(10, TimeUnit.SECONDS);

        assertEquals("2026-07-08", recorded.get(1).apiVersionDate);
        assertEquals("2026-07-08", recorded.get(2).apiVersionDate);
        assertEquals(newestGeneratedApiVersion(), WhopOidc.newestApiVersion(ApiKeyApiVersionDate.class));
    }

    @Test
    void theAsyncClientExchangesOffTheCallersThreadAndSendsTheExchangedToken() throws Exception {
        String address = serve(
                200, "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}", "{\"id\":\"user_xxxxxxxxxxxxx\"}");
        AtomicReference<Thread> exchangedOn = new AtomicReference<>();
        AsyncWhopApiClient whop = WhopOidc.asyncClientBuilder(OidcToken.builder()
                        .roleId(ROLE_ID)
                        .identityToken(() -> {
                            exchangedOn.set(Thread.currentThread());
                            return "id-token";
                        })
                        .baseUrl(address)
                        .build())
                .url(address)
                .maxRetries(0)
                .build();

        whop.users()
                .retrieve("user_xxxxxxxxxxxxx")
                .handle((user, error) -> null)
                .get(10, TimeUnit.SECONDS);

        assertNotEquals(Thread.currentThread(), exchangedOn.get());
        Recorded apiCall = recorded.get(1);
        assertTrue(apiCall.path.startsWith("/users/user_xxxxxxxxxxxxx"), apiCall.path);
        assertEquals("Bearer whop-token-1", apiCall.authorization);
    }

    @Test
    void theAsyncClientKeepsTheGeneratedRetriesAndLogsEachAttemptOnce() throws Exception {
        statuses.addAll(java.util.Arrays.asList(200, 503));
        String address = serve(
                200,
                "{\"access_token\":\"whop-token-1\",\"expires_in\":3600}",
                "{}",
                "{\"id\":\"user_xxxxxxxxxxxxx\"}");
        RequestLog log = new RequestLog();
        AsyncWhopApiClient whop = WhopOidc.asyncClientBuilder(OidcToken.builder()
                        .roleId(ROLE_ID)
                        .identityToken(() -> "id-token")
                        .baseUrl(address)
                        .build())
                .url(address)
                .maxRetries(1)
                .logging(LogConfig.builder()
                        .level(LogLevel.DEBUG)
                        .logger(log)
                        .silent(false)
                        .build())
                .build();

        whop.users()
                .retrieve("user_xxxxxxxxxxxxx")
                .handle((user, error) -> null)
                .get(10, TimeUnit.SECONDS);

        assertEquals(3, recorded.size());
        assertEquals("Bearer whop-token-1", recorded.get(1).authorization);
        assertEquals("Bearer whop-token-1", recorded.get(2).authorization);
        assertEquals(2, log.requests.size());
    }

    @Test
    void theAsyncClientFailsTheFutureWhenTheExchangeFails() throws IOException {
        String address = serve(400, NO_MATCH);
        AsyncWhopApiClient whop = WhopOidc.asyncClientBuilder(OidcToken.builder()
                        .roleId(ROLE_ID)
                        .identityToken(() -> "id-token")
                        .baseUrl(address)
                        .build())
                .url(address)
                .maxRetries(0)
                .build();

        CompletableFuture<?> result = whop.users().retrieve("user_xxxxxxxxxxxxx");

        ExecutionException error = assertThrows(ExecutionException.class, () -> result.get(10, TimeUnit.SECONDS));
        assertEquals(
                "oidc_exchange_no_match",
                causeOfType(error, OidcTokenException.class).getCode());
        assertEquals(1, recorded.size());
    }

    @Test
    void githubActionsRequestsTheWhopAudienceAndExplainsAMissingPermission() throws IOException {
        String address = serve(200, "{\"value\":\"github-id-token\"}");
        OkHttpClient http = new OkHttpClient();

        String token = IdentityTokens.githubActions(
                        IdentityTokens.DEFAULT_AUDIENCE, http, address + "/idtoken?api-version=2.0", "request-token")
                .get();

        assertEquals("github-id-token", token);
        assertEquals("api-version=2.0&audience=https%3A%2F%2Fapi.whop.com", recorded.get(0).query);
        assertEquals("Bearer request-token", recorded.get(0).authorization);

        OidcTokenException missing = assertThrows(OidcTokenException.class, () -> IdentityTokens.githubActions(
                        IdentityTokens.DEFAULT_AUDIENCE, http, null, null)
                .get());
        assertTrue(missing.getMessage().contains("id-token: write"));
    }

    @Test
    void googleCloudAsksTheMetadataServer() throws IOException {
        String address = serve(200, "google-id-token\n");

        String token = IdentityTokens.googleCloud("https://example.com", new OkHttpClient(), address + "/identity")
                .get();

        assertEquals("google-id-token", token);
        assertEquals("audience=https%3A%2F%2Fexample.com", recorded.get(0).query);
        assertEquals("Google", recorded.get(0).metadataFlavor);
    }

    private static final class NoApiVersions {}

    private static final class RequestLog implements ILogger {
        final List<String> requests = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void debug(String message) {
            if (message.startsWith("HTTP Request:")) {
                requests.add(message);
            }
        }

        @Override
        public void info(String message) {}

        @Override
        public void warn(String message) {}

        @Override
        public void error(String message) {}
    }

    private static final class MutableClock extends Clock {
        private volatile Instant now;

        MutableClock(Instant now) {
            this.now = now;
        }

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public java.time.ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }
    }
}
