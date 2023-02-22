package com.link_intersystems.util.function;

import java.util.concurrent.Executor;

import static java.util.Objects.*;

public class FunctionExecutorAdapter implements FunctionExecutor {

    private Executor executor;

    public FunctionExecutorAdapter(Executor executor) {
        this.executor = requireNonNull(executor);
    }

    @Override
    public void exec(Runnable command) {
        executor.execute(command);
    }
}
