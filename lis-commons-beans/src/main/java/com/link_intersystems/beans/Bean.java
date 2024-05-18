package com.link_intersystems.beans;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class Bean<T> {

    private BeanClass<T> beanClass;
    private T bean;

    private PropertyList properties;
    private PropertyList indexedProperties;

    protected Bean(BeanClass<T> beanClass, T bean) {
        this.beanClass = requireNonNull(beanClass);
        this.bean = requireNonNull(bean);
    }

    public BeanClass<T> getBeanClass() {
        return beanClass;
    }

    public T getBeanObject() {
        return bean;
    }

    public abstract PropertyList getProperties();

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

    public boolean propertiesEqual(Bean<T> otherBean) {
        List<Property> properties = getSingleProperties();
        List<Property> otherProperties = otherBean.getSingleProperties();
        return properties.equals(otherProperties);
    }

    /**
     * Convenience method to copy all properties to the target bean. See {@link PropertyList#copy(PropertyList)} for details.
     *
     * @param targetBean
     */
    public void copyProperties(Bean targetBean) {
        copyProperties(targetBean, PropertySelectors.BY_NAME);
    }

    /**
     * Convenience method for {@link #getSingleProperties()}.copy(...). See {@link PropertyList#copy(PropertyList, PropertySelector)} for details.
     *
     * @param targetBean
     */
    public void copyProperties(Bean targetBean, PropertySelector propertySelector) {
        copyProperties(targetBean, propertySelector, PropertyCopyStrategies.EXACT);
    }

    /**
     * Convenience method for {@link #getSingleProperties()}.copy(...). See {@link PropertyList#copy(PropertyList, PropertySelector, PropertyCopyStrategy)} for details.
     *
     * @param targetBean
     */
    public void copyProperties(Bean targetBean, PropertySelector propertySelector, PropertyCopyStrategy propertyCopyStrategy) {
        getSingleProperties().copy(targetBean.getSingleProperties(), propertySelector, propertyCopyStrategy);
    }
}
