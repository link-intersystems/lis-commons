package com.link_intersystems.beans.simple;

import com.link_intersystems.beans.BeanClass;
import com.link_intersystems.beans.BeanInstantiationException;
import com.link_intersystems.beans.PropertyDesc;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SimpleBeanClass<T> {

    private BeanClass<T> beanClass;
    private PropertyList propertyList;

    public SimpleBeanClass(BeanClass<T> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getType() {
        return beanClass.getType();
    }

    public String getName() {
        return beanClass.getName();
    }

    public PropertyList getProperties() {
        if (propertyList == null) {
            List<SimpleProperty> simpleProperties = beanClass.getProperties().stream()
                    .map(this::toSimpleProperty)
                    .collect(toList());
            propertyList = new PropertyList(simpleProperties);
        }
        return propertyList;
    }

    private SimpleProperty toSimpleProperty(PropertyDesc<?> propertyDesc) {
        return new SimpleProperty(propertyDesc, (BeanClass<Object>) beanClass);
    }

    public T newInstance() throws BeanInstantiationException{
        return beanClass.newInstance();
    }
}
