package com.link_intersystems.beans;

import static java.util.Objects.*;

public class PropertyCopier {

    private PropertyList properties;

    private PropertySelector propertySelector = PropertySelectors.BY_NAME;

    public PropertyCopier(PropertyList properties) {
        this.properties = requireNonNull(properties);
    }

    /**
     * Applies the values of this {@link PropertyCopier}'s {@link PropertyList} to the given {@link PropertyList},
     * if the target property could be resolved by the PropertySelector.
     *
     * @param targetProperties
     */
    public void applyTo(PropertyList targetProperties) {
        copy(properties, targetProperties, propertySelector);
    }

    /**
     * Adopt the values of the given {@link PropertyList} to this {@link PropertyCopier}'s {@link PropertyList},
     * if a source property could be resolved by the PropertySelector.
     *
     * @param sourceProperties
     */
    public void adoptFrom(PropertyList sourceProperties) {
        copy(sourceProperties, properties, propertySelector);
    }

    /**
     * Copies the sourceProperties values to the targetProperties if one can be selected using the targetPropertySelector.
     *
     * @param sourceProperties
     * @param targetProperties
     * @param targetPropertySelector
     */
    public static void copy(PropertyList sourceProperties, PropertyList targetProperties, PropertySelector targetPropertySelector) {
        sourceProperties.forEach(sourceProperty -> {
            Property targetProperty = targetPropertySelector.select(targetProperties, sourceProperty);
            if (targetProperty != null) {
                Object value = sourceProperty.getValue();
                targetProperty.setValue(value);
            }
        });
    }
}
