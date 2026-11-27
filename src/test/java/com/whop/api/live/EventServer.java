package com.whop.api.live;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import com.whop.api.Whop;
import com.whop.api.WhopApiClient;
import com.whop.api.core.Environment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

/** A local HTTP server that answers each connection with the next scripted answer and records its request. */
final class EventServer implements AutoCloseable {
    static final String KEEP_ALIVE = ": keep-alive\n\n";

    interface Answer {
        void answer(HttpExchange exchange) throws IOException;
    }

    static final class Recorded {
        final String method;

        final String target;

        final Map<String, List<String>> headers;

        final String body;

        Recorded(String method, String target, Map<String, List<String>> headers, String body) {
            this.method = method;
            this.target = target;
            this.headers = headers;
            this.body = body;
        }
    }

    private final HttpServer server;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final List<Answer> answers;

    final List<Recorded> requests = Collections.synchronizedList(new ArrayList<Recorded>());

    EventServer(Answer... answers) throws IOException {
        this(HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0), answers);
    }

    /** The same server over TLS, with the certificate {@code context} holds. */
    static EventServer tls(SSLContext context, Answer... answers) throws IOException {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(context));
        return new EventServer(server, answers);
    }

    private EventServer(HttpServer server, Answer... answers) {
        this.answers = Arrays.asList(answers);
        this.server = server;
        server.setExecutor(executor);
        server.createContext("/", this::handle);
        server.start();
    }

    String url() {
        return (server instanceof HttpsServer ? "https" : "http") + "://127.0.0.1:"
                + server.getAddress().getPort();
    }

    WhopApiClient client() {
        return WhopApiClient.builder()
                .token("sk_test")
                .environment(Environment.custom().api(url()).vault(url()).build())
                .build();
    }

    Whop whop() {
        return Whop.of(client());
    }

    Recorded request(int index) {
        return requests.get(index);
    }

    private void handle(HttpExchange exchange) throws IOException {
        Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        headers.putAll(exchange.getRequestHeaders());
        int index;
        synchronized (requests) {
            requests.add(new Recorded(
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawPath() + "?"
                            + exchange.getRequestURI().getRawQuery(),
                    headers,
                    read(exchange.getRequestBody())));
            index = requests.size() - 1;
        }
        try {
            answers.get(Math.min(index, answers.size() - 1)).answer(exchange);
        } finally {
            exchange.close();
        }
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    static String snapshot(int timestamp) {
        return "id: " + timestamp + "\nevent: stream.snapshot\ndata: {\"data\":{\"points\":[{\"timestamp\":" + timestamp
                + ",\"value\":1.5}]},\"event\":\"stream.snapshot\"}\n\n";
    }

    static String closed(String reason, Integer status) {
        return "event: stream.closed\ndata: {\"reason\":\"" + reason + "\",\"status\":" + status + "}\n\n";
    }

    static String answer(int timestamp) {
        return "{\"data\":{\"points\":[{\"timestamp\":" + timestamp + ",\"value\":1.5}]}}";
    }

    /** An event stream of {@code frames}, which then ends. */
    static Answer events(String... frames) {
        return exchange -> {
            OutputStream body = open(exchange);
            for (String frame : frames) {
                write(body, frame);
            }
        };
    }

    /** An event stream of {@code frames}, then a keep-alive every 20ms until the client goes away. */
    static Answer eventsUntilGone(CountDownLatch gone, String... frames) {
        return exchange -> {
            OutputStream body = open(exchange);
            try {
                for (String frame : frames) {
                    write(body, frame);
                }
                while (true) {
                    sleep(20);
                    write(body, KEEP_ALIVE);
                }
            } catch (IOException disconnected) {
                gone.countDown();
            }
        };
    }

    /** An event stream of {@code frames}, then nothing for {@code millis}, then its end. */
    static Answer eventsThenSilence(long millis, String... frames) {
        return exchange -> {
            OutputStream body = open(exchange);
            for (String frame : frames) {
                write(body, frame);
            }
            sleep(millis);
        };
    }

    /** A response with status {@code code} that sends the start of its body, then nothing for {@code millis}. */
    static Answer statusThenSilence(int code, long millis) {
        return exchange -> {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(code, 0);
            write(exchange.getResponseBody(), "{\"error\":");
            sleep(millis);
        };
    }

    static Answer status(int code) {
        return respond(code, "application/json", "{\"error\":{\"message\":\"HTTP " + code + "\"}}");
    }

    static Answer respond(int code, String contentType, String body) {
        return exchange -> {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(code, bytes.length);
            exchange.getResponseBody().write(bytes);
        };
    }

    private static OutputStream open(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/event-stream");
        exchange.sendResponseHeaders(200, 0);
        return exchange.getResponseBody();
    }

    private static void write(OutputStream body, String frame) throws IOException {
        body.write(frame.getBytes(StandardCharsets.UTF_8));
        body.flush();
    }

    private static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private static String read(InputStream input) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        for (int read = input.read(buffer); read >= 0; read = input.read(buffer)) {
            bytes.write(buffer, 0, read);
        }
        return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
    }
}
