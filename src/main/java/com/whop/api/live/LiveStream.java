package com.whop.api.live;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whop.api.core.ObjectMappers;
import com.whop.api.core.WhopApiApiException;
import com.whop.api.core.WhopApiException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A live read's answer now and each time it changes: iterate it, on one thread, until it ends.
 *
 * <p>{@link #close()} stops it at once from any thread: a loop over it ends without an error, and its connection
 * closes, even while it waits between events. A try-with-resources block closes it on the way out. An iterating thread
 * found interrupted closes it as well, and stays interrupted.
 *
 * <p>Each answer is the read's own answer type, decoded by the read. The stream reconnects after a drop (each
 * connection starts with a fresh answer, so nothing is replayed) and ends when the read closes for good:
 *
 * <ul>
 *   <li>{@code not_live} with no status or a 2xx one ends the iteration: the last answer stands and will not change.
 *   <li>{@code not_live} with any other status, or {@code unreadable}, throws {@link LiveReadClosedException}.
 *   <li>any other close reconnects.
 *   <li>a stream that ends without a close, or receives nothing for 45 seconds, is a drop: {@code onDisconnect} gets
 *       the error, then it reconnects.
 *   <li>a client error throws the error the read throws for it, as the server ends a read on one; a 408, a 429, a
 *       server error or a network error is a drop.
 *   <li>a server certificate the client rejects throws, as every reconnect would be rejected the same way.
 *   <li>a success that is not an event stream is the read's one answer, and the iteration ends after it.
 * </ul>
 *
 * <p>Reconnects back off as {@link LiveOptions} says: the first waits a random 0 to 1 second, each after it 1 second
 * doubling up to the maximum backoff, plus up to 25%; the count starts over after a connection that stayed open at
 * least 30 seconds.
 */
public final class LiveStream<T> implements Iterator<T>, Iterable<T>, AutoCloseable {
    static final String NOT_LIVE = "not_live";

    private static final String UNREADABLE = "unreadable";

    private static final String SNAPSHOT_EVENT = "stream.snapshot";

    private static final String CLOSED_EVENT = "stream.closed";

    private static final String EVENT_FIELD = "event";

    private static final byte[] EMPTY_ERROR = "{}".getBytes(StandardCharsets.UTF_8);

    private final GeneratedRead<T> read;

    private final Reconnects reconnects;

    private final Consumer<? super Exception> onDisconnect;

    private final Duration timeout;

    private final long deadline;

    private final CountDownLatch closed = new CountDownLatch(1);

    private volatile Call call;

    private boolean reading;

    private boolean ended;

    private boolean pending;

    private T answer;

    private boolean answeredOnce;

    private Response response;

    private ServerSentEvents events;

    private boolean opened;

    private long openedAt;

