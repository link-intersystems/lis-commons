package com.link_intersystems.beans;

import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface Bean<T> {
    List<Property> getProperties();

    Property getProperty(String propertyName);

    Property getProperty(PropertyDesc propertyDesc);

    T getObject();

    BeanClass<T> getBeanClass();

    default <L> BeanEventSupport<T, L> newBeanEventSupport() {
        BeanEventSupport<T, L> eventSupport = new BeanEventSupport<>();
        eventSupport.setBean(this);
        return eventSupport;
    }

    void removeListener(Object listener);

    void addListener(Object listener);


}
