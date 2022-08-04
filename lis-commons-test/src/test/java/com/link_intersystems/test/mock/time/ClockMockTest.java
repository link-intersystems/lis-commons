package com.link_intersystems.test.mock.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ClockMockTest {

    private ClockMock clockMock;

    @BeforeEach
    void setUp() {
        clockMock = new ClockMock();
    }

    @Test
    void zoneId() {
        assertNotNull(clockMock.getZone());

        Clock clock = clockMock.withZone(ZoneId.of("Europe/Berlin"));
        assertNotSame(clockMock, clock);

        assertEquals(ZoneId.of("Europe/Berlin"), clock.getZone());
    }


    @Test
    void returnTimesMins() {
        clockMock.returnTimesMins(0, 1, 2);

        assertTimeMins(0);
        assertTimeMins(1);
        assertTimeMins(2);
    }


    private void assertTimeMins(long timeMins) {
        long epochMilli = clockMock.instant().toEpochMilli();
        assertEquals(MILLISECONDS.convert(timeMins, MINUTES), epochMilli);
    }

    @Test
    void returnTimeMillis() {
        clockMock.returnTimesMillis(0, 1000, 2000);

        assertTimeMillis(0);
        assertTimeMillis(1000);
        assertTimeMillis(2000);
    }

    private void assertTimeMillis(long timeMillis) {
        long epochMilli = clockMock.instant().toEpochMilli();
        assertEquals(timeMillis, epochMilli);
    }
}