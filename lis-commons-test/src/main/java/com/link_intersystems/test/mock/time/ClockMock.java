package com.link_intersystems.test.mock.time;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ClockMock extends Clock {

    private Clock clock = Mockito.mock(Clock.class);
    private ZoneId zoneId = ZoneId.systemDefault();

    public ClockMock() {
    }

    private ClockMock(ZoneId zone) {
        this.zoneId = zone;
    }

    public Clock getMock() {
        return clock;
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new ClockMock(zone);
    }

    @Override
    public Instant instant() {
        return Instant.ofEpochMilli(clock.millis());
    }

    public void returnTimesMins(long... minutes) {
        returnTimesMillis(Arrays.stream(minutes).map(this::minsToMillis).toArray());
    }

    public void returnTimesMillis(long... timeMillis) {
        Mockito.reset(clock);
        if (timeMillis.length > 0) {
            long timeMilli = timeMillis[0];
            OngoingStubbing<Long> ongoingStubbing = Mockito.when(clock.millis()).thenReturn(timeMilli);

            for (int i = 1; i < timeMillis.length; i++) {
                ongoingStubbing.thenReturn(timeMillis[i]);
            }
        }
    }

    private long minsToMillis(long mins) {
        return MILLISECONDS.convert(mins, MINUTES);
    }
}
