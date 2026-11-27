package com.whop.api.live;

import com.whop.api.core.WhopApiException;
import java.time.Duration;

public class WaitForTimeoutException extends WhopApiException {
    private static final long serialVersionUID = 1L;

    private final Duration timeout;

    public WaitForTimeoutException(Duration timeout) {
        super("No answer matched within " + timeout);
        this.timeout = timeout;
    }

    public Duration timeout() {
        return timeout;
    }
}
