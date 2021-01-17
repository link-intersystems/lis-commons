package com.link_intersystems.beans.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.lang.reflect.AbstractInvocationHandler;

public abstract class AbstractBeanProxyHandler<T> extends AbstractInvocationHandler<T> {

	private BeanClass<T> beanClass;

	protected AbstractBeanProxyHandler(Class<T> beanClass) {
		super(beanClass);
		this.beanClass = BeanClass.get(beanClass);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		PropertyDescriptor propertyDescriptor = beanClass.getPropertyDescriptor(method);
		if (propertyDescriptor == null) {
			method.invoke(this, args);
		} else {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (method.equals(readMethod)) {
				return handleGetMethod(method, propertyDescriptor);
			} else {
				Method writeMethod = propertyDescriptor.getWriteMethod();
				if (method.equals(writeMethod)) {
					handleSetMethod(method, args, propertyDescriptor);
				}
			}
		}
		return null;
	}

	protected List<PropertyDescriptor> getPropertyDescriptors() {
		return new ArrayList<>(beanClass.getPropertyDescriptors().values());
	}

	protected BeanClass<T> getBeanClass() {
		return beanClass;
	}

	protected abstract void handleSetMethod(Method method, Object[] args, PropertyDescriptor propertyDescriptor);

	protected abstract Object handleGetMethod(Method method, PropertyDescriptor propertyDescriptor);

}