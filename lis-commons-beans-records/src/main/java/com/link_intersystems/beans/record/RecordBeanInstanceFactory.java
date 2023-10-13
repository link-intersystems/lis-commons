package com.link_intersystems.beans.record;

import com.link_intersystems.beans.*;
import com.link_intersystems.beans.java.JavaBeanClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;

import static java.util.Objects.*;

class RecordBeanInstanceFactory<T> implements BeanInstanceFactory<T> {

    private JavaBeanClass<T> beanClass;
    private final ArgumentResolver argumentResolver;

    RecordBeanInstanceFactory(JavaBeanClass<T> beanClass, ArgumentResolver argumentResolver) {
        this.beanClass = requireNonNull(beanClass);
        this.argumentResolver = requireNonNull(argumentResolver);
    }

    @Override
    public Bean<T> newBeanInstance() {
        Callable<T> newInstanceCallable = resolveNewInstanceCallable();
        if (newInstanceCallable == null) {
            throw new BeanInstantiationException("No constructor resolvable for " + beanClass);
        }

        try {
            T bean = newInstanceCallable.call();
            return fromExistingInstance(bean);
        } catch (Exception e) {
            throw new BeanInstantiationException("Unable to create a record bean.", e);
        }
    }

    protected Callable<T> resolveNewInstanceCallable() {
        Class<T> type = beanClass.getType();

        try {
            Constructor<T> defaultConstructor = type.getDeclaredConstructor();
            return () -> defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();

            for (Constructor<?> constructor : declaredConstructors) {
                Parameter[] parameters = constructor.getParameters();
                try {
                    Object[] arguments = argumentResolver.resolveArguments(parameters);
                    return () -> (T) constructor.newInstance(arguments);
                } catch (ArgumentResolveException ex) {
                    // continue
                }
            }
        }

        return null;
    }

    @Override
    public Bean<T> fromExistingInstance(T beanObject) {
        return new RecordBean<>(beanClass, beanObject);
    }
}
