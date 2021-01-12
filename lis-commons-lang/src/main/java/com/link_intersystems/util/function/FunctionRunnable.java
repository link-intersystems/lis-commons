package com.link_intersystems.util.function;

import java.util.function.Function;

public class FunctionRunnable<T, R> implements Runnable {

	private R result;
	private Function<T, R> function;
	private T argument;

	public FunctionRunnable(Function<T, R> function, T argument) {
		this.function = function;
		this.argument = argument;
	}

	@Override
	public void run() {
		result = function.apply(argument);
	}

	public R getResult() {
		return result;
	}

}