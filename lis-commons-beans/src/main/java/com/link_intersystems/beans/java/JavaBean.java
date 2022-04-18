/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans.java;

import com.link_intersystems.beans.*;
import com.link_intersystems.lang.Assert;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link JavaBean} is a wrapper for any java object that fulfills the <a href=
 * "http://download.oracle.com/otndocs/jcp/7224-javabeans-1.01-fr-spec-oth-JSpec/"
 * target="_blank">java bean specification</a> to access the bean's properties
 * in a convenience way.
 *
 * @param <T> the type of the bean.
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class JavaBean<T> implements Bean<T> {

    private T bean;

    private JavaBeanClass<T> beanClass;

    private Map<String, JavaProperty<?>> properties = new HashMap<>();

    private Map<String, JavaIndexedProperty<?>> indexedProperties = new HashMap<>();

    /**
     * Constructs a new {@link JavaBean} for the given bean object.
     *
     * @param bean the bean object.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public JavaBean(T bean) {
        Assert.notNull("bean", bean);
        this.bean = bean;
        beanClass = (JavaBeanClass<T>) JavaBeanClass.get(bean.getClass());
    }

    private JavaBean(JavaBeanClass<T> beanClass) {
        Assert.notNull("beanClass", beanClass);
        this.beanClass = beanClass;
    }

    /**
     * Constructs a new {@link JavaBean} based on the beanClass. The bean class must
     * fulfill the java bean specification. The bean that this {@link JavaBean}
     * represents is lazy initialized by instantiating a bean using the default
     * constructor of the beanClass.
     *
     * @param beanClass the type of the bean that this {@link JavaBean} represents.
     * @since 1.2.0;
     */
    public static <T> JavaBean<T> strictBean(Class<T> beanClass) {
        return new JavaBean<>(JavaBeanClass.getStrict(beanClass));
    }

    /**
     * Constructs a new {@link JavaBean} based on the beanClass. The bean class must
     * fulfill the java bean specification. The bean that this {@link JavaBean}
     * represents is lazy initialized by instantiating a bean using the default
     * constructor of the beanClass.
     *
     * @param beanClass the type of the bean that this {@link JavaBean} represents.
     * @since 1.2.0;
     */
    public static <T, B extends T> JavaBean<T> niceBean(Class<T> beanClass, B beanObject) {
        JavaBean<T> bean = new JavaBean<>(JavaBeanClass.get(beanClass));
        bean.bean = beanObject;
        return bean;
    }

    public static <T> JavaBean<T> niceBean(Class<T> beanClass) {
        return new JavaBean<>(JavaBeanClass.get(beanClass));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <PT> Property<PT> getProperty(PropertyDesc<PT> propertyDesc) {
        String propertyName = propertyDesc.getName();
        if (propertyDesc.isIndexed()) {
            IndexedProperty<PT> indexedProperty = getIndexedProperty(propertyName);
            // TODO something might be wrong with the generic typing here. I have to do it later when the refactoring is done.
            return (Property<PT>) indexedProperty;
        }
        return getProperty(propertyName);
    }

    /**
     * Get the {@link JavaIndexedProperty} of this bean with the property name.
     *
     * @param propertyName the name of the indexed property.
     * @return the indexed property, if any.
     * @throws NoSuchPropertyException if the property does not exist.
     * @throws PropertyReadException if the property could not be accessed.
     * @since 1.2.0;
     */
    public <PT> IndexedProperty<PT> getIndexedProperty(String propertyName) {
        IndexedProperty<PT> indexedProperty = getIndexedPropertyInternal(propertyName);
        if (indexedProperty == null) {
            throw new PropertyReadException(bean.getClass(), propertyName);
        }
        return indexedProperty;
    }

    @SuppressWarnings("unchecked")
    private <PT> IndexedProperty<PT> getIndexedPropertyInternal(String propertyName) {
        JavaIndexedProperty<PT> indexedProperty = (JavaIndexedProperty<PT>) indexedProperties.get(propertyName);
        if (indexedProperty == null) {
            PropertyDesc<?> propertyDesc = beanClass.getProperties().getByName(propertyName);
            if (propertyDesc instanceof JavaIndexedPropertyDesc) {
                JavaIndexedPropertyDesc<PT> indexedPropertyDescriptor = (JavaIndexedPropertyDesc<PT>) propertyDesc;
                indexedProperty = new JavaIndexedProperty<>(this, indexedPropertyDescriptor);
                indexedProperties.put(propertyName, indexedProperty);
                properties.put(propertyName, indexedProperty);
            }
        }
        return indexedProperty;
    }

    private List<Property<?>> getPropertiesInternal() {
        List<Property<?>> properties = new ArrayList<>();

        List<String> propertyNames = beanClass.getProperties().getAllPropertyNames();

        for (String propertyName : propertyNames) {
            properties.add(getProperty(propertyName));
        }

        return properties;
    }

    /**
     * Retuns all properties of this bean .
     *
     * @return all properties or an empty list.
     * @since 1.2.0;
     */
    @Override
    public List<Property<Object>> getProperties() {
        List<Property<Object>> properties = new ArrayList<>();

        JavaBeanClass<T> beanClass = getBeanClass();
        List<String> propertyNames = beanClass.getProperties().getAllPropertyNames();

        for (String propertyName : propertyNames) {
            Property<Object> property = getProperty(propertyName);
            properties.add(property);
        }

        return properties;
    }

    /**
     * Get the {@link JavaProperty} of this bean with the property name.
     *
     * @param propertyName the name of the indexed property.
     * @return the property, if any.
     * @throws NoSuchPropertyException if the property does not exist.
     * @since 1.2.0;
     */
    @Override
    public <PT> Property<PT> getProperty(String propertyName) {
        JavaProperty<PT> property = getPropertyInternal(propertyName);
        if (property == null) {
            throw new NoSuchPropertyException(bean.getClass(), propertyName);
        }
        return property;
    }


    @SuppressWarnings("unchecked")
    private <PT> JavaProperty<PT> getPropertyInternal(String propertyName) {
        IndexedProperty<PT> indexedProperty = getIndexedPropertyInternal(propertyName);
        if (indexedProperty != null) {
            return (JavaProperty<PT>) indexedProperty;
        }

        JavaProperty<PT> property = (JavaProperty<PT>) properties.get(propertyName);
        if (property == null) {
            JavaPropertyDesc<PT> propertyDesc = (JavaPropertyDesc<PT>) beanClass.getProperties().getByName(propertyName);
            if (propertyDesc != null) {
                property = new JavaProperty<>(this, propertyDesc);
                properties.put(propertyName, property);
            }
        }
        return property;
    }

    private T getTarget() {
        if (bean == null) {
            try {
                JavaBean<T> newBeanInstance = beanClass.newBeanInstance();
                bean = newBeanInstance.getTarget();
            } catch (Exception e) {
                throw new IllegalStateException("Bean " + getBeanClass() + " can not be instantiated. Is it a nice Bean. See Bean.niceBean(Class<T>)", e);
            }
        }
        return bean;
    }

    @Override
    public T getObject() {
        return getTarget();
    }

    /**
     * @return the {@link JavaBeanClass} of this {@link JavaBean}.
     */
    @Override
    public JavaBeanClass<T> getBeanClass() {
        return beanClass;
    }

    @Override
    public void removeListener(Object listener) {
        if (listener == null) {
            return;
        }

        JavaBeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

        if (applicableBeanEvent == null) {
            String msg = MessageFormat.format("{0} can not handle listener {1}", getBeanClass(), listener.getClass());
            throw new UnsupportedOperationException(msg);
        }

        applicableBeanEvent.removeListener(listener);
    }

    @Override
    public void addListener(Object listener) {
        if (listener == null) {
            return;
        }

        JavaBeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

        if (applicableBeanEvent == null) {
            String msg = MessageFormat.format("{0} can not handle listener {1}", getBeanClass(), listener.getClass());
            throw new UnsupportedOperationException(msg);
        }

        applicableBeanEvent.addListener(listener);
    }

    private JavaBeanEvent getApplicableBeanEvent(Object listener) {
        JavaBeanClass<T> beanClass = getBeanClass();

        BeanEventTypes<JavaBeanEventType> beanEventTypes = beanClass.getBeanEventTypes();

        JavaBeanEvent applicableBeanEvent = null;
        for (JavaBeanEventType beanEvent : beanEventTypes) {
            if (beanEvent.isApplicable(listener)) {
                applicableBeanEvent = new JavaBeanEvent(this, beanEvent);
                break;
            }
        }
        return applicableBeanEvent;
    }

    public boolean propertiesEqual(JavaBean<T> otherBean) {
        List<Property<?>> properties = getPropertiesInternal();
        List<Property<?>> otherProperties = otherBean.getPropertiesInternal();
        return properties.equals(otherProperties);
    }


}
