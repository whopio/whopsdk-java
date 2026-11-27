package com.whop.api.live;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whop.api.WhopApiClient;
import com.whop.api.core.ObjectMappers;
import com.whop.api.core.RequestOptions;
import com.whop.api.core.WhopApiApiException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Each live read's {@code watch} and {@code waitFor} take exactly the read's parameters, then their own options, and
 * send exactly the request the read sends, with {@code Accept: text/event-stream}.
 *
 * <p>{@code live_reads.json} lists the live reads; the pipeline writes it from the spec. Each form of each read, and
 * its {@code watch} and {@code waitFor}, are called against one recording server with a sample for every parameter
 * the read takes; the server answers 404, and each throws the read's own error for it.
 */
class LiveParityTest {
    private static final JsonNodeFactory JSON = JsonNodeFactory.instance;

    private static final class Read {
        final String root;

        final List<String> accessors;

        final Class<?> client;

        final Class<?> live;

        final String read;

        final String watch;

        final String waitFor;

        final Class<?> answer;

        Read(JsonNode read) throws ClassNotFoundException {
            this.root = read.get("root").asText();
            this.accessors = new ArrayList<>();
            read.get("accessors").forEach(accessor -> accessors.add(accessor.asText()));
            this.client = Class.forName(read.get("client").asText());
            this.live = Class.forName(read.get("live").asText());
            this.read = read.get("read").asText();
            this.watch = read.get("watch").asText();
            this.waitFor = read.get("waitFor").asText();
            this.answer = Class.forName(read.get("answer").asText());
        }

        List<Method> forms() {
            return Arrays.stream(client.getDeclaredMethods())
                    .filter(method -> method.getName().equals(read) && Modifier.isPublic(method.getModifiers()))
                    .collect(Collectors.toList());
        }
    }

    private static List<Read> liveReads() throws IOException, ClassNotFoundException {
        List<Read> reads = new ArrayList<>();
        try (InputStream json = LiveParityTest.class.getResourceAsStream("live_reads.json")) {
            for (JsonNode read : ObjectMappers.JSON_MAPPER.readTree(json)) {
                reads.add(new Read(read));
            }
        }
        return reads;
    }

    @TestFactory
    List<DynamicTest> watchAndWaitForTakeExactlyTheReadsParametersThenTheirOptions() throws Exception {
        List<DynamicTest> tests = new ArrayList<>();
        for (Read read : liveReads()) {
            tests.add(DynamicTest.dynamicTest(read.live.getSimpleName() + "." + read.watch, () -> {
                assertTrue(read.client.isAssignableFrom(read.live));
                assertFalse(read.forms().isEmpty(), read.client.getName() + " has no " + read.read);
                String answer = read.answer.getName();
                String predicate = Predicate.class.getName() + "<? super " + answer + ">";
                Set<String> watch = new TreeSet<>();
                Set<String> waitFor = new TreeSet<>();
                for (Method form : read.forms()) {
                    assertEquals(read.answer, form.getGenericReturnType());
                    List<String> parameters = typeNames(form.getGenericParameterTypes());
                    watch.add(signature(parameters, LiveStream.class.getName() + "<" + answer + ">"));
                    watch.add(signature(
                            with(parameters, LiveOptions.class.getName()),
                            LiveStream.class.getName() + "<" + answer + ">"));
                    List<String> waits = with(parameters, predicate, predicate, Duration.class.getName());
                    waitFor.add(signature(waits, answer));
                    waitFor.add(signature(with(waits, LiveOptions.class.getName()), answer));
                }
                assertEquals(watch, declared(read.live, read.watch));
                assertEquals(waitFor, declared(read.live, read.waitFor));
            }));
        }
        return tests;
    }

    @TestFactory
    List<DynamicTest> watchAndWaitForSendExactlyTheReadsRequest() throws Exception {
        List<DynamicTest> tests = new ArrayList<>();
        for (Read read : liveReads()) {
            for (Method form : read.forms()) {
                tests.add(DynamicTest.dynamicTest(
                        read.live.getSimpleName() + "." + read.read + typeNames(form.getGenericParameterTypes()),
                        () -> assertSendsTheReadsRequest(read, form)));
            }
        }
        return tests;
    }

    private static Object liveClient(Read read, WhopApiClient generated) throws ReflectiveOperationException {
        Object client =
                Class.forName(read.root).getMethod("of", WhopApiClient.class).invoke(null, generated);
        for (String accessor : read.accessors) {
            client = client.getClass().getMethod(accessor).invoke(client);
        }
        return client;
    }

