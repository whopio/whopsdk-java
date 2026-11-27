package com.whop.api.helpers;

/**
 * Thrown by {@link WebhookVerifier} when a delivery cannot be trusted: a signature header is missing or malformed,
 * the timestamp is outside the tolerance window, or no signature matches. Calling the helper wrong (a missing key,
 * body or headers) is an {@link IllegalArgumentException} instead, never this.
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
