package com.whop.api.live;

import com.whop.api.core.WhopApiException;
import java.util.Optional;

/**
 * The live read closed for good: {@code not_live} with the status the read now answers, or {@code unreadable}. A
 * {@code not_live} close with no status or a 2xx one is not an error: the stream ends and its last answer stands.
 */
public class LiveReadClosedException extends WhopApiException {
    private static final long serialVersionUID = 1L;

    private final String reason;

    private final Integer status;

    public LiveReadClosedException(String reason, Integer status) {
        super(
                status == null
                        ? "The live read closed: " + reason
                        : "The live read closed: " + reason + " (" + status + ")");
        this.reason = reason;
        this.status = status;
    }

    public String reason() {
        return reason;
    }

    public Optional<Integer> status() {
        return Optional.ofNullable(status);
    }
}
