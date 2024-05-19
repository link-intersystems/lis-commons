package com.link_intersystems.beans;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Bean<T> {
    BeanClass<T> getBeanClass();

    T getBeanObject();

    public PropertyList getProperties();


    default PropertyList getProperties(Predicate<? super Property> predicate) {
        return new PropertyList(getProperties().stream().filter(predicate).collect(Collectors.toList()));
    }

    void removeListener(Object listener);

    void addListener(Object listener);

}
