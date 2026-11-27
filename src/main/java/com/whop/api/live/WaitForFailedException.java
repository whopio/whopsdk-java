package com.whop.api.live;

import com.whop.api.core.WhopApiException;

/** An answer matched {@code failWhen}; {@link #answer()} is that answer, as the read's answer type. */
public class WaitForFailedException extends WhopApiException {
    private static final long serialVersionUID = 1L;

    private final transient Object answer;

    public WaitForFailedException(Object answer) {
        super("An answer matched failWhen");
        this.answer = answer;
    }

    public Object answer() {
        return answer;
    }
}
