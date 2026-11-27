package com.whop.api.live;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class ReconnectsTest {
    private static final long SECOND = TimeUnit.SECONDS.toNanos(1);

    @Test
    void waitsUpToASecondFirstThenDoublesUpToTheMaximumPlusAQuarter() {
        long max = 30 * SECOND;
        assertEquals(0, Reconnects.waitBefore(0, max, 0));
        assertEquals(SECOND / 2, Reconnects.waitBefore(0, max, 0.5));
        assertEquals(SECOND, Reconnects.waitBefore(1, max, 0));
        assertEquals(SECOND + SECOND / 4, Reconnects.waitBefore(1, max, 1));
        assertEquals(8 * SECOND, Reconnects.waitBefore(4, max, 0));
        assertEquals(30 * SECOND, Reconnects.waitBefore(6, max, 0));
        assertEquals(30 * SECOND + 30 * SECOND / 4, Reconnects.waitBefore(6, max, 1));
        assertEquals(30 * SECOND, Reconnects.waitBefore(10_000, max, 0));
    }

    @Test
    void neverWaitsLongerThanTheMaximumBeforeTheFirstReconnect() {
        assertEquals(SECOND / 4, Reconnects.waitBefore(0, SECOND / 2, 0.5));
        assertEquals(0, Reconnects.waitBefore(3, 0, 1));
    }

    @Test
    void startsTheCountOverOnlyAfterASteadyConnection() {
        Reconnects reconnects = new Reconnects(Duration.ofSeconds(30), () -> 0);
        List<Long> waits = new ArrayList<>();
        for (boolean steady : new boolean[] {false, false, false, true, false}) {
            waits.add(reconnects.next(steady) / SECOND);
        }
        assertEquals(Arrays.asList(0L, 1L, 2L, 0L, 1L), waits);
    }
}
