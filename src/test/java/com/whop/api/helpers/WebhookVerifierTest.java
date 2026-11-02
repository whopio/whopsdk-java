package com.whop.api.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;

/**
 * Every fixture here is signed THE WAY THE BACKEND SIGNS, by
 * {@link #backendSignature}, and never by the class under test. Signing and
 * verifying with the same code is self-consistent and proves nothing: it is
 * exactly how the other SDKs' helpers shipped agreeing with themselves while
 * rejecting every genuine Whop delivery.
 *
 * <p>So that the oracle itself is not just this file agreeing with this file,
 * {@link #theFixtureSignerMatchesAnIndependentlyComputedVector()} pins it
 * against a vector computed in Python — {@code base64(hmac_sha256(secret,
 * b"<id>.<ts>." + body))} — before any other test relies on it.
 */
class WebhookVerifierTest {

    /** The format WebhooksManager::Create issues: "ws_" + SecureRandom.hex(32). */
    private static final String KEY = "ws_" + repeat("3f2a", 16);

    private static final String OTHER_KEY = "ws_" + repeat("c17b", 16);

    private static final String BODY =
            "{\"id\":\"msg_9Fq1\",\"type\":\"product.created\",\"data\":{\"id\":\"prod_7Kd2\"}}";

    private static final String ID = "msg_9Fq1";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ---------------------------------------------------------------- the oracle

    /**
     * backend/app/services/webhooks_manager/sign_webhook.rb, transcribed:
     *
     * <pre>
     *   payload   = "#{id}.#{timestamp}.#{body_json}"
     *   raw_sig   = OpenSSL::HMAC.digest("sha256", secret, payload)
     *   signature = Base64.strict_encode64(raw_sig)
     *   header    = "v1,#{signature}"
     * </pre>
     *
     * The secret is the RAW HMAC key, prefix and all — it is not decoded, not
     * stripped and not re-encoded.
     */
    private static String backendSignature(String key, String id, String timestamp, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((id + "." + timestamp + ".").getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(mac.doFinal(body));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void theFixtureSignerMatchesAnIndependentlyComputedVector() {
        // A body with characters outside ASCII, so the vector pins the UTF-8
        // BYTES that are signed rather than a sequence of Java chars: 85
        // characters, 89 bytes.
        String body = "{\"id\":\"msg_9Fq1\",\"type\":\"product.created\","
                + "\"data\":{\"id\":\"prod_7Kd2\",\"title\":\"Caf\u00e9 \ud83c\udf1f\"}}";
        byte[] raw = body.getBytes(StandardCharsets.UTF_8);

        assertEquals(86, body.length());
        assertEquals(89, raw.length);
        assertEquals(
                "1n/ft+WOoFeNWuarlpjbJiFzqiFs93ZXYzFp+ynUDAE=", backendSignature(KEY, "msg_9Fq1", "1787000000", raw));
    }

    // ---------------------------------------------------------------- fixtures

    private static Map<String, String> signedHeaders() {
        return signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), KEY, ID, now());
    }

    private static Map<String, String> signedHeaders(byte[] body, String key, String id, long timestamp) {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("webhook-id", id);
        headers.put("webhook-timestamp", Long.toString(timestamp));
        headers.put("webhook-signature", "v1," + backendSignature(key, id, Long.toString(timestamp), body));
        return headers;
    }

    private static Map<String, String> withoutHeader(Map<String, String> headers, String name) {
        Map<String, String> copy = new LinkedHashMap<>(headers);
        copy.remove(name);
        return copy;
    }

    private static Map<String, String> withHeader(Map<String, String> headers, String name, String value) {
        Map<String, String> copy = new LinkedHashMap<>(headers);
        copy.put(name, value);
        return copy;
    }

    private static long now() {
        return System.currentTimeMillis() / 1000L;
    }

    private static String repeat(String unit, int times) {
        StringBuilder text = new StringBuilder(unit.length() * times);
        for (int i = 0; i < times; i++) {
            text.append(unit);
        }
        return text.toString();
    }

    // ---------------------------------------------------------------- accepts

    @Test
    void returnsTheParsedBodyForAValidSignature() {
        JsonNode event = WebhookVerifier.unwrap(BODY, signedHeaders(), KEY);

        assertEquals("msg_9Fq1", event.get("id").asText());
        assertEquals("product.created", event.get("type").asText());
        assertEquals("prod_7Kd2", event.get("data").get("id").asText());
    }

