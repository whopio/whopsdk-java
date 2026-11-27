package com.whop.api.live;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import okio.Buffer;
import org.junit.jupiter.api.Test;

class ServerSentEventsTest {
    private static List<String> events(String stream) throws IOException {
        ServerSentEvents events = new ServerSentEvents(new Buffer().writeUtf8(stream));
        List<String> read = new ArrayList<>();
        for (ServerSentEvents.Event event = events.next(); event != null; event = events.next()) {
            read.add(event.name + "=" + event.data);
        }
        return read;
    }

    @Test
    void readsEventsWhateverTheLinesEndIn() throws IOException {
        assertEquals(
                Arrays.asList("a=1", "b=2", "c=3", "d=4"),
                events("event: a\ndata: 1\n\nevent: b\r\ndata: 2\r\n\r\nevent: c\rdata: 3\r\revent: d\r\ndata: 4\n\n"));
    }

    @Test
    void joinsDataLinesAndSkipsCommentsAndEventsWithoutData() throws IOException {
        assertEquals(
                Arrays.asList("message=first\nsecond", "named=x"),
                events(": keep-alive\n\ndata: first\ndata:second\n\nevent: empty\n\nevent: named\nid: 9\ndata: x\n\n"));
    }

    @Test
    void endsWithoutDispatchingAnUnfinishedEvent() throws IOException {
        ServerSentEvents events = new ServerSentEvents(new Buffer().writeUtf8("event: a\ndata: 1\n"));
        assertNull(events.next());
    }
}
