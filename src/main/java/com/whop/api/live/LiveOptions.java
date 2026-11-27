package com.whop.api.live;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

/** How a live read reconnects: the longest wait between reconnects, and what observes each drop. */
public final class LiveOptions {
    public static final Duration DEFAULT_MAX_BACKOFF = Duration.ofSeconds(30);

    private static final LiveOptions DEFAULTS = builder().build();

    private final Duration maxBackoff;

    private final Consumer<? super Exception> onDisconnect;

    private LiveOptions(Duration maxBackoff, Consumer<? super Exception> onDisconnect) {
        this.maxBackoff = maxBackoff;
        this.onDisconnect = onDisconnect;
    }

    public static LiveOptions defaults() {
        return DEFAULTS;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Duration getMaxBackoff() {
        return maxBackoff;
    }

    /**
     * Called on the iterating thread with each drop before the reconnect: a network error, an idle or ended stream
     * ({@link LiveStreamEndedException}), or the read's error for a 408, a 429 or a server error.
     */
    public Consumer<? super Exception> getOnDisconnect() {
        return onDisconnect;
    }

    public static final class Builder {
        private Duration maxBackoff = DEFAULT_MAX_BACKOFF;

        private Consumer<? super Exception> onDisconnect = error -> {};

        private Builder() {}

        public Builder maxBackoff(Duration maxBackoff) {
            Objects.requireNonNull(maxBackoff, "maxBackoff");
            if (maxBackoff.isNegative()) {
                throw new IllegalArgumentException("maxBackoff must not be negative");
            }
            this.maxBackoff = maxBackoff;
            return this;
        }

        public Builder onDisconnect(Consumer<? super Exception> onDisconnect) {
            this.onDisconnect = Objects.requireNonNull(onDisconnect, "onDisconnect");
            return this;
        }

        public LiveOptions build() {
            return new LiveOptions(maxBackoff, onDisconnect);
        }
    }
}
