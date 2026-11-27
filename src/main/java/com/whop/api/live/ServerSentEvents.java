package com.whop.api.live;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okio.BufferedSource;
import okio.ByteString;

/** The events of an event stream, read as they arrive; a line may end in CR, LF or CRLF. */
final class ServerSentEvents {
    private static final ByteString LINE_ENDINGS = ByteString.encodeUtf8("\r\n");

    private static final byte CARRIAGE_RETURN = '\r';

    private static final byte LINE_FEED = '\n';

    private final BufferedSource source;

    private boolean skipsLineFeed;

    ServerSentEvents(BufferedSource source) {
        this.source = source;
    }

    static final class Event {
        final String name;

        final String data;

        Event(String name, String data) {
            this.name = name;
            this.data = data;
        }
    }

    /** The next event with data, or null when the stream ends. */
    Event next() throws IOException {
        String name = "";
        List<String> data = new ArrayList<>();
        while (true) {
            String line = readLine();
            if (line == null) {
                return null;
            }
            if (line.isEmpty()) {
                if (!data.isEmpty()) {
                    return new Event(name.isEmpty() ? "message" : name, String.join("\n", data));
                }
                name = "";
                continue;
            }
            if (line.startsWith(":")) {
                continue;
            }
            int colon = line.indexOf(':');
            String field = colon < 0 ? line : line.substring(0, colon);
            String value = colon < 0 ? "" : line.substring(colon + 1);
            if (value.startsWith(" ")) {
                value = value.substring(1);
            }
            if (field.equals("event")) {
                name = value;
            } else if (field.equals("data")) {
                data.add(value);
            }
        }
    }

    private String readLine() throws IOException {
        if (skipsLineFeed) {
            skipsLineFeed = false;
            if (!source.request(1)) {
                return null;
            }
            if (source.getBuffer().getByte(0) == LINE_FEED) {
                source.skip(1);
            }
        }
        long end = source.indexOfElement(LINE_ENDINGS);
        if (end < 0) {
            return null;
        }
        String line = source.readUtf8(end);
        skipsLineFeed = source.readByte() == CARRIAGE_RETURN;
        return line;
    }
}
