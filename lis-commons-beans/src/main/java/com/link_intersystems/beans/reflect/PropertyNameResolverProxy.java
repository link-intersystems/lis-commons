package com.link_intersystems.beans.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.link_intersystems.lang.Primitives;

/**
 *
 *
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 */
public class PropertyNameResolverProxy<T> extends AbstractBeanProxyHandler<T> {

	private PropertyDescriptor latestCallPropertyDescriptor;

	protected PropertyNameResolverProxy(Class<T> beanClass) {
		super(beanClass);
	}

	@Override
	protected void handleSetMethod(Method method, Object[] args, PropertyDescriptor propertyDescriptor) {
		this.latestCallPropertyDescriptor = propertyDescriptor;
	}

	@Override
	protected Object handleGetMethod(Method method, PropertyDescriptor propertyDescriptor) {
		latestCallPropertyDescriptor = propertyDescriptor;
		Class<?> returnType = method.getReturnType();

		if (Primitives.isPrimitive(returnType)) {
			return Primitives.getDefaultValue(returnType);
		}
		return null;
	}

	public PropertyDescriptor getLatestCallPropertyDescriptor() {
		return latestCallPropertyDescriptor;
	}

}
