package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.*;
import com.link_intersystems.beans.java.JavaBeanClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

class RecordBeanInstanceFactory<T> implements BeanInstanceFactory<T> {

    private JavaBeanClass<T> beanClass;

    RecordBeanInstanceFactory(JavaBeanClass<T> beanClass) {
        this.beanClass = requireNonNull(beanClass);
    }

    @Override
    public AbstractBean<T> newBeanInstance(ArgumentResolver argumentResolver) {

        try {
            Callable<T> newInstanceCallable = resolveNewInstanceCallable(argumentResolver);
            if (newInstanceCallable == null) {
                throw new BeanInstantiationException("No constructor resolvable for " + beanClass);
            }

            try {
                T bean = newInstanceCallable.call();
                return fromExistingInstance(bean);
            } catch (Exception e) {
                throw new BeanInstantiationException("Unable to create a record bean.", e);
            }
        } catch (ArgumentResolveException e) {
            throw new BeanInstantiationException("No constructor resolvable for " + beanClass, e);
        }
    }

    protected Callable<T> resolveNewInstanceCallable(ArgumentResolver argumentResolver) throws ArgumentResolveException {
        Class<T> type = beanClass.getType();

        try {
            Constructor<T> defaultConstructor = type.getDeclaredConstructor();
            return () -> defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();

            for (Constructor<?> constructor : declaredConstructors) {
                Parameter[] parameters = constructor.getParameters();
                if (argumentResolver.canResolveArguments(parameters)) {
                    Object[] arguments = argumentResolver.resolveArguments(parameters);
                    if (arguments != null && arguments.length == parameters.length) {
                        return () -> (T) constructor.newInstance(arguments);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public AbstractBean<T> fromExistingInstance(T beanObject) {
        return new RecordBean<>(beanClass, beanObject);
    }
}
