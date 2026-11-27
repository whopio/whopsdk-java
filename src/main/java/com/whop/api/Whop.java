package com.whop.api;

import com.whop.api.core.ClientOptions;
import com.whop.api.core.Suppliers;
import com.whop.api.resources.stats.LiveStatsClient;
import java.util.function.Supplier;

/**
 * A {@link WhopApiClient} whose resource clients also watch their live reads: build the client,
 * then {@code Whop.of(client)}.
 */
public class Whop extends WhopApiClient {
    private final Supplier<LiveStatsClient> liveStatsClient;

    public Whop(ClientOptions clientOptions) {
        super(clientOptions);
        this.liveStatsClient = Suppliers.memoize(() -> new LiveStatsClient(clientOptions));
    }

    public static Whop of(WhopApiClient client) {
        return new Whop(client.clientOptions);
    }

    @Override
    public LiveStatsClient stats() {
        return this.liveStatsClient.get();
    }
}
