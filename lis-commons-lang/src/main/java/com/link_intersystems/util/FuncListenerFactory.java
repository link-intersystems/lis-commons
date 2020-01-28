package com.link_intersystems.util;

import java.lang.reflect.Proxy;
import java.util.EventObject;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

class FuncListenerFactory {

	public static <L, E extends EventObject> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Runnable runnable) {
		return create(eventListenerType, eventMethod, e -> runnable.run());
	}

	public static <L, E extends EventObject> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Predicate<E> eventFilter, Runnable runnable) {
		return create(eventListenerType, eventMethod, eventFilter, new Consumer<E>() {

			@Override
			public void accept(E t) {

			}
		});
	}

	public static <L, E extends EventObject> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Consumer<E> eventConsumer) {

		return create(eventListenerType, eventMethod, new BiConsumer<E, Void>() {

			@Override
			public void accept(E t, Void u) {
				eventConsumer.accept(t);
			}
		}, (Void) null);
	}

	public static <L, E extends EventObject> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Predicate<E> eventFilter, Consumer<E> eventConsumer) {

		return create(eventListenerType, eventMethod, eventFilter, new BiConsumer<E, Void>() {

			@Override
			public void accept(E t, Void u) {
				eventConsumer.accept(t);
			}
		}, (Supplier<Void>) () -> null);
	}

	public static <L, E extends EventObject, P> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			BiConsumer<E, P> eventConsumer, P param) {
		return create(eventListenerType, eventMethod, eventConsumer, new Supplier<P>() {

			@Override
			public P get() {
				return param;
			}
		});
	}

	public static <L, E extends EventObject, P> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Predicate<E> eventFilter, BiConsumer<E, P> eventConsumer, P param) {
		return create(eventListenerType, eventMethod, eventFilter, eventConsumer, new Supplier<P>() {

			@Override
			public P get() {
				return param;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static <L, E extends EventObject, P> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			BiConsumer<E, P> eventConsumer, Supplier<P> paramSupplier) {
		FuncListenerInvocationHandler<L, E, P> funcListenerInvocationHandler = new FuncListenerInvocationHandler<>(
				eventMethod, null, eventConsumer, paramSupplier);
		return (L) Proxy.newProxyInstance(eventListenerType.getClassLoader(), new Class<?>[] { eventListenerType },
				funcListenerInvocationHandler);
	}

	@SuppressWarnings("unchecked")
	public static <L, E extends EventObject, P> L create(Class<L> eventListenerType, EventMethod<L, E> eventMethod,
			Predicate<E> eventFilter, BiConsumer<E, P> eventConsumer, Supplier<P> paramSupplier) {
		FuncListenerInvocationHandler<L, E, P> funcListenerInvocationHandler = new FuncListenerInvocationHandler<>(
				eventMethod, eventFilter, eventConsumer, paramSupplier);
		return (L) Proxy.newProxyInstance(eventListenerType.getClassLoader(), new Class<?>[] { eventListenerType },
				funcListenerInvocationHandler);
	}
}
