package com.whop.api.resources.stats;

import com.whop.api.core.ClientOptions;
import com.whop.api.core.Suppliers;
import com.whop.api.resources.stats.timeseries.LiveTimeSeriesClient;
import java.util.function.Supplier;

/**
 * {@link StatsClient} with the live reads under it.
 */
public class LiveStatsClient extends StatsClient {
    private final Supplier<LiveTimeSeriesClient> liveTimeSeriesClient;

    public LiveStatsClient(ClientOptions clientOptions) {
        super(clientOptions);
        this.liveTimeSeriesClient = Suppliers.memoize(() -> new LiveTimeSeriesClient(clientOptions));
    }

    @Override
    public LiveTimeSeriesClient timeSeries() {
        return this.liveTimeSeriesClient.get();
    }
}
