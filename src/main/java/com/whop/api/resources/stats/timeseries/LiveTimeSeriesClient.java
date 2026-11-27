package com.whop.api.resources.stats.timeseries;

import com.whop.api.core.ClientOptions;
import com.whop.api.core.RequestOptions;
import com.whop.api.live.Live;
import com.whop.api.live.LiveOptions;
import com.whop.api.live.LiveStream;
import com.whop.api.resources.stats.timeseries.requests.RetrieveTimeSeriesRequest;
import com.whop.api.resources.stats.timeseries.types.RetrieveTimeSeriesResponse;
import java.time.Duration;
import java.util.function.Predicate;

/**
 * {@link TimeSeriesClient} with its live reads: each one's answer now and every time it changes.
 */
public class LiveTimeSeriesClient extends TimeSeriesClient {
    public LiveTimeSeriesClient(ClientOptions clientOptions) {
        super(clientOptions);
    }

    /** {@code retrieve}'s answer now and each time it changes; see {@link LiveStream}. */
    public LiveStream<RetrieveTimeSeriesResponse> watch(String metric, RetrieveTimeSeriesRequest request) {
        return watch(metric, request, LiveOptions.defaults());
    }

    /** {@code retrieve}'s answer now and each time it changes; see {@link LiveStream}. */
    public LiveStream<RetrieveTimeSeriesResponse> watch(
            String metric, RetrieveTimeSeriesRequest request, LiveOptions options) {
        return Live.watch(clientOptions, through -> new TimeSeriesClient(through).retrieve(metric, request), options);
    }

    /**
     * The first of {@code retrieve}'s answers that {@code until} accepts, the current one included. {@code
     * failWhen} is checked first, and a null {@code timeout} waits indefinitely; see {@link Live#waitFor}.
     */
    public RetrieveTimeSeriesResponse waitFor(
            String metric,
            RetrieveTimeSeriesRequest request,
            Predicate<? super RetrieveTimeSeriesResponse> until,
            Predicate<? super RetrieveTimeSeriesResponse> failWhen,
            Duration timeout) {
        return waitFor(metric, request, until, failWhen, timeout, LiveOptions.defaults());
    }

    /**
     * The first of {@code retrieve}'s answers that {@code until} accepts, the current one included. {@code
     * failWhen} is checked first, and a null {@code timeout} waits indefinitely; see {@link Live#waitFor}.
     */
    public RetrieveTimeSeriesResponse waitFor(
            String metric,
            RetrieveTimeSeriesRequest request,
            Predicate<? super RetrieveTimeSeriesResponse> until,
            Predicate<? super RetrieveTimeSeriesResponse> failWhen,
            Duration timeout,
            LiveOptions options) {
        return Live.waitFor(
                clientOptions,
                through -> new TimeSeriesClient(through).retrieve(metric, request),
                until,
                failWhen,
                timeout,
                options);
    }

    /** {@code retrieve}'s answer now and each time it changes; see {@link LiveStream}. */
    public LiveStream<RetrieveTimeSeriesResponse> watch(
            String metric, RetrieveTimeSeriesRequest request, RequestOptions requestOptions) {
        return watch(metric, request, requestOptions, LiveOptions.defaults());
    }

    /** {@code retrieve}'s answer now and each time it changes; see {@link LiveStream}. */
    public LiveStream<RetrieveTimeSeriesResponse> watch(
            String metric, RetrieveTimeSeriesRequest request, RequestOptions requestOptions, LiveOptions options) {
        return Live.watch(
                clientOptions,
                through -> new TimeSeriesClient(through).retrieve(metric, request, requestOptions),
                options);
    }

    /**
     * The first of {@code retrieve}'s answers that {@code until} accepts, the current one included. {@code
     * failWhen} is checked first, and a null {@code timeout} waits indefinitely; see {@link Live#waitFor}.
     */
    public RetrieveTimeSeriesResponse waitFor(
            String metric,
            RetrieveTimeSeriesRequest request,
            RequestOptions requestOptions,
            Predicate<? super RetrieveTimeSeriesResponse> until,
            Predicate<? super RetrieveTimeSeriesResponse> failWhen,
            Duration timeout) {
        return waitFor(metric, request, requestOptions, until, failWhen, timeout, LiveOptions.defaults());
    }

    /**
     * The first of {@code retrieve}'s answers that {@code until} accepts, the current one included. {@code
     * failWhen} is checked first, and a null {@code timeout} waits indefinitely; see {@link Live#waitFor}.
     */
    public RetrieveTimeSeriesResponse waitFor(
            String metric,
            RetrieveTimeSeriesRequest request,
            RequestOptions requestOptions,
            Predicate<? super RetrieveTimeSeriesResponse> until,
            Predicate<? super RetrieveTimeSeriesResponse> failWhen,
            Duration timeout,
            LiveOptions options) {
        return Live.waitFor(
                clientOptions,
                through -> new TimeSeriesClient(through).retrieve(metric, request, requestOptions),
                until,
                failWhen,
                timeout,
                options);
    }
}
