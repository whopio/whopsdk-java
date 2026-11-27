package com.whop.api.live;

import static com.whop.api.live.EventServer.KEEP_ALIVE;
import static com.whop.api.live.EventServer.closed;
import static com.whop.api.live.EventServer.events;
import static com.whop.api.live.EventServer.eventsThenSilence;
import static com.whop.api.live.EventServer.eventsUntilGone;
import static com.whop.api.live.EventServer.respond;
import static com.whop.api.live.EventServer.snapshot;
import static com.whop.api.live.EventServer.status;
import static com.whop.api.live.EventServer.statusThenSilence;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.whop.api.Whop;
import com.whop.api.WhopApiClient;
import com.whop.api.core.ClientOptions;
import com.whop.api.core.Environment;
import com.whop.api.core.WhopApiApiException;
import com.whop.api.core.WhopApiException;
import com.whop.api.errors.UnauthorizedError;
import com.whop.api.resources.stats.timeseries.TimeSeriesClient;
import com.whop.api.resources.stats.timeseries.requests.RetrieveTimeSeriesRequest;
import com.whop.api.resources.stats.timeseries.types.RetrieveTimeSeriesResponse;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

class LiveStreamTest {
    private static final RetrieveTimeSeriesRequest REQUEST = RetrieveTimeSeriesRequest.builder()
            .from("2026-11-01")
            .to("2026-11-30")
            .accountId("biz_123")
            .build();

    private static final Duration PROMPTLY = Duration.ofSeconds(5);

    private final List<Exception> disconnects = Collections.synchronizedList(new ArrayList<Exception>());

    private final LiveOptions options = LiveOptions.builder()
            .maxBackoff(Duration.ZERO)
            .onDisconnect(disconnects::add)
            .build();

    private static List<Integer> timestamps(Iterable<RetrieveTimeSeriesResponse> answers) {
        List<Integer> timestamps = new ArrayList<>();
        for (RetrieveTimeSeriesResponse answer : answers) {
            timestamps.add(answer.getData().getPoints().get(0).getTimestamp());
        }
        return timestamps;
    }

    private LiveStream<RetrieveTimeSeriesResponse> watch(Whop whop) {
        return whop.stats().timeSeries().watch("gmv", REQUEST, options);
    }

