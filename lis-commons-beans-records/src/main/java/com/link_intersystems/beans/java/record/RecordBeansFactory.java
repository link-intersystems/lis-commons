package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeansFactory;

import java.beans.IntrospectionException;

/**
 * A {@link BeansFactory} for creating Java record based {@link BeanClass}es.
 *
 * <pre>
 *     BeansFactory recordBeansFactory = BeansFactory.getInstance("record");
 * </pre>
 */
public class RecordBeansFactory extends BeansFactory {
    @Override
    public String getTypeName() {
        return "record";
    }

    /**
     * @param beanClass the Java record type.
     * @param stopClass is ignored, because records can not create an inheritance hierarchy.
     *                  See Java 17 JLS 8.10 https://docs.oracle.com/javase/specs/jls/se17/html/jls-8.html#jls-8.10
     * @param <T>
     * @return
     * @throws BeanClassException
     */
    @Override
    public <T> BeanClass<T> createBeanClass(Class<T> beanClass, Class<?> stopClass) throws BeanClassException {
        try {
            return new RecordBeanClass<>(beanClass);
        } catch (IntrospectionException e) {
            throw new BeanClassException("Unable to create record bean class", e);
        }
    }
}
