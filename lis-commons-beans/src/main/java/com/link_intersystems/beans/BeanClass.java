package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface BeanClass<T> {

    String getName();

    Class<T> getType();

    Bean<T> newBeanInstance() throws BeanInstantiationException;

    default T newInstance() throws BeanInstantiationException {
        return newBeanInstance().getObject();
    }

    Bean<T> getBean(T bean);

    PropertyDescList getProperties();

    PropertyDescList getIndexedProperties();

    PropertyDescList getAllProperties();


    /**
     * @return true if either a simple property or an indexed property with the
     * given name exists.
     */
    default public boolean hasAnyProperty(String propertyName) {
        return hasProperty(propertyName) || hasIndexedProperty(propertyName);
    }

    default boolean hasProperty(String propertyName) {
        return getProperties().stream()
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }

    default boolean hasIndexedProperty(String propertyName) {
        return getIndexedProperties().stream()
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }


    BeanEventTypes getBeanEventTypes();

    default boolean isListenerSupported(Class<?> listenerClass) {
        return getBeanEventTypes()
                .stream()
                .anyMatch(be -> be.isApplicable(listenerClass));
    }

}
