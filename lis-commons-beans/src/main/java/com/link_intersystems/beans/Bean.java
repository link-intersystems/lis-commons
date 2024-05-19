package com.link_intersystems.beans;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Bean<T> {
    public BeanClass<T> getBeanClass();

    public T getBeanObject();

    public PropertyList getProperties();


    public default PropertyList getProperties(Predicate<? super Property> predicate) {
        return new PropertyList(getProperties().stream().filter(predicate).collect(Collectors.toList()));
    }

    public void removeListener(Object listener);

    public void addListener(Object listener);

}