    LiveStream(GeneratedRead<T> read, LiveOptions options, Duration timeout) {
        this.read = read;
        this.reconnects = new Reconnects(options.getMaxBackoff());
        this.onDisconnect = options.getOnDisconnect();
        this.timeout = timeout;
        this.deadline = timeout == null ? 0 : System.nanoTime() + timeout.toNanos();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    /** Waits for the next answer; false once the stream has ended or been closed. */
    @Override
    public boolean hasNext() {
        synchronized (this) {
            if (pending) {
                return true;
            }
            if (ended || isClosed()) {
                end();
                return false;
            }
            reading = true;
        }
        boolean next = false;
        try {
            next = advance();
            return next;
        } catch (RuntimeException | Error error) {
            end();
            throw error;
        } finally {
            synchronized (this) {
                reading = false;
                pending = next;
                if (isClosed()) {
                    disconnect();
                }
            }
        }
    }

    @Override
    public T next() {
        synchronized (this) {
            if (pending) {
                pending = false;
                T next = answer;
                answer = null;
                return next;
            }
        }
        if (!hasNext()) {
            throw new NoSuchElementException("The live read has ended");
        }
        return next();
    }

    /** Stops the stream from any thread: iterating it ends, and its connection closes. */
    @Override
    public void close() {
        closed.countDown();
        Call current = call;
        if (current != null) {
            current.cancel();
        }
        synchronized (this) {
            if (!reading) {
                disconnect();
            }
        }
    }

    private boolean advance() {
        while (!isClosed()) {
            if (answeredOnce) {
                return end();
            }
            try {
                if (events == null && connect()) {
                    answeredOnce = true;
                    return true;
                }
                ServerSentEvents.Event event = events.next();
                if (event == null) {
                    throw new LiveStreamEndedException();
                }
                if (event.name.equals(SNAPSHOT_EVENT)) {
                    answer = snapshot(event.data);
                    return true;
                }
                if (!event.name.equals(CLOSED_EVENT)) {
                    continue;
                }
                JsonNode closedEvent = json(event);
                String reason = closedEvent.path("reason").isTextual()
                        ? closedEvent.path("reason").asText()
                        : "unknown";
                Integer status = closedEvent.path("status").isInt()
                        ? closedEvent.path("status").asInt()
                        : null;
                if (reason.equals(NOT_LIVE) && (status == null || (status >= 200 && status < 300))) {
                    return end();
                }
                if (reason.equals(NOT_LIVE) || reason.equals(UNREADABLE)) {
                    end();
                    throw new LiveReadClosedException(reason, status);
                }
                if (!backOff()) {
                    return end();
                }
            } catch (IOException | WhopApiApiException error) {
                if (isClosed()) {
                    return end();
                }
                if (Thread.currentThread().isInterrupted()) {
                    close();
                    return end();
                }
                if (error instanceof WhopApiApiException && !isDrop(((WhopApiApiException) error).statusCode())) {
                    end();
                    throw (WhopApiApiException) error;
                }
                if (isCertificateFailure(error)) {
                    end();
                    throw new WhopApiException("The server's certificate was not accepted", error);
                }
                if (pastDeadline()) {
                    end();
                    throw new WaitForTimeoutException(timeout);
                }
                onDisconnect.accept(error);
                if (!backOff()) {
                    return end();
                }
            }
        }
        return end();
    }

    /** Opens the next connection; true when the read answered once instead of streaming, its answer pending. */
    private boolean connect() throws IOException {
        Request request = read.streamRequest();
        Call next = read.transport().newCall(request);
        if (timeout != null) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                end();
                throw new WaitForTimeoutException(timeout);
            }
            next.timeout().timeout(remaining, TimeUnit.NANOSECONDS);
        }
        call = next;
        if (isClosed()) {
            next.cancel();
        }
        Response received = next.execute();
        if (!isEventStream(received)) {
            Response buffered;
            try {
                buffered = received.newBuilder()
                        .body(ResponseBody.create(
                                bodyOf(received), received.body().contentType()))
                        .build();
            } finally {
                received.close();
            }
            answer = read.answer(buffered);
            return true;
        }
        synchronized (this) {
            response = received;
        }
        events = new ServerSentEvents(received.body().source());
        opened = true;
        openedAt = System.nanoTime();
        return false;
    }

    /** Waits before the next connection; false when the stream was closed meanwhile. */
    private boolean backOff() {
        disconnect();
        boolean steady = opened && System.nanoTime() - openedAt >= Reconnects.STEADY_CONNECTION.toNanos();
        opened = false;
        long wait = reconnects.next(steady);
        if (timeout != null) {
            wait = Math.max(0, Math.min(wait, deadline - System.nanoTime()));
        }
        try {
            if (closed.await(wait, TimeUnit.NANOSECONDS)) {
                return false;
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            close();
            return false;
        }
        if (pastDeadline()) {
            end();
            throw new WaitForTimeoutException(timeout);
        }
        return true;
    }

    private T snapshot(String data) {
        JsonNode snapshot = json(new ServerSentEvents.Event(SNAPSHOT_EVENT, data));
        if (snapshot instanceof ObjectNode) {
            ((ObjectNode) snapshot).remove(EVENT_FIELD);
        }
        try {
            return read.snapshot(ObjectMappers.JSON_MAPPER.writeValueAsBytes(snapshot));
        } catch (JsonProcessingException error) {
            throw new WhopApiException("A " + SNAPSHOT_EVENT + " event could not be read", error);
        }
    }

    private static JsonNode json(ServerSentEvents.Event event) {
        try {
            return ObjectMappers.JSON_MAPPER.readTree(event.data);
        } catch (JsonProcessingException error) {
            throw new WhopApiException("The " + event.name + " event is not JSON", error);
        }
    }

    private static boolean isEventStream(Response response) {
        MediaType type = response.body().contentType();
        return response.isSuccessful()
                && type != null
                && (type.type() + "/" + type.subtype()).equals(GeneratedRead.EVENT_STREAM);
    }

    /** A certificate the client rejects: every reconnect would be rejected the same way. */
    /** True when the failure, or anything that caused it, is a certificate the client rejected, however it is wrapped. */
    static boolean isCertificateFailure(Throwable error) {
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            if (cause instanceof SSLPeerUnverifiedException || cause instanceof CertificateException) {
                return true;
            }
        }
        return false;
    }

    // A final status fails the read with its own error whether or not its body finishes: a drop partway would
    // reconnect and get the same status again. The read decodes an error body as JSON before it picks the error for
    // the status, so a body that never finished is replayed as {}.
    private static byte[] bodyOf(Response received) throws IOException {
        try {
            return received.body().bytes();
        } catch (IOException dropped) {
            if (received.isSuccessful() || isDrop(received.code())) {
                throw dropped;
            }
            return EMPTY_ERROR;
        }
    }

    private static boolean isDrop(int status) {
        return status < 400 || status >= 500 || status == 408 || status == 429;
    }

    private boolean isClosed() {
        return closed.getCount() == 0;
    }

    private boolean pastDeadline() {
        return timeout != null && System.nanoTime() - deadline >= 0;
    }

    private synchronized boolean end() {
        ended = true;
        disconnect();
        return false;
    }

    private synchronized void disconnect() {
        events = null;
        call = null;
        if (response != null) {
            response.close();
            response = null;
        }
    }
}
