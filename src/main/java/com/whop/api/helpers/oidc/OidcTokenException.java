package com.whop.api.helpers.oidc;

import java.time.Instant;

/** The exchange or the identity token request failed. {@link #getCode()} is the API's error code when it sent one. */
public final class OidcTokenException extends RuntimeException {
    private final Integer status;
    private final String code;
    private final Instant retryAt;

    public OidcTokenException(String message) {
        this(message, null, null, null);
    }

    public OidcTokenException(String message, Integer status, String code, Throwable cause) {
        this(message, status, code, null, cause);
    }

    OidcTokenException(String message, Integer status, String code, Instant retryAt, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.retryAt = retryAt;
    }

    /** The HTTP status of the refused request, or {@code null} when no response arrived. */
    public Integer getStatus() {
        return status;
    }

    /** The API's error code, for example {@code oidc_exchange_no_match}, or {@code null}. */
    public String getCode() {
        return code;
    }

    /**
     * When {@link OidcToken#get()} exchanges again. Until then it returns the current access token while that has more
     * than 30 seconds left, and otherwise throws this exception. Set on every exception {@code get()} throws for a
     * failed exchange; {@code null} on one thrown elsewhere, such as by {@link IdentityTokens} called directly.
     */
    public Instant getRetryAt() {
        return retryAt;
    }

    OidcTokenException withRetryAt(Instant retryAt) {
        OidcTokenException copy = new OidcTokenException(getMessage(), status, code, retryAt, getCause());
        copy.setStackTrace(getStackTrace());
        return copy;
    }
}
