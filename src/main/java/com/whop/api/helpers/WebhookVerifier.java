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
 * Verifies the Standard Webhooks signature Whop sends on every webhook delivery, then parses the body.
 *
 * <pre>{@code
 * JsonNode event = WebhookVerifier.unwrap(rawBody, request.getHeaders(), webhookSigningSecret);
 * }</pre>
 *
 * <p>The secret is a parameter, never read from the environment: this SDK reads no environment variables, and
 * whopsdk-java's e2e suite asserts that it reads none.</p>
 *
 * <p>This is the verification half of the {@code client.webhooks.unwrap} the Stainless-generated SDK shipped. Fern
 * generates from OpenAPI paths and {@code unwrap} was never a path, so the generated client has no equivalent. It is a
 * standalone class rather than a method on {@code WhopApiClient} so that nothing generated has to be patched: it
 * depends only on the JDK and Jackson, never on generated client code, so it survives the client being replaced.
 *
 * <p>It does NOT coerce the parsed body into a typed event model, which the Stainless version did through a union of
 * 42 of them. Fern generates no webhook event models — {@code CreateWebhooksRequestEventsItem} is the enum of event
 * <i>names</i> a webhook subscribes to, not a payload type — so there is nothing to coerce into. The parsed
 * {@link JsonNode} is returned as-is; {@code new ObjectMapper().treeToValue(event, YourType.class)} from there.
 *
 * <h2>Why this computes the HMAC itself</h2>
 *
 * <p>The signature Whop sends is {@code base64(HMAC-SHA256(secret, "<webhook-id>.<webhook-timestamp>.<body>"))}, and
 * the key is the <b>literal bytes of the {@code ws_…} secret</b> — {@code WebhooksManager::SignWebhook} passes the
 * stored secret straight to {@code OpenSSL::HMAC}, prefix included. Every Standard Webhooks client library instead
 * base64-<i>decodes</i> whatever key it is handed, so handing one a {@code ws_…} secret derives the wrong key and
 * every genuine delivery fails to verify. The SDKs that use such a library have to base64-<i>encode</i> the whole
 * secret to cancel that decode out.
 *
 * <p>Java needs none of that: {@link Mac} with {@code HmacSHA256} and {@link Base64} are both in the JDK, so the HMAC
 * is computed over the raw secret directly and the mismatch cannot arise. That is also why this helper adds no
 * dependency to {@code build.gradle} — which matters, because {@code build.gradle} is generated.
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
     *     has been through a JSON parse and re-serialize will not verify — read the request as bytes, not as a model.
     * @param headers the request headers. Only {@code webhook-id}, {@code webhook-timestamp} and
     *     {@code webhook-signature} are read, and the lookup is case-insensitive. Values may be a {@link String} or a
     *     {@link Collection} of them, so both a {@code Map<String, String>} and the {@code Map<String, List<String>>}
     *     that {@code java.net.http.HttpHeaders#map()} hands back are accepted.
     * @param key the endpoint's signing secret, exactly as Whop shows it — a {@code ws_}-prefixed string. Pass it
     *     verbatim; do not strip the prefix and do not pre-encode it.
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

        // Ahead of the comparison, so a replayed delivery is refused FOR THE TIMESTAMP. Back-dating the header also
        // invalidates the signature, and "no matching signature" would not say which check fired.
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
     * Verifies {@code payload} against the signature headers and returns the parsed body.
     *
     * <p>The string is signed as UTF-8, which is what Whop sends. Prefer {@link #unwrap(byte[], Map, String)} where
     * the raw bytes are available: decoding to a string and back is lossless only for well-formed UTF-8, and a body
     * that arrived as anything else would silently stop matching its signature.
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
     * The header is a space-separated list of {@code <version>,<base64>} entries. Unknown versions are ignored rather
     * than refused, so a future scheme signed alongside {@code v1} does not break a handler that only knows
     * {@code v1}. Every {@code v1} entry is compared, and every comparison is constant-time.
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

    /**
     * HTTP header names are case-insensitive, and Whop sends these three lowercase — but a framework may hand them
     * back capitalized, so the lookup cannot assume either.
     */
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
