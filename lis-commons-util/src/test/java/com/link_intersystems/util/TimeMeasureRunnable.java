package com.link_intersystems.util;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TimeMeasureRunnable implements Runnable {

    private int i;
    private long[] times;

    public TimeMeasureRunnable(int timesCount) {
        times = new long[timesCount];
    }

    @Override
    public void run() {
        times[i++] = currentTimeMillis();
    }

    public int getTimesCounted() {
        return i;
    }

    public void assertTimesBetween(long start, long end) {
        for (int j = 0; j < getTimesCounted(); j++) {
            assertTrue(times[j] >= start);
            assertTrue(times[j] <= end);
        }
    }
}
