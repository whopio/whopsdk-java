package com.whop.api.helpers;

/**
 * Thrown by {@link WebhookVerifier} when a delivery cannot be trusted: a
 * signature header is missing or malformed, the timestamp is outside the
 * tolerance window, or no signature matches.
 *
 * <p>Unchecked, matching {@code com.whop.api.core.WhopApiException} — the
 * generated client's own errors are unchecked, and a webhook handler is
 * normally a request handler whose framework already maps a thrown exception to
 * a status code.
 *
 * <p>A caller that must distinguish "this delivery is not authentic" from "I
 * called the helper wrong" can: the second is an {@link IllegalArgumentException}
 * and never this.
 */
public final class WebhookVerificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WebhookVerificationException(String message) {
        super(message);
    }

    public WebhookVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
