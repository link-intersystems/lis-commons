package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanEvent;
import com.link_intersystems.lang.reflect.Invokable;
import com.link_intersystems.lang.reflect.Method2;

import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import static java.util.Objects.requireNonNull;

public class JavaBeanEvent implements BeanEvent {

    private final JavaBeanEventType javaBeanEventType;
    private Invokable removeListenerMethodInvokable;
    private Invokable addListenerInvokable;

    JavaBeanEvent(Object beanObject, JavaBeanEventType javaBeanEventType) {
        requireNonNull(beanObject);
        this.javaBeanEventType = requireNonNull(javaBeanEventType);

        EventSetDescriptor eventSetDescriptor = javaBeanEventType.getEventSetDescriptor();

        Method removeListenerMethod = eventSetDescriptor.getRemoveListenerMethod();
        if (removeListenerMethod != null) {
            Method2 removeListenerMethod2 = Method2.forMethod(removeListenerMethod);
            removeListenerMethodInvokable = removeListenerMethod2.getInvokable(beanObject);
        }

        Method addListenerMethod = eventSetDescriptor.getAddListenerMethod();
        if (addListenerMethod != null) {
            Method2 addListenerMethod2 = Method2.forMethod(addListenerMethod);
            addListenerInvokable = addListenerMethod2.getInvokable(beanObject);
        }
    }

    @Override
    public JavaBeanEventType getType() {
        return javaBeanEventType;
    }

    @Override
    public void addListener(Object listener) {
        if (addListenerInvokable == null) {
            String msg = MessageFormat.format("{0} has no add method", javaBeanEventType);
            throw new UnsupportedOperationException(msg);
        }

        try {
            addListenerInvokable.invoke(listener);
        } catch (Exception e) {
            String msg = MessageFormat.format("Unable to add listener {0}", listener);
            throw new IllegalArgumentException(msg, e);
        }

    }

    @Override
    public void removeListener(Object listener) {
        if (removeListenerMethodInvokable == null) {
            String msg = MessageFormat.format("{0} has no remove method.", javaBeanEventType);
            throw new UnsupportedOperationException(msg);
        }

        try {
            removeListenerMethodInvokable.invoke(listener);
        } catch (Exception e) {
            String msg = MessageFormat.format("Unable to remove listener {0}", listener);
            throw new IllegalArgumentException(msg, e);
        }

    }

}
