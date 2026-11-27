package com.whop.api.live;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleSupplier;

/**
 * The wait before each reconnect. Attempt 0 waits a random 0 to 1 second (at most the maximum backoff); attempt n
 * waits 2^(n-1) seconds, at most the maximum backoff, plus up to 25% more. The count starts over only after a
 * connection that stayed open at least 30 seconds, not after an answer, since every connection opens with one.
 */
final class Reconnects {
    static final Duration STEADY_CONNECTION = Duration.ofSeconds(30);

    private static final long SECOND = TimeUnit.SECONDS.toNanos(1);

    private static final double JITTER = 0.25;

    private static final int MAX_DOUBLINGS = 30;

    private final long maxBackoff;

    private final DoubleSupplier random;

    private int attempt;

    Reconnects(Duration maxBackoff) {
        this(maxBackoff, () -> ThreadLocalRandom.current().nextDouble());
    }

    Reconnects(Duration maxBackoff, DoubleSupplier random) {
        this.maxBackoff = maxBackoff.toNanos();
        this.random = random;
    }

    /** The wait, in nanoseconds, before the next connection; {@code steady} when the dropped one stayed open. */
    long next(boolean steady) {
        if (steady) {
            attempt = 0;
        }
        long wait = waitBefore(attempt, maxBackoff, random.getAsDouble());
        attempt++;
        return wait;
    }

    static long waitBefore(int attempt, long maxBackoff, double fraction) {
        if (attempt <= 0) {
            return (long) (Math.min(maxBackoff, SECOND) * fraction);
        }
        long base = Math.min(maxBackoff, SECOND << Math.min(attempt - 1, MAX_DOUBLINGS));
        return base + (long) (base * JITTER * fraction);
    }
}
