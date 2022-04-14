package com.link_intersystems.util.function;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    public ExecutorFunctionExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void exec(Runnable r) {
        executor.execute(r);
    }
}
