package com.link_intersystems.beans.java;

import java.beans.*;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Represents a bean as a Map.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanInfoBeanMap extends AbstractMap<String, Object> {

    private final BeanInfo beanInfo;
    private final Object bean;
    private Map<String, Object> beanProperties;

    public BeanInfoBeanMap(Object bean) throws IntrospectionException {
        this(bean, Object.class);
    }

    public BeanInfoBeanMap(Object bean, Class<?> stopClass) throws IntrospectionException {
        this(getBeanInfo(bean, stopClass), bean);
    }

    /**
     * Creates a Map that contains the properties of the resolved beanInfo of the given bean
     * as keys and the respective property values of the bean as values. The bean info is resolved using
     * {@link Introspector#getBeanInfo(Class, Class)}.
     *
     * @param bean
     * @param stopClass
     * @return
     * @throws IntrospectionException
     */
    private static BeanInfo getBeanInfo(Object bean, Class<?> stopClass) throws IntrospectionException {
        Class<?> aClass = bean.getClass();
        return Introspector.getBeanInfo(aClass, stopClass);
    }

    /**
     * Creates a Map that contains the properties of the given
     * beanInfo as keys and the respective property values of the bean as values.
     *
     * Passing another Bean info then the one returned by {@link Introspector#getBeanInfo(Class)} can be used
     * to have another map perspective on the given bean. E.g. if the {@link BeanInfo} is determined by an interface
     * that the given bean implements.
     *
     * @param beanInfo the bean info to use.
     * @param bean     the bean.
     * @see InterfaceBeanInfo
     */
    public BeanInfoBeanMap(BeanInfo beanInfo, Object bean) {
        this.beanInfo = requireNonNull(beanInfo);
        this.bean = requireNonNull(bean);

        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        Class<?> beanClass = beanDescriptor.getBeanClass();

        if (!beanClass.isInstance(bean)) {
            String msg = MessageFormat.format("BeanInfo ''{0}'' is not applicable to bean ''{1}''", beanInfo, bean);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        if (beanProperties == null) {
            beanProperties = getBeanProperties();
        }
        return beanProperties.entrySet();
    }

    private Map<String, Object> getBeanProperties() {
        Map<String, Object> beanProperties = new LinkedHashMap<>();

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            try {
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null) {
                    Object value = readMethod.invoke(bean);
                    beanProperties.put(propertyDescriptor.getName(), value);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return beanProperties;
    }
}
