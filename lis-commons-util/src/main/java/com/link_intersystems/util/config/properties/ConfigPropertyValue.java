package com.link_intersystems.util.config.properties;

import java.text.MessageFormat;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ConfigPropertyValue<T> {

    private ConfigProperty<T> property;
    private T propertyValue;

    public ConfigPropertyValue(ConfigProperty<T> property) {
        this.property = requireNonNull(property);
    }

    public void setValue(T propertyValue) {
        if (propertyValue == null && !property.isOptional()) {
            String msg = MessageFormat.format("Can not set ''null'' to not null property ''{0}''", property);
            throw new IllegalArgumentException(msg);
        }
        this.propertyValue = propertyValue;
    }

    public T getValue() {
        return propertyValue;
    }
}
