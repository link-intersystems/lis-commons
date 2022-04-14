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

        return new Function<T, R>() {

            @Override
            public R apply(T t) {
                FunctionRunnable<T, R> functionRunnable = new FunctionRunnable<>(function, t);
                functionExecutor.exec(functionRunnable);
                return functionRunnable.getResult();
            }
        };
    }

    public <T, U, R> BiFunction<T, U, R> adapter(BiFunction<T, U, R> function) {

        return new BiFunction<T, U, R>() {

            @Override
            public R apply(T t, U u) {
                BiFunctionRunnable<T, U, R> functionRunnable = new BiFunctionRunnable<>(function, t, u);
                functionExecutor.exec(functionRunnable);
                return functionRunnable.getResult();
            }
        };
    }

    public <T, U> BiConsumer<T, U> adapter(BiConsumer<T, U> consumer) {

        return new BiConsumer<T, U>() {

            @Override
            public void accept(T t, U u) {
                functionExecutor.exec(consumer, t, u);
            }
        };
    }

    public <T, U> Consumer<T> adapter(BiConsumer<T, U> consumer, U arg) {
        return adapter(consumer, new Supplier<U>() {

            @Override
            public U get() {
                return arg;
            }
        });

    }

    public <T, U> Consumer<T> adapter(BiConsumer<T, U> consumer, Supplier<U> argSupplier) {

        return new Consumer<T>() {

            @Override
            public void accept(T t) {
                functionExecutor.exec(consumer, t, argSupplier.get());
            }
        };
    }

    public <T> Consumer<T> adapter(Consumer<T> consumer) {

        return new Consumer<T>() {

            @Override
            public void accept(T t) {
                functionExecutor.exec(consumer, t);
            }
        };
    }

    public Runnable adapter(Runnable runnable) {
        return new Runnable() {

            @Override
            public void run() {
                functionExecutor.exec(runnable::run);
            }
        };
    }

}
