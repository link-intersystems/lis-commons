package com.link_intersystems.lang;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Inheritance {

	public static <E, T extends E> void ifInstance(E element, Class<T> type, Consumer<T> consumer) {
		if (isInstance(element, type)) {
			consumer.accept(cast(element, type));
		}
	}

	public static <E, T extends E, U> void ifInstance(E element, Class<T> type, BiConsumer<T, U> consumer, U param) {
		Supplier<U> paramSource = () -> param;
		ifInstance(element, type, consumer, paramSource);
	}

	public static <E, T extends E, U> void ifInstance(E element, Class<T> type, BiConsumer<T, U> consumer,
			Supplier<U> paramSource) {
		if (isInstance(element, type)) {
			U u = paramSource.get();
			T casted = cast(element, type);
			consumer.accept(casted, u);
		}
	}

	public static <E, T extends E, R> R ifInstanceCall(E element, Class<T> type, Function<T, R> function) {
		return ifInstanceCall(element, type, function, null);
	}

	public static <E, T extends E, R> R ifInstanceCall(E element, Class<T> type, Function<T, R> function,
			R ifNotAdabtable) {
		if (isInstance(element, type)) {
			T casted = cast(element, type);
			return function.apply(casted);
		}
		return ifNotAdabtable;
	}

	public static <E, T extends E, U, R> R ifInstanceCall(E element, Class<T> type, BiFunction<T, U, R> function,
			U param) {
		Supplier<U> paramSource = () -> param;
		return ifInstanceCall(element, type, function, paramSource);
	}

	public static <E, T extends E, U, R> R ifInstanceCall(E element, Class<T> type, BiFunction<T, U, R> function,
			Supplier<U> paramSource) {
		if (isInstance(element, type)) {
			U param = paramSource.get();
			T casted = cast(element, type);
			return function.apply(casted, param);
		}
		return null;
	}

	private static boolean isInstance(Object element, Class<?> type) {
		return type.isInstance(element);
	}

	private static <T> T cast(Object element, Class<T> type) {
		if (element == null) {
			return null;
		}

		T tryCastOrAdapt = tryCast(element, type);
		return tryCastOrAdapt;
	}

	private static <T> T tryCast(Object element, Class<T> type) {
		if (element == null) {
			return null;
		}

		return type.cast(element);
	}

}
