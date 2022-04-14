package com.link_intersystems.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EventMethod<L, E extends EventObject> {

	private List<Method> eventMethods = new ArrayList<>();
	private Class<E> eventObjectClass;
	private Class<L> listenerClass;

	public EventMethod(Class<L> listenerClass, Class<E> eventObjectClass, String... methodNames) {
		this.listenerClass = listenerClass;
		this.eventObjectClass = eventObjectClass;

		for (String methodName : methodNames) {
			try {
				Method eventMethod = listenerClass.getDeclaredMethod(methodName, eventObjectClass);
				eventMethods.add(eventMethod);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public boolean accept(Method method) {
		return eventMethods.contains(method);
	}

	public E getEventObject(Object[] args) {
		if (args.length > 0) {
			Object arg1 = args[0];
			if (eventObjectClass.isInstance(arg1)) {
				return eventObjectClass.cast(arg1);
			}
		}
		return null;
	}

	public L listener(Runnable runnable) {
		return FuncListenerFactory.create(listenerClass, this, runnable);
	}

	public L listener(Runnable runnable, Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter, runnable);
	}

	public L listener(Consumer<E> consumer) {
		return FuncListenerFactory.create(listenerClass, this, consumer);
	}

	public L listener(Consumer<E> consumer, Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer);
	}

	public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton) {
		return FuncListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e)));
	}

	public <T> L listener(Consumer<T> consumer, Function<E, T> transformFunciton, Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter,
				e -> consumer.accept(transformFunciton.apply(e)));
	}

	public <P> L listener(BiConsumer<E, P> consumer, P param) {
		return FuncListenerFactory.create(listenerClass, this, consumer, param);
	}

	public <P> L listener(BiConsumer<E, P> consumer, P param, Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer, param);
	}

	public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, P param) {
		return FuncListenerFactory.create(listenerClass, this, e -> consumer.accept(transformFunciton.apply(e), param));
	}

	public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, P param,
			Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter,
				e -> consumer.accept(transformFunciton.apply(e), param));
	}

	public <P> L listener(BiConsumer<E, P> consumer, Supplier<P> paramSupplier, Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter, consumer, paramSupplier);
	}

	public <P> L listener(BiConsumer<E, P> consumer, Supplier<P> paramSupplier) {
		return FuncListenerFactory.create(listenerClass, this, consumer, paramSupplier);
	}

	public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, Supplier<P> paramSupplier) {
		return FuncListenerFactory.create(listenerClass, this,
				(E e, P p) -> consumer.accept(transformFunciton.apply(e), p), paramSupplier);
	}

	public <P, T> L listener(BiConsumer<T, P> consumer, Function<E, T> transformFunciton, Supplier<P> paramSupplier,
			Predicate<E> eventFilter) {
		return FuncListenerFactory.create(listenerClass, this, eventFilter,
				(E e, P p) -> consumer.accept(transformFunciton.apply(e), p), paramSupplier);
	}

}
