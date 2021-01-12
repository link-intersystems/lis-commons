package com.link_intersystems.util.function;

import java.util.function.BiFunction;

public class BiFunctionRunnable<T, U, R> implements Runnable {

	private R result;
	private BiFunction<T, U, R> function;
	private T argument1;
	private U argument2;

	public BiFunctionRunnable(BiFunction<T, U, R> function, T argument1, U argument2) {
		this.function = function;
		this.argument1 = argument1;
		this.argument2 = argument2;
	}

	@Override
	public void run() {
		result = function.apply(argument1, argument2);
	}

	public R getResult() {
		return result;
	}

}