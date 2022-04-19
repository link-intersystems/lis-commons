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

    private List<? extends Property> properties;

    private Map<String, Property> propertyMap = new HashMap<>();
    private Map<PropertyDesc, Property> propertyByDesc = new HashMap<>();


    public PropertyList(List<? extends Property> properties) {
        this.properties = requireNonNull(properties);

        properties.forEach(property -> {
            String name = property.getPropertyDesc().getName();
            propertyMap.put(name, property);
            propertyByDesc.put(property.getPropertyDesc(), property);
        });
    }

    public Property getByName(String propertyName) {
        return propertyMap.get(propertyName);
    }

    public Property getByDesc(PropertyDesc propertyDesc) {
        return propertyByDesc.get(propertyDesc);
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
