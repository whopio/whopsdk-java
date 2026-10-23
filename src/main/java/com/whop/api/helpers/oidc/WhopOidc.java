package com.whop.api.helpers.oidc;

import com.whop.api.AsyncWhopApiClientBuilder;
import com.whop.api.WhopApiClientBuilder;
import com.whop.api.core.ClientOptions;

/**
 * Client builders that authenticate every request with an {@link OidcToken}. Configure them like the builders from
 * {@code WhopApiClient.builder()}, without calling {@code token(...)}:
 *
 * <pre>{@code
 * WhopApiClient whop = WhopOidc.clientBuilder(
 *         OidcToken.of("aurl_xxxxxxxxxxxxx", IdentityTokens.githubActions()))
 *     .build();
 * }</pre>
 */
public final class WhopOidc {
    private WhopOidc() {}

    /** A {@link WhopApiClientBuilder} whose requests carry the current access token from {@code token}. */
    public static WhopApiClientBuilder clientBuilder(OidcToken token) {
        return new WhopApiClientBuilder() {
            @Override
            protected void setAuthentication(ClientOptions.Builder builder) {
                builder.addHeader("Authorization", () -> "Bearer " + token.get());
            }
        };
    }

    /** An {@link AsyncWhopApiClientBuilder} whose requests carry the current access token from {@code token}. */
    public static AsyncWhopApiClientBuilder asyncClientBuilder(OidcToken token) {
        return new AsyncWhopApiClientBuilder() {
            @Override
            protected void setAuthentication(ClientOptions.Builder builder) {
                builder.addHeader("Authorization", () -> "Bearer " + token.get());
            }
        };
    }
}