    @Test
    void acceptsTheRawBytesOfANonAsciiBody() {
        String body = "{\"id\":\"msg_9Fq1\",\"note\":\"Caf\u00e9 \ud83c\udf1f\"}";
        byte[] raw = body.getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = signedHeaders(raw, KEY, ID, now());

        assertEquals(
                "Caf\u00e9 \ud83c\udf1f",
                WebhookVerifier.unwrap(raw, headers, KEY).get("note").asText());
    }

    @Test
    void acceptsHeadersWhoseNamesAreCapitalized() {
        Map<String, String> headers = new LinkedHashMap<>();
        for (Map.Entry<String, String> header : signedHeaders().entrySet()) {
            String[] words = header.getKey().split("-");
            StringBuilder name = new StringBuilder();
            for (String word : words) {
                if (name.length() > 0) {
                    name.append('-');
                }
                name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
            headers.put(name.toString(), header.getValue());
        }

        assertEquals("Webhook-Id", headers.keySet().iterator().next());
        assertEquals(
                "msg_9Fq1", WebhookVerifier.unwrap(BODY, headers, KEY).get("id").asText());
    }

    /** What {@code java.net.http.HttpHeaders#map()} hands back. */
    @Test
    void acceptsMultiValuedHeaderMaps() {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        for (Map.Entry<String, String> header : signedHeaders().entrySet()) {
            headers.put(header.getKey(), Collections.singletonList(header.getValue()));
        }

        assertEquals(
                "msg_9Fq1", WebhookVerifier.unwrap(BODY, headers, KEY).get("id").asText());
    }

    /**
     * The header can carry a space-separated list of versioned entries. An
     * unknown version is ignored rather than refused, so a scheme added
     * alongside v1 does not break a handler that only knows v1.
     */
    @Test
    void acceptsAValidV1EntryWhereverItAppearsInTheList() {
        Map<String, String> headers = signedHeaders();
        String valid = headers.get("webhook-signature");
        String filler = repeat("A", 44);

        for (String value : Arrays.asList(
                "v1," + filler + " " + valid,
                valid + " v1," + filler,
                "v0," + repeat("B", 44) + " " + valid + " v2," + repeat("C", 44),
                "v1n,not-base64!! " + valid)) {
            JsonNode event = WebhookVerifier.unwrap(BODY, withHeader(headers, "webhook-signature", value), KEY);

            assertEquals("msg_9Fq1", event.get("id").asText(), "expected \"" + value + "\" to verify on its v1 entry");
        }
    }

    @Test
    void acceptsATimestampAtTheEdgeOfTheToleranceWindow() {
        long edge = now() - WebhookVerifier.TOLERANCE_SECONDS + 10;
        Map<String, String> headers = signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), KEY, ID, edge);

