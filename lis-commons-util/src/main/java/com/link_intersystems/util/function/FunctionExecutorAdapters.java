package com.link_intersystems.util.function;

import java.util.function.*;

/**
 * Provides utility methods to adapt {@link Function}, {@link Consumer}, {@link BiConsumer} and {@link Runnable}s
 * to {@link FunctionExecutor#exec(Runnable)} calls.
 */
public class FunctionExecutorAdapters {

    private FunctionExecutor functionExecutor;

    public FunctionExecutorAdapters(FunctionExecutor functionExecutor) {
        this.functionExecutor = functionExecutor;
    }

    public <T, R> Function<T, R> adapter(Function<T, R> function) {

        return t -> {
            FunctionRunnable<T, R> functionRunnable = new FunctionRunnable<>(function, t);
            functionExecutor.exec(functionRunnable);
            return functionRunnable.getResult();
        };
    }

    public <T, U, R> BiFunction<T, U, R> adapter(BiFunction<T, U, R> function) {

        return (t, u) -> {
            BiFunctionRunnable<T, U, R> functionRunnable = new BiFunctionRunnable<>(function, t, u);
            functionExecutor.exec(functionRunnable);
            return functionRunnable.getResult();
        };
    }

    public <T, U> BiConsumer<T, U> adapter(BiConsumer<T, U> consumer) {

        return (t, u) -> functionExecutor.exec(consumer, t, u);
    }

    public <T, U> Consumer<T> adapter(BiConsumer<T, U> consumer, U arg) {
        return adapter(consumer, (Supplier<U>) () -> arg);

    }

    public <T, U> Consumer<T> adapter(BiConsumer<T, U> consumer, Supplier<U> argSupplier) {

        return t -> functionExecutor.exec(consumer, t, argSupplier.get());
    }

    public <T> Consumer<T> adapter(Consumer<T> consumer) {

        return t -> functionExecutor.exec(consumer, t);
    }

    public Runnable adapter(Runnable runnable) {
        return () -> functionExecutor.exec(runnable::run);
    }

}
