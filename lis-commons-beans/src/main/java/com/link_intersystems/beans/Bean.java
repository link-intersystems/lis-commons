package com.link_intersystems.beans;

import java.util.function.Predicate;

public interface Bean<T> {
    public BeanClass<T> getBeanClass();

    public T getBeanObject();

    public PropertyList getProperties();

    public void removeListener(Object listener);

    public void addListener(Object listener);

}
