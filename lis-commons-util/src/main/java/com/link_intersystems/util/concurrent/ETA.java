package com.link_intersystems.util.concurrent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ETA {

    private long maxWork = -1;
    private long tickDurationMillis;
    private long lastTickTimeMillis;

    private BigDecimal averageTimeMillisPerTickIndex = BigDecimal.ZERO;
    private BigDecimal averageTimeMillisPerTick = BigDecimal.ZERO;
    private long worked;
    private Clock clock;

    public ETA() {
        this(Clock.systemUTC());
    }

    public ETA(Clock clock) {
        this.clock = requireNonNull(clock);
    }

    public void begin(long maxWork) {
        this.worked = 0;
        this.maxWork = maxWork;
        tickDurationMillis = 0;
        averageTimeMillisPerTick = BigDecimal.ZERO;
        averageTimeMillisPerTickIndex = BigDecimal.ZERO;
        lastTickTimeMillis = clock.millis();
    }

    public void worked(int worked) {
        this.worked = Math.min(maxWork, this.worked + worked);

        if (this.worked < maxWork) {
            long now = clock.millis();
            long newTicksDurationMillis = now - lastTickTimeMillis;


            tickDurationMillis = newTicksDurationMillis;

            BigDecimal avgMillisPerTick = new BigDecimal(tickDurationMillis)
                    .divide(BigDecimal.valueOf(worked), RoundingMode.HALF_UP);

            addAvgMillisPerTick(avgMillisPerTick);

            lastTickTimeMillis = clock.millis();
        }
    }

    public Duration getRemainingDuration() {
        if (maxWork == -1 || worked == 0) {
            return null;
        }

        long remainingWork = maxWork - worked;
        BigDecimal etaMillis = averageTimeMillisPerTick.multiply(new BigDecimal(remainingWork));
        return Duration.of(etaMillis.longValue(), ChronoUnit.MILLIS);
    }

    private void addAvgMillisPerTick(BigDecimal avgMillisPerTick) {
        averageTimeMillisPerTickIndex = averageTimeMillisPerTickIndex.add(BigDecimal.ONE);
        averageTimeMillisPerTick = averageTimeMillisPerTick.add(
                avgMillisPerTick.subtract(averageTimeMillisPerTick)
                        .divide(averageTimeMillisPerTickIndex, RoundingMode.HALF_EVEN)
        );
    }
}
