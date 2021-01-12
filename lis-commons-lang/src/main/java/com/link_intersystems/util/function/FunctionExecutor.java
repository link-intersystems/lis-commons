package com.link_intersystems.util.function;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface FunctionExecutor {

	public void exec(Runnable r);

	public default <T, R> R exec(Function<T, R> function, Supplier<T> paramSupplier) {
		return execAdapter(function).apply(paramSupplier.get());
	}

	public default <T, R> Function<T, R> execAdapter(Function<T, R> function) {

		return new Function<T, R>() {

			@Override
			public R apply(T t) {
				FunctionRunnable<T, R> functionRunnable = new FunctionRunnable<>(function, t);
				exec(functionRunnable);
				return functionRunnable.getResult();
			}
		};
	}

	public default <T, U, R> R exec(BiFunction<T, U, R> function, T arg1, U arg2) {
		BiFunctionRunnable<T, U, R> functionRunnable = new BiFunctionRunnable<>(function, arg1, arg2);
		exec(functionRunnable);
		return functionRunnable.getResult();
	}

	public default <T, U> void exec(BiConsumer<T, U> consumer, T arg1, U arg2) {
		exec(() -> consumer.accept(arg1, arg2));
	}

	public default <T, U> void exec(Consumer<T> consumer, T arg) {
		exec(() -> consumer.accept(arg));
	}

	public default <V> V exec(Callable<V> callable) {
		CallableRunnable<V> callableRunnable = new CallableRunnable<V>(callable);
		exec(callableRunnable);
		return callableRunnable.getResult();
	}

}
