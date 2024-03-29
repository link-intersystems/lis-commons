package com.link_intersystems.beans;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class BeanClass<T> {

    protected static final Predicate<? super PropertyDesc> INDEXED_PROPERTY_FILTER = jpd -> jpd instanceof IndexedPropertyDesc;
    protected static final Predicate<? super PropertyDesc> NO_INDEXED_PROPERTY_FILTER = jpd -> !INDEXED_PROPERTY_FILTER.test(jpd);

    private transient PropertyDescList properties;
    private transient PropertyDescList indexedProperties;

    public abstract String getName();

    public abstract Class<T> getType();

    public boolean isInstance(Object bean) {
        return getType().isInstance(bean);
    }

    /**
     * Creates a new {@link Bean} of this class that has
     * a new bean instance that can be retrieved by {@link Bean#getBeanObject()}.
     */
    public Bean<T> newBeanInstance() throws BeanInstantiationException {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.newBeanInstance();
    }

    public abstract BeanInstanceFactory<T> getBeanInstanceFactory();

    /**
     * Returns a {@link Bean} based on the given bean instance.
     */
    public Bean<T> getBeanFromInstance(T beanObject) {
        BeanInstanceFactory<T> beanInstanceFactory = getBeanInstanceFactory();
        return beanInstanceFactory.fromExistingInstance(beanObject);
    }

    public PropertyDescList getProperties() {
        if (this.properties == null) {
            List<PropertyDesc> propertyDescs = getAllProperties().stream()
                    .filter(NO_INDEXED_PROPERTY_FILTER)
                    .collect(toList());
            this.properties = new PropertyDescList(propertyDescs);
        }
        return properties;
    }

    public PropertyDescList getIndexedProperties() {
        if (this.indexedProperties == null) {
            List<PropertyDesc> propertyDescs = getAllProperties().stream()
                    .filter(INDEXED_PROPERTY_FILTER)
                    .collect(toList());
            this.indexedProperties = new PropertyDescList(propertyDescs);
        }
        return indexedProperties;
    }

    /**
     * @return the {@link #getProperties()} and the {@link #getIndexedProperties()}.
     */
    public abstract PropertyDescList getAllProperties();


    /**
     * @return true if either a simple property or an indexed property with the
     * given name exists.
     */
    public boolean hasAnyProperty(String propertyName) {
        return hasProperty(propertyName) || hasIndexedProperty(propertyName);
    }

    boolean hasProperty(String propertyName) {
        return getProperties().stream()
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }

    boolean hasIndexedProperty(String propertyName) {
        return getIndexedProperties().stream()
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }


    public BeanEventTypeList getBeanEventTypes() {
        return BeanEventTypeList.EMPTY;
    }

    public boolean isListenerSupported(Class<?> listenerClass) {
        return getBeanEventTypes()
                .stream()
                .anyMatch(be -> be.isApplicable(listenerClass));
    }

    public BeanClass<T> filter(PropertyDescFilter propertyDescFilter) {
        return new FilteredBeanClass<>(this, propertyDescFilter);
    }
}
