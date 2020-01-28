package com.link_intersystems.lang.ref;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;

public class WeakDelegateProxyFactory {

	private static class WeakDelegateProxy<T> implements InvocationHandler {

		private WeakReference<T> delegateReference;
		private Consumer<T> onDelegateRemoved;

		public WeakDelegateProxy(T delegate, Consumer<T> onDelegateRemoved) {
			this.onDelegateRemoved = onDelegateRemoved;
			delegateReference = new WeakReference<T>(delegate);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			T t = delegateReference.get();

			if (t == null) {
				onDelegateRemoved.accept((T) proxy);
				return null;
			} else {
				return method.invoke(t, args);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> interfaceClass, T delegate, Consumer<T> onDelegateRemoved) {
		WeakDelegateProxy<T> weakDelegateProxy = new WeakDelegateProxy<>(delegate, onDelegateRemoved);
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				weakDelegateProxy);
	}

}
