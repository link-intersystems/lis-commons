package com.link_intersystems.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class AbstractInvocationHandler<T> implements InvocationHandler {

	private Class<T> proxyType;

	public AbstractInvocationHandler(Class<T> proxyType) {
		this.proxyType = proxyType;
	}

	public T createProxy() {
		return createProxy(proxyType.getClassLoader());
	}

	@SuppressWarnings("unchecked")
	public T createProxy(ClassLoader classLoader) {
		return (T) Proxy.newProxyInstance(classLoader, new Class<?>[] { proxyType }, this);
	}

}