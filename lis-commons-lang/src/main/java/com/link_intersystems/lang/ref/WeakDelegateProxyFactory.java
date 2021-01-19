package com.link_intersystems.lang.ref;

import java.util.function.Consumer;

public class WeakDelegateProxyFactory {

	/**
	 * @deprecated use {@link WeakDelegateProxy#createProxy()}
	 */
	@Deprecated
	public static <T> T createProxy(Class<T> interfaceClass, T delegate, Consumer<T> onDelegateRemoved) {
		WeakDelegateProxy<T> weakDelegateProxy = new WeakDelegateProxy<>(interfaceClass, delegate, onDelegateRemoved);
		return weakDelegateProxy.createProxy();
	}

}
