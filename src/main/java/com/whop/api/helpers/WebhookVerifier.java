package com.whop.api.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Verifies the signature Whop sends on every webhook delivery, then parses the body.
 *
 * <pre>{@code
 * JsonNode event = WebhookVerifier.unwrap(rawBody, request.getHeaders(), webhookSigningSecret);
 * }</pre>
 *
 * <p>The signature is {@code base64(HMAC-SHA256(secret, "<webhook-id>.<webhook-timestamp>.<body>"))}, keyed with the
 * literal bytes of the {@code ws_…} secret. Standard Webhooks libraries base64-decode the key they are handed, so they
 * derive the wrong key for a Whop secret; this class computes the HMAC with the JDK instead and adds no dependency.
 *
 * <p>The body comes back as a {@link JsonNode}; map it to your own type with
 * {@code new ObjectMapper().treeToValue(event, YourType.class)}.
 */
public final class WebhookVerifier {

    /**
     * How far the {@code webhook-timestamp} header may be from now, in either direction, before the delivery is
     * refused as a possible replay.
     */
    public static final long TOLERANCE_SECONDS = 5 * 60;

    public static final String MISSING_KEY_MESSAGE =
            "Cannot verify a webhook without a key. Pass the endpoint's signing secret as `key`.";

    private static final String ID_HEADER = "webhook-id";
    private static final String TIMESTAMP_HEADER = "webhook-timestamp";
    private static final String SIGNATURE_HEADER = "webhook-signature";

    private static final String SUPPORTED_VERSION = "v1";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private WebhookVerifier() {}

    /**
     * Verifies {@code payload} against the signature headers and returns the parsed body.
     *
     * @param payload the raw, unmodified request body. The signature covers the exact bytes Whop sent, so a body that
     *     has been parsed and re-serialized will not verify: read the request as bytes, not as a model.
     * @param headers the request headers. Only {@code webhook-id}, {@code webhook-timestamp} and
     *     {@code webhook-signature} are read, case-insensitively. Values may be a {@link String} or a
     *     {@link Collection} of them, so both a {@code Map<String, String>} and the {@code Map<String, List<String>>}
     *     from {@code java.net.http.HttpHeaders#map()} work.
     * @param key the endpoint's signing secret exactly as Whop shows it, a {@code ws_}-prefixed string. Pass it
     *     verbatim: do not strip the prefix or encode it.
     * @return the parsed body.
     * @throws IllegalArgumentException if {@code payload}, {@code headers} or {@code key} is missing.
     * @throws WebhookVerificationException if a signature header is missing, the timestamp is outside the tolerance
     *     window, or no signature matches.
     */
    public static JsonNode unwrap(byte[] payload, Map<String, ?> headers, String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException(MISSING_KEY_MESSAGE);
        }
        if (payload == null) {
            throw new IllegalArgumentException("Cannot verify a webhook without the raw request body.");
        }
        if (headers == null) {
            throw new IllegalArgumentException("Cannot verify a webhook without the request headers.");
        }

        String id = requireHeader(headers, ID_HEADER);
        String timestamp = requireHeader(headers, TIMESTAMP_HEADER);
        String signatures = requireHeader(headers, SIGNATURE_HEADER);

        // Ahead of the comparison: a back-dated header also breaks the signature, and the error should name the
        // timestamp rather than report a mismatch.
        checkTimestamp(timestamp);

        byte[] expected = sign(key, id, timestamp, payload);
        if (!matches(signatures, expected)) {
            throw new WebhookVerificationException("No matching " + SUPPORTED_VERSION
                    + " signature in the webhook-signature header. The body must be the exact bytes received.");
        }

        try {
            return MAPPER.readTree(payload);
        } catch (Exception e) {
            throw new WebhookVerificationException("The webhook body is correctly signed but is not valid JSON.", e);
        }
    }

    /**
     * Verifies {@code payload} against the signature headers and returns the parsed body, signing the string as
     * UTF-8, which is what Whop sends. Prefer {@link #unwrap(byte[], Map, String)} when the raw bytes are available:
     * a body that was not well-formed UTF-8 does not survive the round trip through a string.
     *
     * @see #unwrap(byte[], Map, String)
     */
    public static JsonNode unwrap(String payload, Map<String, ?> headers, String key) {
        if (payload == null) {
            throw new IllegalArgumentException("Cannot verify a webhook without the raw request body.");
        }
        return unwrap(payload.getBytes(StandardCharsets.UTF_8), headers, key);
    }

    private static void checkTimestamp(String timestamp) {
        long sent;
        try {
            sent = Long.parseLong(timestamp.trim());
        } catch (NumberFormatException e) {
            throw new WebhookVerificationException(
                    "The webhook-timestamp header is not a unix timestamp: " + abbreviate(timestamp), e);
        }
        long now = System.currentTimeMillis() / 1000L;
        long drift = Math.abs(now - sent);
        if (drift > TOLERANCE_SECONDS) {
            throw new WebhookVerificationException("The webhook-timestamp header is " + drift
                    + "s away from now, outside the " + TOLERANCE_SECONDS + "s tolerance window.");
        }
    }

    private static byte[] sign(String key, String id, String timestamp, byte[] payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            mac.update((id + "." + timestamp + ".").getBytes(StandardCharsets.UTF_8));
            return mac.doFinal(payload);
        } catch (Exception e) {
            throw new WebhookVerificationException("Could not compute the expected webhook signature.", e);
        }
    }

    /**
     * The header is a space-separated list of {@code <version>,<base64>} entries. Unknown versions are skipped, so a
     * scheme signed alongside {@code v1} does not break a handler that only knows {@code v1}.
     */
    private static boolean matches(String signatures, byte[] expected) {
        boolean matched = false;
        for (String entry : signatures.split("\\s+")) {
            int comma = entry.indexOf(',');
            if (comma < 0 || !SUPPORTED_VERSION.equals(entry.substring(0, comma))) {
                continue;
            }
            byte[] candidate;
            try {
                candidate = Base64.getDecoder().decode(entry.substring(comma + 1));
            } catch (IllegalArgumentException e) {
                continue;
            }
            // Not short-circuited: returning on the first hit would leak, through timing, which entry matched.
            matched |= MessageDigest.isEqual(expected, candidate);
        }
        return matched;
    }

    private static String requireHeader(Map<String, ?> headers, String name) {
        String value = lookup(headers, name);
        if (value == null || value.trim().isEmpty()) {
            throw new WebhookVerificationException("The " + name
                    + " header is missing. Whop sends webhook-id, webhook-timestamp and webhook-signature on every"
                    + " delivery.");
        }
        return value;
    }

    private static String lookup(Map<String, ?> headers, String name) {
        for (Map.Entry<String, ?> header : headers.entrySet()) {
            if (header.getKey() == null
                    || !name.equalsIgnoreCase(header.getKey().trim())) {
                continue;
            }
            Object value = header.getValue();
            if (value instanceof Collection) {
                Iterator<?> values = ((Collection<?>) value).iterator();
                value = values.hasNext() ? values.next() : null;
            }
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }

    private static String abbreviate(String text) {
        String flat = text.replace('\n', ' ');
        return flat.length() <= 64 ? flat : flat.substring(0, 64) + "...";
    }
}
