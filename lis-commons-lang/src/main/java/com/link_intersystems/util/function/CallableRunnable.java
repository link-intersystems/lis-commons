package com.link_intersystems.util.function;

import java.util.concurrent.Callable;


public class CallableRunnable<R> implements Runnable {

	private R result;
	private Callable<R> callable;

	public CallableRunnable(Callable<R> callable) {
		this.callable = callable;
	}

	@Override
	public void run() {
		try {
			result = callable.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public R getResult() {
		return result;
	}

}