    private static void assertSendsTheReadsRequest(Read read, Method form) throws Throwable {
        try (EventServer server = new EventServer(EventServer.status(404))) {
            Object client = liveClient(read, server.client());
            assertInstanceOf(read.live, client);

            Object[] arguments = samples(form.getGenericParameterTypes());
            Class<? extends WhopApiApiException> error = assertThrows(
                            WhopApiApiException.class, () -> invoke(form, client, arguments))
                    .getClass();
            Class<?>[] types = form.getParameterTypes();
            Method watch = read.live.getMethod(read.watch, types);
            LiveStream<?> stream = (LiveStream<?>) invoke(watch, client, arguments);
            assertEquals(
                    error,
                    assertThrows(WhopApiApiException.class, stream::hasNext).getClass());
            Method waitFor =
                    read.live.getMethod(read.waitFor, with(types, Predicate.class, Predicate.class, Duration.class));
            Predicate<Object> always = answer -> true;
            assertEquals(
                    error,
                    assertThrows(
                                    WhopApiApiException.class,
                                    () -> invoke(
                                            waitFor, client, with(arguments, always, null, Duration.ofSeconds(10))))
                            .getClass());

            assertEquals(3, server.requests.size());
            EventServer.Recorded sent = server.request(0);
            for (EventServer.Recorded live : Arrays.asList(server.request(1), server.request(2))) {
                assertEquals(sent.method, live.method);
                assertEquals(sent.target, live.target);
                assertEquals(sent.body, live.body);
                assertEquals(withoutAccept(sent.headers), withoutAccept(live.headers));
                assertEquals(Arrays.asList("text/event-stream"), live.headers.get("Accept"));
            }
        }
    }

    private static Object invoke(Method method, Object target, Object[] arguments) throws Throwable {
        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException error) {
            throw error.getCause();
        }
    }

    private static Map<String, List<String>> withoutAccept(Map<String, List<String>> headers) {
        Map<String, List<String>> rest = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        rest.putAll(headers);
        rest.remove("Accept");
        return rest;
    }

    private static Object[] samples(Type[] types) {
        Object[] samples = new Object[types.length];
        for (int index = 0; index < types.length; index++) {
            samples[index] = sample(types[index], "parameter " + index);
        }
        return samples;
    }

    private static Object sample(Type type, String name) {
        if (type == RequestOptions.class) {
            return RequestOptions.builder()
                    .addHeader("X-Parity", "header 1/2")
                    .addQueryParameter("parity", "query 1/2")
                    .timeout(30)
                    .maxRetries(0)
                    .build();
        }
        JsonNode json = json(type, name, 0);
        return ObjectMappers.JSON_MAPPER.convertValue(
                json, ObjectMappers.JSON_MAPPER.getTypeFactory().constructType(type));
    }

    /** A JSON value of {@code type}, with every property of a model it reaches. */
    private static JsonNode json(Type type, String name, int depth) {
        Class<?> raw = raw(type);
        if (raw == Optional.class) {
            return json(argument(type), name, depth);
        }
        if (raw == List.class || raw == Set.class) {
            ArrayNode items = JSON.arrayNode();
            items.add(json(argument(type), name, depth));
            items.add(json(argument(type), name + " again", depth));
            return items;
        }
        if (raw == String.class) {
            return JSON.textNode(name + " 1/2");
        }
        if (raw == Integer.class || raw == int.class || raw == Long.class || raw == long.class) {
            return JSON.numberNode(7);
        }
        if (raw == Double.class || raw == double.class || raw == Float.class || raw == float.class) {
            return JSON.numberNode(1.5);
        }
        if (raw == Boolean.class || raw == boolean.class) {
            return JSON.booleanNode(true);
        }
        if (raw == OffsetDateTime.class) {
            return JSON.textNode("2026-11-21T12:30:00Z");
        }
        Optional<Object> constant = Arrays.stream(raw.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && field.getType() == raw)
                .findFirst()
                .map(field -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException error) {
                        throw new IllegalStateException(error);
                    }
                });
        if (constant.isPresent()) {
            return ObjectMappers.JSON_MAPPER.valueToTree(constant.get());
        }
        if (raw.isAnnotationPresent(JsonDeserialize.class) && depth < 4) {
            ObjectNode model = JSON.objectNode();
            for (Method getter : raw.getMethods()) {
                JsonProperty property = getter.getAnnotation(JsonProperty.class);
                if (property != null && getter.getParameterCount() == 0) {
                    model.set(property.value(), json(getter.getGenericReturnType(), property.value(), depth + 1));
                }
            }
            return model;
        }
        throw new IllegalStateException("No sample for " + type.getTypeName() + " (" + name + ")");
    }

    private static Class<?> raw(Type type) {
        return type instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) type).getRawType() : (Class<?>) type;
    }

    private static Type argument(Type type) {
        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    private static Set<String> declared(Class<?> live, String name) {
        return Arrays.stream(live.getDeclaredMethods())
                .filter(method -> method.getName().equals(name) && Modifier.isPublic(method.getModifiers()))
                .map(method -> signature(
                        typeNames(method.getGenericParameterTypes()),
                        method.getGenericReturnType().getTypeName()))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static String signature(List<String> parameters, String returned) {
        return "(" + String.join(", ", parameters) + ") -> " + returned;
    }

    private static List<String> typeNames(Type[] types) {
        return Arrays.stream(types).map(Type::getTypeName).collect(Collectors.toList());
    }

    private static List<String> with(List<String> list, String... more) {
        List<String> joined = new ArrayList<>(list);
        joined.addAll(Arrays.asList(more));
        return joined;
    }

    private static Class<?>[] with(Class<?>[] array, Class<?>... more) {
        Class<?>[] joined = Arrays.copyOf(array, array.length + more.length);
        System.arraycopy(more, 0, joined, array.length, more.length);
        return joined;
    }

    private static Object[] with(Object[] array, Object... more) {
        Object[] joined = Arrays.copyOf(array, array.length + more.length);
        System.arraycopy(more, 0, joined, array.length, more.length);
        return joined;
    }
}
