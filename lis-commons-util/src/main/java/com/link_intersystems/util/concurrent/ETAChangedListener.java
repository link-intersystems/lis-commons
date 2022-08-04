package com.link_intersystems.util.concurrent;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ETAChangedListener {

    void onWorked(Supplier<Duration> durationSupplier);
}
