package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanEvent;
import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.Invokable;
import com.link_intersystems.lang.reflect.Method2;

import java.beans.EventSetDescriptor;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class JavaBeanEvent implements BeanEvent {

    private final JavaBeanEventType javaBeanEventType;
    private Invokable removeListenerMethodInvokable;
    private Invokable addListenerInvokable;

    JavaBeanEvent(JavaBean<?> bean, JavaBeanEventType javaBeanEventType) {
        Assert.notNull("bean", bean);
        Assert.notNull("javaBeanEventType", javaBeanEventType);
        this.javaBeanEventType = javaBeanEventType;

        EventSetDescriptor eventSetDescriptor = javaBeanEventType.getEventSetDescriptor();

        Method removeListenerMethod = eventSetDescriptor.getRemoveListenerMethod();
        if (removeListenerMethod != null) {
            Method2 removeListenerMethod2 = Method2.forMethod(removeListenerMethod);
            Object target = bean.getObject();
            removeListenerMethodInvokable = removeListenerMethod2.getInvokable(target);
        }

        Method addListenerMethod = eventSetDescriptor.getAddListenerMethod();
        if (addListenerMethod != null) {
            Method2 addListenerMethod2 = Method2.forMethod(addListenerMethod);
            Object target = bean.getObject();
            addListenerInvokable = addListenerMethod2.getInvokable(target);
        }
    }

    @Override
    public JavaBeanEventType getType() {
        return javaBeanEventType;
    }

    @Override
    public void addListener(Object listener) {
        if (addListenerInvokable == null) {
            EventSetDescriptor eventSetDescriptor = javaBeanEventType.getEventSetDescriptor();
            String msg = MessageFormat.format("BeanEvent {0} has no add method for event {1}",
                    eventSetDescriptor.getName(), javaBeanEventType.getName());
            throw new IllegalArgumentException(msg);
        }

        if (!javaBeanEventType.isApplicable(listener)) {
            EventSetDescriptor eventSetDescriptor = javaBeanEventType.getEventSetDescriptor();
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

    @Override
    public void removeListener(Object listener) {
        if (removeListenerMethodInvokable == null) {
            EventSetDescriptor eventSetDescriptor = javaBeanEventType.getEventSetDescriptor();
            String msg = MessageFormat.format("BeanEvent {0} has no remove method for event {1}",
                    eventSetDescriptor.getName(), javaBeanEventType.getName());
            throw new IllegalArgumentException(msg);
        }

        if (!javaBeanEventType.isApplicable(listener)) {
            String msg = MessageFormat.format("BeanEvent {0} can not handle listener {1}", javaBeanEventType.getName(),
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
