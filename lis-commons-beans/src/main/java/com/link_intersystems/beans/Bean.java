package com.link_intersystems.beans;

import java.text.MessageFormat;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class Bean<T> {

    private BeanClass<T> beanClass;
    private T bean;

    private PropertyList properties;
    private PropertyList indexedProperties;

    protected Bean(BeanClass<T> beanClass) {
        this(beanClass, null);
    }

    protected Bean(BeanClass<T> beanClass, T bean) {
        this.beanClass = requireNonNull(beanClass);
        this.bean = requireNonNull(bean);
    }

    public BeanClass<T> getBeanClass() {
        return beanClass;
    }

    public T getObject() {
        return bean;
    }

    public abstract PropertyList getAllProperties();

    public PropertyList getProperties() {
        if (properties == null) {
            properties = new PropertyList(getAllProperties().stream().filter(p -> !(p instanceof IndexedProperty)).collect(Collectors.toList()));
        }
        return properties;
    }

    public PropertyList getIndexedProperties() {
        if (indexedProperties == null) {
            indexedProperties = new PropertyList(getAllProperties().stream().filter(p -> p instanceof IndexedProperty).collect(Collectors.toList()));
        }
        return indexedProperties;
    }

    public <L> BeanEventSupport<T, L> newBeanEventSupport() {
        BeanEventSupport<T, L> eventSupport = new BeanEventSupport<>();
        eventSupport.setBean(this);
        return eventSupport;
    }

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
        return null;
    }


}
