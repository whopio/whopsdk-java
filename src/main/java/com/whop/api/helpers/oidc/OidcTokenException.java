package com.whop.api.helpers.oidc;

/** The exchange or the identity token request failed. {@link #getCode()} is the API's error code when it sent one. */
public final class OidcTokenException extends RuntimeException {
    private final Integer status;
    private final String code;

    public OidcTokenException(String message) {
        this(message, null, null, null);
    }

    public OidcTokenException(String message, Integer status, String code, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    /** The HTTP status of the refused request, or {@code null} when no response arrived. */
    public Integer getStatus() {
        return status;
    }

    /** The API's error code, for example {@code oidc_exchange_no_match}, or {@code null}. */
    public String getCode() {
        return code;
    }
}