        assertEquals(
                "msg_9Fq1", WebhookVerifier.unwrap(BODY, headers, KEY).get("id").asText());
    }

    // ---------------------------------------------------------------- refuses

    @Test
    void refusesATamperedBody() {
        String tampered = BODY.replace("prod_7Kd2", "prod_0000");

        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(tampered, signedHeaders(), KEY));
    }

    /**
     * The signature covers the exact bytes sent, so a body that has been parsed
     * and written back out does not verify even though it carries the same
     * data. In Java that is the live risk: round-tripping a request through a
     * Map or a model and verifying the result would fail on every delivery.
     */
    @Test
    void refusesABodyReserializedWithTheSameContent() throws Exception {
        String reserialized = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(BODY));

        assertNotEquals(BODY, reserialized);
        assertEquals(MAPPER.readTree(BODY), MAPPER.readTree(reserialized));
        assertThrows(
                WebhookVerificationException.class, () -> WebhookVerifier.unwrap(reserialized, signedHeaders(), KEY));
    }

    @Test
    void refusesASignatureMadeWithADifferentKey() {
        Map<String, String> headers = signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), OTHER_KEY, ID, now());

        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));
    }

    /**
     * The backend HMACs the stored secret as-is, so a secret and that same
     * secret minus its prefix are two different keys. Stripping either — which
     * the Standard Webhooks libraries do for {@code whsec_} — silently derives
     * the wrong one.
     */
    @Test
    void usesTheSecretVerbatimWithoutStrippingAPrefix() {
        String prefixed = "whsec_" + repeat("9d4e", 16);
        String bare = prefixed.substring("whsec_".length());
        Map<String, String> headers = signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), prefixed, ID, now());

        assertEquals(
                "msg_9Fq1",
                WebhookVerifier.unwrap(BODY, headers, prefixed).get("id").asText());
        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, bare));
    }

    @Test
    void refusesASignatureBoundToADifferentMessageId() {
        Map<String, String> headers = withHeader(signedHeaders(), "webhook-id", "msg_replaced");

        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));
    }

    @Test
    void refusesEachMissingSignatureHeader() {
        for (String name : Arrays.asList("webhook-id", "webhook-timestamp", "webhook-signature")) {
            Map<String, String> headers = withoutHeader(signedHeaders(), name);

            WebhookVerificationException error = assertThrows(
                    WebhookVerificationException.class,
                    () -> WebhookVerifier.unwrap(BODY, headers, KEY),
                    "expected a missing " + name + " to be refused");

            assertTrue(error.getMessage().contains(name), error.getMessage());
        }
    }

    @Test
    void refusesAnEmptyOrBlankSignatureHeader() {
        for (String value : Arrays.asList("", "   ")) {
            Map<String, String> headers = withHeader(signedHeaders(), "webhook-signature", value);

            assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));
        }
    }

    @Test
    void refusesAMalformedSignatureHeader() {
        for (String value : Arrays.asList(
                "not-a-signature", "v1,", "v1," + repeat("A", 44), "v2,abc", "v1,!!!not base64!!!", ",")) {
            Map<String, String> headers = withHeader(signedHeaders(), "webhook-signature", value);

            assertThrows(
                    WebhookVerificationException.class,
                    () -> WebhookVerifier.unwrap(BODY, headers, KEY),
                    "expected \"" + value + "\" to be refused");
        }
    }

    @Test
    void refusesATimestampOutsideTheToleranceWindow() {
        for (long timestamp : Arrays.asList(now() - 3600, now() + 3600)) {
            Map<String, String> headers = signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), KEY, ID, timestamp);

            WebhookVerificationException error =
                    assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));

            assertTrue(error.getMessage().contains("webhook-timestamp"), error.getMessage());
        }
    }

    /**
     * Back-dating the header on an otherwise genuine delivery also invalidates
     * the signature, so "it was refused" alone would not say which check fired.
     * The error has to name the timestamp, which is what shows the tolerance
     * window is enforced AHEAD of the comparison — the shape the replay defence
     * actually needs, and the one the e2e suite asserts against a captured
     * delivery.
     */
    @Test
    void refusesABackDatedHeaderForTheTimestampRatherThanTheSignature() {
        Map<String, String> headers = withHeader(signedHeaders(), "webhook-timestamp", Long.toString(now() - 3600));

        WebhookVerificationException error =
                assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));

        assertTrue(error.getMessage().contains("webhook-timestamp"), error.getMessage());
    }

    @Test
    void refusesASignatureBoundToADifferentTimestamp() {
        long timestamp = now();
        Map<String, String> headers = withHeader(
                signedHeaders(BODY.getBytes(StandardCharsets.UTF_8), KEY, ID, timestamp),
                "webhook-timestamp",
                Long.toString(timestamp - 60));

        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));
    }

    @Test
    void refusesAnUnparsableTimestamp() {
        Map<String, String> headers = withHeader(signedHeaders(), "webhook-timestamp", "not-a-timestamp");

        WebhookVerificationException error =
                assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(BODY, headers, KEY));

        assertTrue(error.getMessage().contains("webhook-timestamp"), error.getMessage());
    }

    // ---------------------------------------------------------------- misuse

    @Test
    void raisesAClearErrorWhenTheKeyIsMissing() {
        for (String key : Arrays.asList(null, "")) {
            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class, () -> WebhookVerifier.unwrap(BODY, signedHeaders(), key));

            assertEquals(WebhookVerifier.MISSING_KEY_MESSAGE, error.getMessage());
        }
    }

    /** Before anything is read off the headers, so a misuse is never reported as a verification failure. */
    @Test
    void raisesBeforeVerifyingWhenTheKeyIsMissing() {
        assertThrows(
                IllegalArgumentException.class,
                () -> WebhookVerifier.unwrap(BODY, Collections.<String, String>emptyMap(), null));
    }

    @Test
    void raisesWhenTheBodyOrTheHeadersAreMissing() {
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.unwrap((String) null, signedHeaders(), KEY));
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.unwrap((byte[]) null, signedHeaders(), KEY));
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.unwrap(BODY, null, KEY));
    }
}
