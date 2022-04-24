package com.link_intersystems.util.function;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ExecutorFunctionExecutor implements FunctionExecutor {

    public static FunctionExecutor newSingleThreadFunctionExecutor() {
        return new ExecutorFunctionExecutor(Executors.newSingleThreadExecutor());
    }

    public static FunctionExecutor newCachedThreadPoolFunctionExecutor() {
        return new ExecutorFunctionExecutor(Executors.newCachedThreadPool());
    }

    private Executor executor;

    private long timeout = 1;
    private TimeUnit timeoutTimeUnit = TimeUnit.SECONDS;

    public ExecutorFunctionExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setTimeout(long timeout, TimeUnit timeoutTimeUnit) {
        this.timeout = timeout;
        this.timeoutTimeUnit = requireNonNull(timeoutTimeUnit);
    }

    @Override
    public void exec(Runnable r) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        executor.execute(() -> {
            r.run();
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await(timeout, timeoutTimeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    Executor getExecutor() {
        return executor;
    }
}
