package com.whop.api.live;

import com.whop.api.core.ClientOptions;
import com.whop.api.core.ConcurrencyLimitingInterceptor;
import com.whop.api.core.RetryInterceptor;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A generated read, run against a stand-in for its client's HTTP client: the first interceptor, which answers without
 * sending anything. Capturing its request and handing it a response both go through the read itself, so the request
 * is the one it builds and the answer, or the error, the one it makes of a response.
 */
final class GeneratedRead<T> {
    static final String EVENT_STREAM = "text/event-stream";

    private static final MediaType JSON = MediaType.get("application/json");

    private final Live.Read<T> read;

    private final ClientOptions standIn;

    private final OkHttpClient transport;

    private volatile Function<Request, Response> respond = request -> {
        throw new IllegalStateException("The stand-in client has no answer to give");
    };

    GeneratedRead(ClientOptions clientOptions, Live.Read<T> read, Duration idleTimeout) {
        this.read = read;
        OkHttpClient.Builder transport = clientOptions
                .httpClient()
                .newBuilder()
                .callTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(idleTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(idleTimeout.toMillis(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false);
        transport
                .interceptors()
                .removeIf(interceptor -> interceptor instanceof RetryInterceptor
                        || interceptor instanceof ConcurrencyLimitingInterceptor);
        this.transport = transport.build();
        OkHttpClient.Builder standIn = clientOptions.httpClient().newBuilder();
        standIn.interceptors().clear();
        standIn.networkInterceptors().clear();
        standIn.addInterceptor(chain -> respond.apply(chain.request()));
        this.standIn = ClientOptions.Builder.from(clientOptions)
                .httpClient(standIn.build())
                .build();
    }

    OkHttpClient transport() {
        return transport;
    }

    /** The request the read sends, asking for the event stream. */
    Request streamRequest() {
        Request captured = null;
        try {
            through(request -> {
                throw new Captured(request);
            });
        } catch (Captured capture) {
            captured = capture.request;
        }
        if (captured == null) {
            throw new IllegalStateException("A live read must call one generated read and return its answer");
        }
        return captured.newBuilder().header("Accept", EVENT_STREAM).build();
    }

    /** What the read makes of {@code response}: its answer, or the error it throws for it. */
    T answer(Response response) {
        return through(request -> response.newBuilder().request(request).build());
    }

    /** {@code json}, one snapshot of the read's answer, as the read's answer type. */
    T snapshot(byte[] json) {
        return through(request -> new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(json, JSON))
                .build());
    }

    private T through(Function<Request, Response> answer) {
        respond = answer;
        return read.read(standIn);
    }

    private static final class Captured extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private final transient Request request;

        Captured(Request request) {
            super(null, null, false, false);
            this.request = request;
        }
    }
}
