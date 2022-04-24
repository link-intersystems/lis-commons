package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanEvent;

import static java.util.Objects.requireNonNull;

public class JavaBeanEvent implements BeanEvent {

    private final JavaBeanEventType javaBeanEventType;
    private final Object beanObject;

    JavaBeanEvent(Object beanObject, JavaBeanEventType javaBeanEventType) {
        this.beanObject = requireNonNull(beanObject);
        this.javaBeanEventType = requireNonNull(javaBeanEventType);
    }

    @Override
    public JavaBeanEventType getType() {
        return javaBeanEventType;
    }

    @Override
    public void addListener(Object listener) {
        getType().addListener(beanObject, listener);
    }

    @Override
    public void removeListener(Object listener) {
        getType().removeListener(beanObject, listener);
    }

}