    @Test
    void streamsEachSnapshotAsTheReadsAnswerUntilTheReadIsNotLive() throws IOException {
        try (EventServer server =
                new EventServer(events(snapshot(1), KEEP_ALIVE, snapshot(2), closed("not_live", 200)))) {
            try (LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop())) {
                assertEquals(Arrays.asList(1, 2), timestamps(stream));
                assertFalse(stream.hasNext());
            }
            EventServer.Recorded request = server.request(0);
            assertEquals(Collections.singletonList("text/event-stream"), request.headers.get("Accept"));
            assertEquals(Collections.singletonList("Bearer sk_test"), request.headers.get("Authorization"));
            assertEquals("/stats/time_series/gmv?account_id=biz_123&from=2026-11-01&to=2026-11-30", request.target);
            assertTrue(disconnects.isEmpty());
        }
    }

    @Test
    void endsWithAnErrorWhenTheReadIsUnreadableOrNotLiveWithAnErrorStatus() throws IOException {
        try (EventServer server =
                new EventServer(events(snapshot(1), closed("unreadable", null)), events(closed("not_live", 403)))) {
            Whop whop = server.whop();
            LiveStream<RetrieveTimeSeriesResponse> unreadable = watch(whop);
            assertEquals(1, unreadable.next().getData().getPoints().get(0).getTimestamp());
            LiveReadClosedException first = assertThrows(LiveReadClosedException.class, unreadable::hasNext);
            assertEquals("unreadable", first.reason());
            assertFalse(first.status().isPresent());
            assertFalse(unreadable.hasNext());

            LiveReadClosedException second = assertThrows(LiveReadClosedException.class, watch(whop)::hasNext);
            assertEquals("not_live", second.reason());
            assertEquals(403, second.status().get());
        }
    }

    @Test
    void reconnectsWhenTheServerAsksWithoutReportingADisconnect() throws IOException {
        try (EventServer server = new EventServer(
                events(snapshot(1), closed("reconnect", null)), events(snapshot(2), closed("not_live", null)))) {
            try (LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop())) {
                assertEquals(Arrays.asList(1, 2), timestamps(stream));
            }
            assertEquals(2, server.requests.size());
            assertTrue(disconnects.isEmpty());
        }
    }

    @Test
    void reconnectsAfterAnEndedStreamARetriedStatusAndAServerError() throws IOException {
        try (EventServer server = new EventServer(
                events(snapshot(1)),
                status(503),
                status(429),
                status(408),
                events(snapshot(2), closed("not_live", null)))) {
            try (LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop())) {
                assertEquals(Arrays.asList(1, 2), timestamps(stream));
            }
            assertEquals(4, disconnects.size());
            assertInstanceOf(LiveStreamEndedException.class, disconnects.get(0));
            assertEquals(503, ((WhopApiApiException) disconnects.get(1)).statusCode());
            assertEquals(429, ((WhopApiApiException) disconnects.get(2)).statusCode());
            assertEquals(408, ((WhopApiApiException) disconnects.get(3)).statusCode());
        }
    }

    @Test
    void throwsTheReadsErrorForAClientError() throws IOException {
        try (EventServer server = new EventServer(status(401))) {
            LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop());
            UnauthorizedError error = assertThrows(UnauthorizedError.class, stream::hasNext);
            assertEquals(401, error.statusCode());
            assertFalse(stream.hasNext());
            assertEquals(1, server.requests.size());
            assertTrue(disconnects.isEmpty());
        }
    }

    @Test
    void throwsTheReadsErrorForAClientErrorWhoseBodyGoesQuiet() throws IOException {
        try (EventServer server =
                new EventServer(statusThenSilence(401, 2_000), events(snapshot(2), closed("not_live", null)))) {
            ClientOptions clientOptions = ClientOptions.builder()
                    .environment(Environment.custom()
                            .api(server.url())
                            .vault(server.url())
                            .build())
                    .build();
            try (LiveStream<RetrieveTimeSeriesResponse> stream = Live.watch(
                    clientOptions,
                    through -> new TimeSeriesClient(through).retrieve("gmv", REQUEST),
                    options,
                    Duration.ofMillis(300),
                    null)) {
                UnauthorizedError error = assertThrows(UnauthorizedError.class, stream::hasNext);
                assertEquals(401, error.statusCode());
            }
            assertEquals(1, server.requests.size());
            assertTrue(disconnects.isEmpty());
        }
    }

    @Test
    void endsWithAnErrorWhenTheServersCertificateIsNotTrusted() throws Exception {
        Certificates certificate = Certificates.selfSigned("ip:127.0.0.1");
        try (EventServer server =
                EventServer.tls(certificate.server(), events(snapshot(1), closed("not_live", null)))) {
            LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop());
            WhopApiException error =
                    assertTimeoutPreemptively(PROMPTLY, () -> assertThrows(WhopApiException.class, stream::hasNext));
            assertInstanceOf(SSLHandshakeException.class, error.getCause());
            assertFalse(stream.hasNext());
            assertTrue(disconnects.isEmpty(), "a rejected certificate is final, not a drop");
            assertTrue(server.requests.isEmpty());
        }
    }

    @Test
    void treatsARejectedCertificateAsFinalHoweverItIsWrapped() {
        SSLHandshakeException handshake = new SSLHandshakeException("handshake failed");
        handshake.initCause(new CertificateException("untrusted"));
        assertTrue(LiveStream.isCertificateFailure(handshake));
        assertTrue(LiveStream.isCertificateFailure(new IOException("connection failed", handshake)));
        assertTrue(LiveStream.isCertificateFailure(new SSLException(new CertificateException("expired"))));
        assertTrue(
                LiveStream.isCertificateFailure(new IOException(new SSLPeerUnverifiedException("peer not verified"))));
        assertFalse(LiveStream.isCertificateFailure(new SSLHandshakeException("handshake timed out")));
        assertFalse(LiveStream.isCertificateFailure(new IOException("reset", new SocketException("reset"))));
    }

    @Test
    void endsWithAnErrorWhenTheServersCertificateNamesAnotherHost() throws Exception {
        Certificates certificate = Certificates.selfSigned("dns:other.test");
        X509TrustManager trust = certificate.trust();
        SSLContext trusting = SSLContext.getInstance("TLS");
        trusting.init(null, new TrustManager[] {trust}, null);
        try (EventServer server =
                EventServer.tls(certificate.server(), events(snapshot(1), closed("not_live", null)))) {
            Whop whop = Whop.of(WhopApiClient.builder()
                    .token("sk_test")
                    .environment(Environment.custom()
                            .api(server.url())
                            .vault(server.url())
                            .build())
                    .httpClient(new OkHttpClient.Builder()
                            .sslSocketFactory(trusting.getSocketFactory(), trust)
                            .build())
                    .build());
            LiveStream<RetrieveTimeSeriesResponse> stream = watch(whop);
            WhopApiException error =
                    assertTimeoutPreemptively(PROMPTLY, () -> assertThrows(WhopApiException.class, stream::hasNext));
            assertInstanceOf(SSLPeerUnverifiedException.class, error.getCause());
            assertTrue(disconnects.isEmpty(), "a certificate for another host is final, not a drop");
        }
    }

    @Test
    void reconnectsWhenNothingAnswersTheConnection() throws Exception {
        int port;
        try (ServerSocket unused = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            port = unused.getLocalPort();
        }
        String url = "http://127.0.0.1:" + port;
        ClientOptions clientOptions = ClientOptions.builder()
                .environment(Environment.custom().api(url).vault(url).build())
                .build();
        AtomicReference<LiveStream<RetrieveTimeSeriesResponse>> stream = new AtomicReference<>();
        LiveOptions closingOnTheSecondDrop = LiveOptions.builder()
                .maxBackoff(Duration.ZERO)
                .onDisconnect(error -> {
                    disconnects.add(error);
                    if (disconnects.size() == 2) {
                        stream.get().close();
                    }
                })
                .build();
        stream.set(Live.watch(
                clientOptions,
                through -> new TimeSeriesClient(through).retrieve("gmv", REQUEST),
                closingOnTheSecondDrop));
        assertTimeoutPreemptively(PROMPTLY, () -> assertFalse(stream.get().hasNext()));
        assertEquals(2, disconnects.size());
        assertInstanceOf(ConnectException.class, disconnects.get(0));
    }

    @Test
    void takesASuccessThatIsNotAnEventStreamAsTheReadsOneAnswer() throws IOException {
        try (EventServer server = new EventServer(respond(200, "application/json", EventServer.answer(7)))) {
            try (LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop())) {
                assertEquals(Collections.singletonList(7), timestamps(stream));
            }
            assertEquals(1, server.requests.size());
        }
    }

    @Test
    void dropsAndReconnectsAConnectionThatGoesQuiet() throws IOException {
        try (EventServer server =
                new EventServer(eventsThenSilence(2_000, snapshot(1)), events(snapshot(2), closed("not_live", null)))) {
            ClientOptions clientOptions = ClientOptions.builder()
                    .environment(Environment.custom()
                            .api(server.url())
                            .vault(server.url())
                            .build())
                    .build();
            try (LiveStream<RetrieveTimeSeriesResponse> stream = Live.watch(
                    clientOptions,
                    through -> new TimeSeriesClient(through).retrieve("gmv", REQUEST),
                    options,
                    Duration.ofMillis(300),
                    null)) {
                assertEquals(Arrays.asList(1, 2), timestamps(stream));
            }
            assertEquals(1, disconnects.size());
            assertInstanceOf(SocketTimeoutException.class, disconnects.get(0));
        }
    }

    @Test
    void closingFromAnotherThreadEndsTheLoopAndClosesTheConnection() throws Exception {
        CountDownLatch gone = new CountDownLatch(1);
        try (EventServer server = new EventServer(eventsUntilGone(gone, snapshot(1)))) {
            LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop());
            CountDownLatch first = new CountDownLatch(1);
            AtomicReference<List<Integer>> seen = new AtomicReference<>();
            Thread reader = new Thread(() -> {
                List<Integer> timestamps = new ArrayList<>();
                for (RetrieveTimeSeriesResponse answer : stream) {
                    timestamps.add(answer.getData().getPoints().get(0).getTimestamp());
                    first.countDown();
                }
                seen.set(timestamps);
            });
            reader.start();
            assertTrue(first.await(5, TimeUnit.SECONDS));
            TimeUnit.MILLISECONDS.sleep(100);
            long closing = System.nanoTime();
            stream.close();
            reader.join(PROMPTLY.toMillis());
            assertFalse(reader.isAlive(), "the loop ends once the stream is closed");
            assertTrue(System.nanoTime() - closing < TimeUnit.SECONDS.toNanos(1));
            assertEquals(Collections.singletonList(1), seen.get());
            assertTrue(gone.await(5, TimeUnit.SECONDS), "the server sees the connection closed");
            assertTrue(disconnects.isEmpty());
        }
    }

    @Test
    void closingWhileWaitingToReconnectEndsTheLoopPromptly() throws Exception {
        CountDownLatch waiting = new CountDownLatch(2);
        LiveOptions slow =
                LiveOptions.builder().onDisconnect(error -> waiting.countDown()).build();
        try (EventServer server = new EventServer(status(503))) {
            LiveStream<RetrieveTimeSeriesResponse> stream =
                    server.whop().stats().timeSeries().watch("gmv", REQUEST, slow);
            AtomicReference<Long> closedAt = new AtomicReference<>();
            Thread closer = new Thread(() -> {
                try {
                    waiting.await();
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException interrupted) {
                    return;
                }
                closedAt.set(System.nanoTime());
                stream.close();
            });
            closer.start();
            assertTimeoutPreemptively(PROMPTLY, () -> assertFalse(stream.hasNext()));
            long ended = System.nanoTime();
            assertTrue(ended - closedAt.get() < TimeUnit.MILLISECONDS.toNanos(500));
            assertEquals(2, server.requests.size(), "the second wait, at least a second, is cut short");
        }
    }

    @Test
    void anInterruptedReadingThreadEndsTheStreamAndStaysInterrupted() throws Exception {
        CountDownLatch gone = new CountDownLatch(1);
        try (EventServer server = new EventServer(eventsUntilGone(gone, snapshot(1)))) {
            LiveStream<RetrieveTimeSeriesResponse> stream = watch(server.whop());
            AtomicReference<Boolean> interrupted = new AtomicReference<>();
            Thread reader = new Thread(() -> {
                stream.next();
                Thread.currentThread().interrupt();
                stream.hasNext();
                interrupted.set(Thread.currentThread().isInterrupted());
            });
            reader.start();
            reader.join(PROMPTLY.toMillis());
            assertFalse(reader.isAlive());
            assertTrue(interrupted.get(), "the thread stays interrupted");
            assertTrue(gone.await(5, TimeUnit.SECONDS));
            assertFalse(stream.hasNext());
        }
    }

    @Test
    void waitForReturnsTheFirstAnswerItAcceptsAndClosesTheConnection() throws Exception {
        CountDownLatch gone = new CountDownLatch(1);
        try (EventServer server = new EventServer(eventsUntilGone(gone, snapshot(1), snapshot(2), snapshot(3)))) {
            RetrieveTimeSeriesResponse answer = server.whop()
                    .stats()
                    .timeSeries()
                    .waitFor(
                            "gmv",
                            REQUEST,
                            series -> series.getData().getPoints().get(0).getTimestamp() == 2,
                            null,
                            PROMPTLY);
            assertEquals(2, answer.getData().getPoints().get(0).getTimestamp());
            assertTrue(gone.await(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void waitForFailsOnTheFirstAnswerFailWhenAccepts() throws IOException {
        try (EventServer server = new EventServer(events(snapshot(1), snapshot(2), closed("not_live", null)))) {
            WaitForFailedException failed = assertThrows(WaitForFailedException.class, () -> server.whop()
                    .stats()
                    .timeSeries()
                    .waitFor(
                            "gmv",
                            REQUEST,
                            series -> true,
                            series -> series.getData().getPoints().get(0).getTimestamp() == 1,
                            PROMPTLY));
            assertEquals(
                    1,
                    ((RetrieveTimeSeriesResponse) failed.answer())
                            .getData()
                            .getPoints()
                            .get(0)
                            .getTimestamp());
        }
    }

    @Test
    void waitForTimesOutWhileTheStreamIsQuiet() throws Exception {
        CountDownLatch gone = new CountDownLatch(1);
        try (EventServer server = new EventServer(eventsUntilGone(gone, snapshot(1)))) {
            long started = System.nanoTime();
            WaitForTimeoutException timedOut = assertThrows(WaitForTimeoutException.class, () -> server.whop()
                    .stats()
                    .timeSeries()
                    .waitFor("gmv", REQUEST, series -> false, null, Duration.ofMillis(300)));
            long elapsed = System.nanoTime() - started;
            assertEquals(Duration.ofMillis(300), timedOut.timeout());
            assertTrue(elapsed >= TimeUnit.MILLISECONDS.toNanos(300) && elapsed < TimeUnit.SECONDS.toNanos(2));
            assertTrue(gone.await(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void waitForThrowsWhenTheReadStopsBeingLiveFirst() throws IOException {
        try (EventServer server = new EventServer(events(snapshot(1), closed("not_live", 200)))) {
            LiveReadClosedException closedFirst = assertThrows(
                    LiveReadClosedException.class,
                    () -> server.whop().stats().timeSeries().waitFor("gmv", REQUEST, series -> false, null, PROMPTLY));
            assertEquals("not_live", closedFirst.reason());
        }
    }
}
