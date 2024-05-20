package com.link_intersystems.beans;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyList extends AbstractList<Property> {

    private List<? extends Property> properties;

    private Map<String, Property> propertyMap = new HashMap<>();
    private Map<PropertyDesc, Property> propertyByDesc = new HashMap<>();

    private IdentityHashMap<Object, PropertyList> wellKnownProperties = new IdentityHashMap<>(2);

    public PropertyList(List<? extends Property> properties) {
        this.properties = requireNonNull(properties);

        properties.forEach(property -> {
            String name = property.getPropertyDesc().getName();
            propertyMap.put(name, property);
            propertyByDesc.put(property.getPropertyDesc(), property);
        });

        wellKnownProperties.put(Property.PREDICATE, null);
        wellKnownProperties.put(Property.INDEXED_PROPERTY_PREDICATE, null);
    }

    public void copy(PropertyList targetProperties) {
        copy(targetProperties, PropertySelectors.BY_NAME);
    }

    public void copy(PropertyList targetProperties, PropertySelector propertySelector) {
        copy(targetProperties, propertySelector, PropertyCopyStrategies.EXACT);
    }

    /**
     * Copies this {@link PropertyList}'s values to the targetProperties using the given {@link PropertyCopyStrategy} if one can be selected using the targetPropertySelector.
     *
     * @param targetProperties
     * @param targetPropertySelector
     * @see PropertyCopyStrategies
     */
    public void copy(PropertyList targetProperties, PropertySelector targetPropertySelector, PropertyCopyStrategy propertyCopyStrategy) {
        forEach(sourceProperty -> {
            Property targetProperty = targetPropertySelector.select(targetProperties, sourceProperty);
            if (targetProperty != null) {
                propertyCopyStrategy.copy(sourceProperty, targetProperty);
            }
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

    public PropertyList filter(Predicate<? super Property> propertyPredicate) {

        if (wellKnownProperties.containsKey(propertyPredicate)) {
            return wellKnownProperties.computeIfAbsent(propertyPredicate, this::defaultFilter);
        }

        return defaultFilter(propertyPredicate);
    }

    @SuppressWarnings("unchecked")
    private PropertyList defaultFilter(Object object) {
        return defaultFilter((Predicate<? super Property>) object);
    }

    protected PropertyList defaultFilter(Predicate<? super Property> propertyPredicate) {
        return new PropertyList(stream().filter(propertyPredicate).collect(Collectors.toList()));
    }
}
