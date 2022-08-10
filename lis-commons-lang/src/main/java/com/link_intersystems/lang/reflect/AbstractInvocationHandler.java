package com.link_intersystems.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractInvocationHandler<T> implements InvocationHandler {

	private Class<T> proxyType;

	public AbstractInvocationHandler(Class<T> proxyType) {
		this.proxyType = requireNonNull(proxyType);
	}

	public T createProxy() {
		return createProxy(proxyType.getClassLoader());
	}

	@SuppressWarnings("unchecked")
	public T createProxy(ClassLoader classLoader) {
		List<Class<?>> interfaces = getProxyInterfaces();
		Class<?>[] interfacesArray = interfaces.toArray(new Class<?>[interfaces.size()]);
		return (T) Proxy.newProxyInstance(classLoader, interfacesArray, this);
	}

	protected List<Class<?>> getProxyInterfaces() {
		return new ArrayList<>(Collections.singleton(proxyType));
	}

}