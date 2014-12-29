package com.link_intersystems.beans;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.collections4.Predicate;

public abstract class BeansFacade {

	/**
	 * A Predicate that evaluates to true if the object it is evaluated against
	 * is a {@link Method} and the method is a property accessor according to
	 * the java bean specification.
	 *
	 * @return a Predicate that evaluates to true if the object it is evaluated
	 *         against is a {@link Method} and the method is a property accessor
	 *         according to the java bean specification.
	 * @since 1.0.0.0
	 */
	public static Predicate getPropertyAccessorPredicate() {
		return PropertyAccessorPredicate.INSTANCE;
	}

}

/**
 * Predicate evaluates to true if the object it is evaluated against is a
 * {@link Method} and the method is a property accessor according to the java
 * bean specification.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
class PropertyAccessorPredicate implements Predicate, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5631775222210839752L;

	public static final Predicate INSTANCE = new PropertyAccessorPredicate();

	public boolean evaluate(Object object) {
		boolean isPropertyAccessor = false;
		if (object instanceof Method) {
			Method method = Method.class.cast(object);
			Class<?> declaringClass = method.getDeclaringClass();
			BeanClass<?> beanClass = BeanClass.get(declaringClass);
			isPropertyAccessor = beanClass.isPropertyAccessor(method);
		}
		return isPropertyAccessor;
	}
}
