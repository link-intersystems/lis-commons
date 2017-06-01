package com.link_intersystems.beans;

import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.Invokable;
import com.link_intersystems.lang.reflect.Method2;

public class BeanEvent {

	private EventSetDescriptor eventSetDescriptor;
	private Invokable removeListenerMethodInvokable;
	private Invokable addListenerInvokable;

	BeanEvent(Bean<?> bean, EventSetDescriptor eventSetDescriptor) {
		Assert.notNull("bean", bean);
		Assert.notNull("eventSetDescriptor", eventSetDescriptor);
		this.eventSetDescriptor = eventSetDescriptor;

		Method removeListenerMethod = eventSetDescriptor.getRemoveListenerMethod();
		if (removeListenerMethod != null) {
			Method2 removeListenerMethod2 = Method2.forMethod(removeListenerMethod);
			Object target = bean.getBean();
			removeListenerMethodInvokable = removeListenerMethod2.getInvokable(target);
		}

		Method addListenerMethod = eventSetDescriptor.getAddListenerMethod();
		if (addListenerMethod != null) {
			Method2 addListenerMethod2 = Method2.forMethod(addListenerMethod);
			Object target = bean.getBean();
			addListenerInvokable = addListenerMethod2.getInvokable(target);
		}
	}

	public String getName() {
		return eventSetDescriptor.getName();
	}

	boolean isApplicable(Object listener) {
		Class<?> listenerType = eventSetDescriptor.getListenerType();
		return listenerType.isInstance(listener);
	}

	public void addListener(Object listener) {
		if (addListenerInvokable == null) {
			String msg = MessageFormat.format("BeanEvent {0} has no add method for event {1}",
					eventSetDescriptor.getName(), getName());
			throw new IllegalArgumentException(msg);
		}

		if (!isApplicable(listener)) {
			String msg = MessageFormat.format("BeanEvent {0} can not handle listener {1}", eventSetDescriptor.getName(),
					listener.getClass());
			throw new IllegalArgumentException(msg);
		}

		try {
			addListenerInvokable.invoke(listener);
		} catch (Exception e) {
			String msg = MessageFormat.format("Unable to invoke {0}", addListenerInvokable);
			throw new IllegalStateException(msg, e);
		}

	}

	public void removeListener(Object listener) {
		if (removeListenerMethodInvokable == null) {
			String msg = MessageFormat.format("BeanEvent {0} has no remove method for event {1}",
					eventSetDescriptor.getName(), getName());
			throw new IllegalArgumentException(msg);
		}

		if (!isApplicable(listener)) {
			String msg = MessageFormat.format("BeanEvent {0} can not handle listener {1}", eventSetDescriptor.getName(),
					listener.getClass());
			throw new IllegalArgumentException(msg);
		}

		try {
			removeListenerMethodInvokable.invoke(listener);
		} catch (Exception e) {
			String msg = MessageFormat.format("Unable to invoke {0}", removeListenerMethodInvokable);
			throw new IllegalStateException(msg, e);
		}

	}

}
