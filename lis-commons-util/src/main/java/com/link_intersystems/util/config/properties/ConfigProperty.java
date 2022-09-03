package com.link_intersystems.util.config.properties;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ConfigProperty<T> {


    public static class ConfigPropertyBuilder {

        private boolean optional = true;
        private String propertyName;

        ConfigPropertyBuilder(String propertyName) {
            this.propertyName = requireNonNull(propertyName);
        }

        public ConfigPropertyBuilder notNull() {
            optional = false;
            return this;
        }

        public <T> ConfigProperty<T> typed(Class<T> type) {
            return new ConfigProperty<>(propertyName, type, optional);
        }

        public <T> ConfigProperty<T> withDefaultValue(T defaultValue) {
            ConfigProperty<T> configProperty = new ConfigProperty<>(propertyName, (Class<T>) defaultValue.getClass(), optional);
            configProperty.defaultValue = defaultValue;
            return configProperty;
        }
    }

    public static ConfigPropertyBuilder named(String name) {
        return new ConfigPropertyBuilder(name);
    }


    private String propertyName;
    private Class<T> type;
    private boolean optional;
    private T defaultValue;

    private ConfigProperty(String propertyName, Class<T> type, boolean optional) {
        this.propertyName = propertyName;
        this.type = type;
        this.optional = optional;
    }

    public ConfigProperty<T> notNull() {
        ConfigProperty<T> configProperty = new ConfigProperty<>(propertyName, type, false);
        configProperty.defaultValue = defaultValue;
        return configProperty;
    }

    public ConfigProperty<T> withDefaultValue(T defaultValue) {
        ConfigProperty<T> configProperty = new ConfigProperty<>(propertyName, type, optional);
        configProperty.defaultValue = defaultValue;
        return configProperty;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "ConfigProperty{" +
                "propertyName='" + propertyName + '\'' +
                ", type=" + type +
                ", optional=" + optional +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
