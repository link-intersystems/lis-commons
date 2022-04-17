package com.link_intersystems.beans;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public abstract class BeansFacade {

	/**
	 * A Predicate that evaluates to true if the object it is evaluated against
	 * is a {@link Method} and the method is a property accessor according to
	 * the java bean specification.
	 *
	 * @return a Predicate that evaluates to true if the object it is evaluated
	 *         against is a {@link Method} and the method is a property accessor
	 *         according to the java bean specification.
	 * @since 1.0.0;
	 */
	public static Predicate<Method> getPropertyAccessorPredicate() {
		return PropertyAccessorPredicate.INSTANCE;
	}

}

