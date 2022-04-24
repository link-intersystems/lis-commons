package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanEvent;
import com.link_intersystems.beans.BeanEventType;

import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaBeanEventType implements BeanEventType {

    private EventSetDescriptor eventDescriptor;

    public JavaBeanEventType(EventSetDescriptor eventDescriptor) {
        this.eventDescriptor = eventDescriptor;
    }

    public EventSetDescriptor getEventDescriptor() {
        return eventDescriptor;
    }

    @Override
    public String getName() {
        return eventDescriptor.getName();
    }

    @Override
    public JavaBeanEvent newBeanEvent(Object bean) {
        return new JavaBeanEvent(bean, this);
    }

    @Override
    public boolean isApplicable(Object listener) {
        Class<?> listenerType = eventDescriptor.getListenerType();
        return listenerType.isInstance(listener);
    }

    @Override
    public boolean isApplicable(Class<?> listenerClass) {
        Class<?> listenerType = eventDescriptor.getListenerType();
        return listenerType.isAssignableFrom(listenerClass);
    }

    @Override
    public void addListener(Object bean, Object listener) throws UnsupportedOperationException {
        Method addListenerMethod = eventDescriptor.getAddListenerMethod();

        if (addListenerMethod == null) {
            String msg = MessageFormat.format("{0} has no add method", eventDescriptor);
            throw new UnsupportedOperationException(msg);
        }

        try {
            addListenerMethod.invoke(bean, listener);
        } catch (Exception e) {
            String msg = MessageFormat.format("Unable to add listener {0}", listener);
            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public void removeListener(Object bean, Object listener) throws UnsupportedOperationException {
        Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();

        if (removeListenerMethod == null) {
            String msg = MessageFormat.format("{0} has no remove method.", eventDescriptor);
            throw new UnsupportedOperationException(msg);
        }

        try {
            removeListenerMethod.invoke(bean, listener);
        } catch (Exception e) {
            String msg = MessageFormat.format("Unable to remove listener {0}", listener);
            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public String toString() {
        return "JavaBeanEventType{" +
                "eventSetDescriptor=" + eventDescriptor +
                '}';
    }
}
