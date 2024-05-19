package com.link_intersystems.beans;

import java.text.MessageFormat;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractBean<T> implements Bean<T> {

    private BeanClass<T> beanClass;
    private T bean;

    private PropertyList properties;
    private PropertyList indexedProperties;

    protected AbstractBean(BeanClass<T> beanClass, T bean) {
        this.beanClass = requireNonNull(beanClass);
        this.bean = requireNonNull(bean);
    }

    @Override
    public BeanClass<T> getBeanClass() {
        return beanClass;
    }

    @Override
    public T getBeanObject() {
        return bean;
    }

    public PropertyList getSingleProperties() {
        if (properties == null) {
            properties = new PropertyList(getProperties().stream().filter(Predicate.not(IndexedProperty.class::isInstance)).collect(Collectors.toList()));
        }
        return properties;
    }

    public PropertyList getIndexedProperties() {
        if (indexedProperties == null) {
            indexedProperties = new PropertyList(getProperties().stream().filter(IndexedProperty.class::isInstance).collect(Collectors.toList()));
        }
        return indexedProperties;
    }

    @Override
    public void removeListener(Object listener) {
        if (listener == null) {
            return;
        }

        BeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

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

        BeanEvent applicableBeanEvent = getApplicableBeanEvent(listener);

        if (applicableBeanEvent == null) {
            String msg = MessageFormat.format("{0} can not handle listener {1}", getBeanClass(), listener.getClass());
            throw new UnsupportedOperationException(msg);
        }

        applicableBeanEvent.addListener(listener);
    }

    /**
     * Override to support {@link BeanEvent}s.
     */
    protected BeanEvent getApplicableBeanEvent(Object listener) {
        BeanClass<T> beanClass = getBeanClass();

        BeanEventTypeList beanEventTypes = beanClass.getBeanEventTypes();

        BeanEvent applicableBeanEvent = null;
        for (BeanEventType beanEventType : beanEventTypes) {
            if (beanEventType.isApplicable(listener)) {
                T beanObject = getBeanObject();
                applicableBeanEvent = beanEventType.newBeanEvent(beanObject);
                break;
            }
        }
        return applicableBeanEvent;
    }

}
