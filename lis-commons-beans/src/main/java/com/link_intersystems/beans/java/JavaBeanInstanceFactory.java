package com.link_intersystems.beans.java;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeanInstanceFactory;
import com.link_intersystems.beans.BeanInstantiationException;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaBeanInstanceFactory<T> implements BeanInstanceFactory<T> {

    private JavaBeanClass<T> javaBeanClass;

    JavaBeanInstanceFactory(JavaBeanClass<T> javaBeanClass) {
        this.javaBeanClass = Objects.requireNonNull(javaBeanClass);
    }

    @Override
    public JavaBean<T> newBeanInstance() {
        Class<T> beanType = javaBeanClass.getType();
        try {
            Constructor<T> defaultConstructor = beanType.getDeclaredConstructor();
            T newBeanObj = defaultConstructor.newInstance();
            return new JavaBean<>(javaBeanClass, newBeanObj);
        } catch (Exception e) {
            String msg = "Bean " + beanType.getCanonicalName() +
                    " throws an exception while invoking the default constructor." +
                    " Does it have a public default constructor?" +
                    " See BeanClass.getStrict(Class<T>)";
            throw new BeanInstantiationException(msg, e);
        }
    }

    @Override
    public JavaBean<T> fromExistingInstance(T beanObject) {
        return new JavaBean<>(javaBeanClass, beanObject);
    }
}
