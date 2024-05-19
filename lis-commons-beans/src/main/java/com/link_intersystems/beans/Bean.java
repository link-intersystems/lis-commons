package com.link_intersystems.beans;

public interface Bean<T> {
    BeanClass<T> getBeanClass();

    T getBeanObject();

    PropertyList getProperties();

    void removeListener(Object listener);

    void addListener(Object listener);

}
