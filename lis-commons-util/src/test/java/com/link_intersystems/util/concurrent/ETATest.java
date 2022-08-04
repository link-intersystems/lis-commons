package com.link_intersystems.util.concurrent;

import com.link_intersystems.test.mock.time.ClockMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ETATest {

    ClockMock clock;
    ETA eta;

    @BeforeEach
    void setUp() {
        clock = new ClockMock();
        eta = new ETA(clock);
    }

    @Test
    void durationNewInstance() {
        assertNull(eta.getRemainingDuration());
    }

    @Test
    void durationNotWorkedYet() {
        eta.begin(10);
        assertNull(eta.getRemainingDuration());
    }

    @Test
    void zeroWork() {
        returnTimesMins();

        eta.begin(0);

        assertNull(eta.getRemainingDuration());
    }


    @Test
    void linear() {
        returnTimesMins(1, 2, 3);

        eta.begin(10);

        eta.worked(1);
        eta.worked(1);
        eta.worked(1);

        assertRemainingDurationMins(7);
    }

    private void assertRemainingDurationMins(int minutes) {
        Duration remainingDuration = eta.getRemainingDuration();
        assertEquals(SECONDS.convert(minutes, MINUTES), remainingDuration.get(ChronoUnit.SECONDS));
    }

    @Test
    void overwork() {
        returnTimesMins(1, 2, 3);

        eta.begin(3);

        eta.worked(1);
        eta.worked(1);
        eta.worked(1);

        eta.worked(1);

        assertRemainingDurationMins(0);
    }


    @Test
    void exponentialSlowdown() {
        returnTimesMins(1, 2, 4, 8, 16);

        eta.begin(10);

        eta.worked(1);
        eta.worked(1);
        eta.worked(1);
        eta.worked(1);
        eta.worked(1);

        assertRemainingDurationMins(16);
    }


    void returnTimesMins(long... mins) {
        long[] clockTimes = new long[(mins.length * 2) + 1];
        int clockTimesIndex = 0;
        clockTimes[clockTimesIndex++] = 0;

        for (int i = 0; i < mins.length; i++) {
            clockTimes[clockTimesIndex++] = mins[i];
            clockTimes[clockTimesIndex++] = mins[i];
        }

        clock.returnTimesMins(clockTimes);
    }
}