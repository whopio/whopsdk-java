package com.whop.api.helpers.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.whop.api.core.ObjectMappers;
import java.io.IOException;
import java.util.function.Supplier;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/** Built-in sources of identity tokens for {@link OidcToken.Builder#identityToken(Supplier)}. */
public final class IdentityTokens {
    /** The audience the built-in sources request, and the one to set on your trust. */
    public static final String DEFAULT_AUDIENCE = "https://api.whop.com";

    static final String GOOGLE_METADATA_IDENTITY_URL =
            "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/identity";

    private IdentityTokens() {}

    /** The job's identity token from GitHub Actions. The job needs {@code permissions: id-token: write}. */
    public static Supplier<String> githubActions() {
        return githubActions(DEFAULT_AUDIENCE);
    }

    /** The job's identity token from GitHub Actions, for {@code audience}. */
    public static Supplier<String> githubActions(String audience) {
        return githubActions(
                audience,
                OidcToken.defaultHttpClient(),
                System.getenv("ACTIONS_ID_TOKEN_REQUEST_URL"),
                System.getenv("ACTIONS_ID_TOKEN_REQUEST_TOKEN"));
    }

    static Supplier<String> githubActions(String audience, OkHttpClient http, String requestUrl, String requestToken) {
        return () -> {
            if (requestUrl == null || requestUrl.isEmpty() || requestToken == null || requestToken.isEmpty()) {
                throw new OidcTokenException(
                        "GitHub Actions did not provide an identity token. Give the job permissions: id-token: write.");
            }
            HttpUrl url = HttpUrl.get(requestUrl)
                    .newBuilder()
                    .setQueryParameter("audience", audience)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + requestToken)
                    .build();
            try (Response response = http.newCall(request).execute()) {
                String body = bodyText(response);
                if (!response.isSuccessful()) {
                    throw new OidcTokenException(
                            "GitHub Actions refused the identity token request with " + response.code() + ".",
                            response.code(),
                            null,
                            null);
                }
                JsonNode value = ObjectMappers.JSON_MAPPER.readTree(body).path("value");
                if (!value.isTextual()) {
                    throw new OidcTokenException(
                            "GitHub Actions returned no identity token.", response.code(), null, null);
                }
                return value.asText();
            } catch (IOException e) {
                throw new OidcTokenException("The GitHub Actions identity token request failed.", null, null, e);
            }
        };
    }

    /**
     * The identity token of the service account the workload runs as, from the Google Cloud metadata server, on
     * Cloud Run, Cloud Functions, Compute Engine, or GKE with Workload Identity.
     */
    public static Supplier<String> googleCloud() {
        return googleCloud(DEFAULT_AUDIENCE);
    }

    /** The service account's identity token from the Google Cloud metadata server, for {@code audience}. */
    public static Supplier<String> googleCloud(String audience) {
        return googleCloud(audience, OidcToken.defaultHttpClient(), GOOGLE_METADATA_IDENTITY_URL);
    }

    static Supplier<String> googleCloud(String audience, OkHttpClient http, String metadataUrl) {
        return () -> {
            HttpUrl url = HttpUrl.get(metadataUrl)
                    .newBuilder()
                    .setQueryParameter("audience", audience)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .header("Metadata-Flavor", "Google")
                    .build();
            try (Response response = http.newCall(request).execute()) {
                String body = bodyText(response);
                if (!response.isSuccessful()) {
                    throw new OidcTokenException(
                            "The Google Cloud metadata server refused the identity token request with "
                                    + response.code() + ".",
                            response.code(),
                            null,
                            null);
                }
                return body.trim();
            } catch (IOException e) {
                throw new OidcTokenException("The Google Cloud identity token request failed.", null, null, e);
            }
        };
    }

    static String bodyText(Response response) throws IOException {
        ResponseBody body = response.body();
        return body == null ? "" : body.string();
    }
}
