package com.link_intersystems.util.config.properties;

import java.text.MessageFormat;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ConfigProperties {

    private Map<ConfigProperty, ConfigPropertyValue> propertyValues = new IdentityHashMap<>();

    public <T> void setProperty(ConfigProperty<T> property, T value) {
        ConfigPropertyValue<T> propertyValue = propertyValues.computeIfAbsent(property, p -> new ConfigPropertyValue(p));
        propertyValue.setValue(value);
    }

    public <T> T getProperty(ConfigProperty<T> property) {
        ConfigPropertyValue<T> propertyValue = propertyValues.get(property);

        T value = null;

        if (propertyValue != null) {
            value = propertyValue.getValue();
        }

        if (value == null) {
            value = property.getDefaultValue();
        }

        if (value == null && !property.isOptional()) {
            String msg = MessageFormat.format("Not null property ''{0}'' is not set", property);
            throw new IllegalStateException(msg);
        }

        return value;
    }

}
