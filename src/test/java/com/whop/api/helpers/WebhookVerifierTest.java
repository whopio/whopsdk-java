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
 * Every fixture is signed the way the backend signs ({@link #backendSignature}), never by the class under test:
 * signing and verifying with the same code would agree with itself while rejecting genuine deliveries.
 * {@link #theFixtureSignerMatchesAnIndependentlyComputedVector()} pins that signer to a vector computed in Python.
 */
class WebhookVerifierTest {

    /** The format WebhooksManager::Create issues: "ws_" + SecureRandom.hex(32). */
    private static final String KEY = "ws_" + repeat("3f2a", 16);

    private static final String OTHER_KEY = "ws_" + repeat("c17b", 16);

    private static final String BODY =
            "{\"id\":\"msg_9Fq1\",\"type\":\"product.created\",\"data\":{\"id\":\"prod_7Kd2\"}}";

    private static final String ID = "msg_9Fq1";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** WebhooksManager::SignWebhook: the raw secret, prefix included, HMACs {@code "<id>.<timestamp>.<body>"}. */
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
        // Non-ASCII, so the vector pins the UTF-8 bytes that are signed rather than Java chars.
        String body = "{\"id\":\"msg_9Fq1\",\"type\":\"product.created\","
                + "\"data\":{\"id\":\"prod_7Kd2\",\"title\":\"Caf\u00e9 \ud83c\udf1f\"}}";
        byte[] raw = body.getBytes(StandardCharsets.UTF_8);

        assertEquals(86, body.length());
        assertEquals(89, raw.length);
        assertEquals(
                "1n/ft+WOoFeNWuarlpjbJiFzqiFs93ZXYzFp+ynUDAE=", backendSignature(KEY, "msg_9Fq1", "1787000000", raw));
    }

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

    /** An unknown version is skipped rather than refused, so a scheme added alongside v1 does not break v1. */
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

    @Test
    void refusesATamperedBody() {
        String tampered = BODY.replace("prod_7Kd2", "prod_0000");

        assertThrows(WebhookVerificationException.class, () -> WebhookVerifier.unwrap(tampered, signedHeaders(), KEY));
    }

    /** The signature covers the exact bytes sent, so a parsed and re-serialized body does not verify. */
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
     * The backend HMACs the stored secret as-is, so stripping a prefix (as Standard Webhooks libraries do for
     * {@code whsec_}) derives the wrong key.
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
     * Back-dating the header on a genuine delivery also invalidates the signature, so the error has to name the
     * timestamp to show the tolerance window is enforced ahead of the comparison.
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
