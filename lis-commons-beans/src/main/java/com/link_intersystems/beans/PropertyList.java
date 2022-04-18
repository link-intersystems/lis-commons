package com.link_intersystems.beans;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyList extends AbstractList<Property> {

    private List<Property> properties;

    private Map<String, Property> propertyMap = new HashMap<>();
    private Map<String, IndexedProperty> indexedPropertyMap = new HashMap<>();

    private Map<PropertyDesc, Property> propertyByDesc = new HashMap<>();
    private Map<PropertyDesc, IndexedProperty> indexedPropertyByDesc = new HashMap<>();


    public PropertyList(List<Property> properties) {
        this.properties = requireNonNull(properties);

        properties.forEach(property -> {
            String name = property.getPropertyDesc().getName();
            if (property instanceof IndexedProperty) {
                IndexedProperty indexedProperty = (IndexedProperty) property;
                indexedPropertyMap.put(name, indexedProperty);
                indexedPropertyByDesc.put(property.getPropertyDesc(), indexedProperty);
            } else {
                propertyMap.put(name, property);
                propertyByDesc.put(property.getPropertyDesc(), property);
            }
        });
    }

    public Property getAnyProperty(String propertyName) {
        Property property = getProperty(propertyName);
        if (property == null) {
            return getIndexedProperty(propertyName);
        }
        return property;
    }

    public Property getAnyProperty(PropertyDesc propertyDesc) {
        Property property = getProperty(propertyDesc);
        if (property == null) {
            return getIndexedProperty(propertyDesc);
        }
        return property;
    }

    public Property getProperty(String propertyName) {
        return propertyMap.get(propertyName);
    }

    public Property getProperty(PropertyDesc propertyDesc) {
        return propertyByDesc.get(propertyDesc);
    }

    public Property getIndexedProperty(String propertyName) {
        return indexedPropertyMap.get(propertyName);
    }

    public Property getIndexedProperty(PropertyDesc propertyDesc) {
        return indexedPropertyByDesc.get(propertyDesc);
    }


    @Override
    public Property get(int index) {
        return properties.get(index);
    }

    @Override
    public int size() {
        return properties.size();
    }
}
