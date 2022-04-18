package com.link_intersystems.beans;

import java.util.Collections;
import java.util.stream.Stream;

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

    PropertyDescs<? extends PropertyDesc> getProperties();


    /**
     * @return true if either a simple property or an indexed property with the
     * given name exists.
     */
    default public boolean hasAnyProperty(String propertyName) {
        return hasProperty(propertyName) || hasIndexedProperty(propertyName);
    }

    default boolean hasProperty(String propertyName) {
        Stream<? extends PropertyDesc> stream = getProperties().stream();
        return stream.filter(p -> !p.isIndexed())
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }

    default boolean hasIndexedProperty(String propertyName) {
        Stream<? extends PropertyDesc> stream = getProperties().stream();
        return stream.filter(PropertyDesc::isIndexed)
                .map(PropertyDesc::getName)
                .anyMatch(propertyName::equals);
    }


    default BeanEventTypes<? extends BeanEventType> getBeanEventTypes() {
        return getBeanEvents(null);
    }

    default BeanEventTypes<? extends BeanEventType> getBeanEvents(Class<?> stopClass) {
        return new BeanEventTypes<>(Collections.emptyList());
    }

    default boolean isListenerSupported(Class<?> listenerClass) {
        return getBeanEventTypes()
                .stream()
                .anyMatch(be -> be.isApplicable(listenerClass));
    }

}
