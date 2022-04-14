package com.link_intersystems.util.function;

import java.util.function.Function;

/**
 * Adapts a function call to a {@link Runnable}.
 *
 * @param <A>
 * @param <R>
 */
public class FunctionRunnable<A, R> implements Runnable {

    private R result;
    private Function<A, R> function;
    private A argument;

    /**
     * Constructs a {@link FunctionRunnable} that will call the given function with the provided argument on {@link Runnable#run()}.
     *
     * @param function the function to be called.
     * @param argument the argument to pass.
     */
    public FunctionRunnable(Function<A, R> function, A argument) {
        this.function = function;
        this.argument = argument;
    }

    @Override
    public void run() {
        result = function.apply(argument);
    }

    /**
     * @return the latest result of the called function.
     */
    public R getResult() {
        return result;
    }

}