package com.link_intersystems.util;

/**
 * A configurable implementation of a loop that can be used in main methods in order to
 * execute a specific task for a number of times. E.g. like the command line util ping does.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Loop {

    private static interface LoopImplementor {

        public boolean hasNext();

        default public void next() {
        }

    }

    private static class BoundedLoopImplementor implements LoopImplementor {

        private int i;
        private int max;

        public BoundedLoopImplementor(int max) {
            this.max = max;
        }

        @Override
        public boolean hasNext() {
            return i < max;
        }

        @Override
        public void next() {
            i++;
        }
    }

    private int maxIterations;
    private int inc;
    private long lagDurationMs;

    public Loop() {
        setMaxIterations(1);
        setLagDurationMs(1000);
    }

    public void execute(Runnable runnable) throws InterruptedException {
        LoopImplementor loopImplementor = getLoopImplementor();

        while (loopImplementor.hasNext()) {
            runnable.run();
            loopImplementor.next();

            if (loopImplementor.hasNext()) {
                sleep(lagDurationMs);
            }
        }
    }

    protected LoopImplementor getLoopImplementor() {
        if(inc == 0){
            return () -> true;
        } else {
            return new BoundedLoopImplementor(maxIterations);
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
