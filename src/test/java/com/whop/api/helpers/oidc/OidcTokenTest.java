package com.whop.api.helpers.oidc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpServer;
import com.whop.api.WhopApiClient;
import com.whop.api.core.ObjectMappers;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class OidcTokenTest {
    private static final String ROLE_ID = "aurl_xxxxxxxxxxxxx";

    private HttpServer server;
    private final List<Recorded> recorded = Collections.synchronizedList(new ArrayList<>());

    private static final class Recorded {
        final String method;
        final String path;
        final String query;
        final String authorization;
        final String metadataFlavor;
        final String body;

        Recorded(String method, String path, String query, String authorization, String metadataFlavor, String body) {
            this.method = method;
            this.path = path;
            this.query = query;
            this.authorization = authorization;
            this.metadataFlavor = metadataFlavor;
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
                    read(exchange.getRequestBody())));
            byte[] response = (responses.hasNext() ? responses.next() : "{}").getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, response.length);
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
        JsonNode body = ObjectMappers.JSON_MAPPER.readTree(request.body);
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
    void reusesTheTokenUntilAMinuteBeforeExpiryThenExchangesAFreshIdentityToken() throws IOException {
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
        clock.advance(Duration.ofMinutes(58));
        assertEquals("whop-token-1", token.get());
        assertEquals(1, recorded.size());

        clock.advance(Duration.ofMinutes(2));
        assertEquals("whop-token-2", token.get());
        assertEquals(
                "id-token-2",
                ObjectMappers.JSON_MAPPER
                        .readTree(recorded.get(1).body)
                        .get("subject_token")
                        .asText());
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
    void surfacesTheApiErrorAndCode() throws IOException {
        String address = serve(
                400,
                "{\"error\":{\"type\":\"bad_request\",\"message\":\"No trust linked to this role accepts this token.\",\"code\":\"oidc_exchange_no_match\"}}");
        OidcToken token = OidcToken.builder()
                .roleId(ROLE_ID)
                .identityToken(() -> "id-token")
                .baseUrl(address)
                .build();

        OidcTokenException error = assertThrows(OidcTokenException.class, token::get);

        assertEquals("No trust linked to this role accepts this token.", error.getMessage());
        assertEquals(400, error.getStatus());
        assertEquals("oidc_exchange_no_match", error.getCode());
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

        try {
            whop.users().retrieve("user_xxxxxxxxxxxxx");
        } catch (RuntimeException ignored) {
            // The response body is not a full user; only the request matters here.
        }

        Recorded apiCall = recorded.get(1);
        assertTrue(apiCall.path.startsWith("/users/user_xxxxxxxxxxxxx"), apiCall.path);
        assertEquals("Bearer whop-token-1", apiCall.authorization);
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

    private static final class MutableClock extends Clock {
        private Instant now;

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
