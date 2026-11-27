package com.whop.api.live;

import com.whop.api.core.ClientOptions;
import com.whop.api.core.WhopApiException;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Live reads: a read's answer now, then again each time it changes.
 *
 * <p>A live read is a GET the API also answers as an event stream: the same request with {@code Accept:
 * text/event-stream} gets the read's whole answer now and again each time it changes. Each live read's resource
 * client on {@link com.whop.api.Whop} carries {@code watch} and {@code waitFor} beside the read, taking exactly the
 * read's parameters; they call the methods here with the read itself.
 *
 * <p>Each connection sends the request the read builds: the read runs against a stand-in for the client's HTTP client
 * that records its request instead of sending it, and that request, asking for the event stream, is what is sent.
 * Its path, query, headers and authentication are therefore the read's, and so are its {@code RequestOptions}'
 * headers and query parameters. Each connection is sent once, without the client's retries (OkHttp's own included)
 * or its concurrent request limit, since the stream reconnects itself and would hold a request slot for as long as
 * it is open. A connection that takes 45 seconds to connect, or then receives no bytes for 45 seconds, is dropped and
 * reconnected (the server sends a keep-alive every 15 seconds), so the read's timeout does not apply.
 */
public final class Live {
    static final Duration IDLE_TIMEOUT = Duration.ofSeconds(45);

    private Live() {}

    /** A generated read, called on a resource client built from {@code through}. */
    @FunctionalInterface
    public interface Read<T> {
        T read(ClientOptions through);
    }

    /** {@code read}'s answer now and each time it changes, until the stream is closed or closes for good. */
    public static <T> LiveStream<T> watch(ClientOptions clientOptions, Read<T> read, LiveOptions options) {
        return watch(clientOptions, read, options, IDLE_TIMEOUT, null);
    }

    /**
     * The first of {@code read}'s answers that {@code until} accepts, the current one included.
     *
     * <p>Throws {@link WaitForFailedException} on the first answer {@code failWhen} accepts (checked first; {@code
     * null} never fails), {@link WaitForTimeoutException} once {@code timeout} passes ({@code null} waits
     * indefinitely), and {@link LiveReadClosedException} when the read stops being live first. Its connection closes
     * as it returns.
     */
    public static <T> T waitFor(
            ClientOptions clientOptions,
            Read<T> read,
            Predicate<? super T> until,
            Predicate<? super T> failWhen,
            Duration timeout,
            LiveOptions options) {
        return waitFor(clientOptions, read, until, failWhen, timeout, options, IDLE_TIMEOUT);
    }

    static <T> LiveStream<T> watch(
            ClientOptions clientOptions, Read<T> read, LiveOptions options, Duration idleTimeout, Duration timeout) {
        Objects.requireNonNull(options, "options");
        return new LiveStream<>(new GeneratedRead<>(clientOptions, read, idleTimeout), options, timeout);
    }

    static <T> T waitFor(
            ClientOptions clientOptions,
            Read<T> read,
            Predicate<? super T> until,
            Predicate<? super T> failWhen,
            Duration timeout,
            LiveOptions options,
            Duration idleTimeout) {
        Objects.requireNonNull(until, "until");
        try (LiveStream<T> answers = watch(clientOptions, read, options, idleTimeout, timeout)) {
            while (answers.hasNext()) {
                T answer = answers.next();
                if (failWhen != null && failWhen.test(answer)) {
                    throw new WaitForFailedException(answer);
                }
                if (until.test(answer)) {
                    return answer;
                }
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            throw new WhopApiException("The thread waiting for a live read was interrupted");
        }
        throw new LiveReadClosedException(LiveStream.NOT_LIVE, null);
    }
}
