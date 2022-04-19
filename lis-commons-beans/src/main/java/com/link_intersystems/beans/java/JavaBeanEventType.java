package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanEventType;

import java.beans.EventSetDescriptor;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaBeanEventType implements BeanEventType {

    private EventSetDescriptor eventSetDescriptor;

    public JavaBeanEventType(EventSetDescriptor eventSetDescriptor) {
        this.eventSetDescriptor = eventSetDescriptor;
    }

    public EventSetDescriptor getEventSetDescriptor() {
        return eventSetDescriptor;
    }

    @Override
    public String getName() {
        return eventSetDescriptor.getName();
    }

    @Override
    public boolean isApplicable(Object listener) {
        Class<?> listenerType = eventSetDescriptor.getListenerType();
        return listenerType.isInstance(listener);
    }

    @Override
    public boolean isApplicable(Class<?> listenerClass) {
        Class<?> listenerType = eventSetDescriptor.getListenerType();
        return listenerType.isAssignableFrom(listenerClass);
    }

    @Override
    public String toString() {
        return "JavaBeanEventType{" +
                "eventSetDescriptor=" + eventSetDescriptor +
                '}';
    }
}
