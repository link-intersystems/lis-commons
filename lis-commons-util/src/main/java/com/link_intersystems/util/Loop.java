package com.link_intersystems.util;

/**
 * A configurable implementation of a loop that can be used in main methods in order to
 * execute a specific task for a number of times. E.g. like the command line util ping does.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Loop {

    private int maxIterations;
    private int inc;
    private long lagDurationMs;

    public Loop() {
        setMaxIterations(1);
        setLagDurationMs(1000);
    }

    public void execute(Runnable runnable) throws InterruptedException {
        for (int i = 1; i <= maxIterations; i += inc) {
            runnable.run();

            if (i < maxIterations) {
                sleep(lagDurationMs);
            }
        }
    }

    protected int getInc() {
        return inc;
    }

    protected void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public void setMaxIterations(int maxIterations) {
        if (maxIterations < 0) {
            throw new IllegalArgumentException("max must be 0 or greater");
        }
        this.maxIterations = maxIterations;
        this.inc = 1;
    }

    public void setInfinite(boolean infinite) {
        this.inc = infinite ? 0 : 1;
        this.maxIterations = Integer.MAX_VALUE;
    }

    public void setLagDurationMs(long lagDurationMs) {
        if (lagDurationMs < 0) {
            throw new IllegalArgumentException("lagDurationMs must be a positive integer.");
        }
        this.lagDurationMs = lagDurationMs;
    }

    public long getLagDurationMs() {
        return lagDurationMs;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}
