package com.whop.api.live;

import java.io.IOException;

/** The event stream ended without a {@code stream.closed} event: a drop, after which the stream reconnects. */
public class LiveStreamEndedException extends IOException {
    private static final long serialVersionUID = 1L;

    public LiveStreamEndedException() {
        super("The event stream ended without a stream.closed event");
    }
}
