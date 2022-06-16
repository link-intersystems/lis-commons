package com.link_intersystems.util;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

import static java.lang.System.currentTimeMillis;
import static java.text.MessageFormat.format;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class LoopTest {

    @Test
    void execute() throws InterruptedException {
        int lagDurationMs = 5;
        long avgSleepTime = measureAvgSleepTime(lagDurationMs);

        class SleepCountLoop extends Loop {

            private int sleepCount;

            @Override
            protected void sleep(long millis) throws InterruptedException {
                super.sleep(millis);
                sleepCount++;
            }
        }

        SleepCountLoop loop = new SleepCountLoop();
        int maxIterations = 5;
        loop.setMaxIterations(maxIterations);
        loop.setLagDurationMs(lagDurationMs);

        TimeMeasureRunnable timeMeasureRunnable = new TimeMeasureRunnable(maxIterations);

        long start = currentTimeMillis();
        loop.execute(timeMeasureRunnable);
        long end = currentTimeMillis();

        assertEquals(4, loop.sleepCount);

        assertEquals(5, timeMeasureRunnable.getTimesCounted());
        timeMeasureRunnable.assertTimesBetween(start, end);

        long atMostTotalDuration = (lagDurationMs + maxIterations - 1) + (avgSleepTime * maxIterations);
        assertTimePeriodAtMost(ofMillis(end - start), ofMillis(atMostTotalDuration));
    }

    private long measureAvgSleepTime(long lagDurationMs) throws InterruptedException {
        double averageMs = 0;

        for (int i = 0; i < 10; i++) {
            long start = currentTimeMillis();
            Thread.sleep(lagDurationMs);
            long end = currentTimeMillis();

            double durationMs = (end - start);
            averageMs = (averageMs + ((durationMs - averageMs) / (i + 1)));
        }

        return (long) Math.ceil(averageMs);
    }

    private void assertTimePeriodAtMost(Duration duration, Duration expectedTimePeriod) {
        int compared = duration.compareTo(expectedTimePeriod);
        assertTrue(compared < 0, () -> format("{0} should be less then {1}", duration, expectedTimePeriod));
    }


    @Test
    void infiniteLoop() {
        Runnable runnable = Mockito.mock(Runnable.class);

        Loop loop = new Loop() {
            @Override
            protected void sleep(long millis) throws InterruptedException {


                throw new InterruptedException();
            }
        };

        loop.setInfinite(true);
        int inc = loop.getInc();
        assertEquals(0, inc);

        assertThrows(InterruptedException.class, () -> loop.execute(runnable));

    }

    @Test
    void setWrongLagDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Loop().setLagDurationMs(-1));
    }

    @Test
    void setWrongMax() {
        assertThrows(IllegalArgumentException.class, () -> new Loop().setMaxIterations(-1));
    }

    @Test
    void setLagDuration() {
        Loop mainLoop = new Loop();
        mainLoop.setLagDurationMs(0);

        assertEquals(0, mainLoop.getLagDurationMs());
    }

    @Test
    void setMax() {
        Loop mainLoop = new Loop();
        mainLoop.setMaxIterations(0);

        assertEquals(0, mainLoop.getMaxIterations());
    }
}