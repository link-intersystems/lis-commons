package com.link_intersystems.util.concurrent;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class DebouncedExecutor implements Executor {

    private static interface DelayStrategy {
        public void exec(Runnable r);
    }

    private static class TimerDelayStrategy implements DelayStrategy {

        private Executor executor;
        private long delayMs;

        private Timer timer;

        public TimerDelayStrategy(Executor executor, long delayMs) {
            this.executor = executor;
            this.delayMs = delayMs;
        }

        @Override
        public void exec(Runnable r) {
            if (timer == null) {
                timer = new Timer("Debounce");
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    executor.execute(r);
                    timer.cancel();
                    timer = null;
                }
            }, delayMs);
        }
    }

    private DelayStrategy delayStrategy;

    private Executor executor;

    public DebouncedExecutor(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
        setDelay(100, TimeUnit.MILLISECONDS);
    }

    public void setDelay(long delay, TimeUnit timeUnit) {
        long delayMs = TimeUnit.MILLISECONDS.convert(delay, timeUnit);
        if (delayMs == 0) {
            delayStrategy = executor::execute;
        } else {
            delayStrategy = new TimerDelayStrategy(executor, delayMs);
        }
    }

    @Override
    public void execute(Runnable r) {
        delayStrategy.exec(r);
    }
}
