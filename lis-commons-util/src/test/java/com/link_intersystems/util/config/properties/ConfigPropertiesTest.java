package com.link_intersystems.util.config.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ConfigPropertiesTest {

    @Test
    void getProperty() {
        ConfigProperty<String> property1 = ConfigProperty.named("prop1").typed(String.class).withDefaultValue("prop1");
        ConfigProperty<String> property2 = ConfigProperty.named("prop1").typed(String.class).withDefaultValue("prop1");

        ConfigProperties configProperties = new ConfigProperties();
        configProperties.setProperty(property1, "prop1");
        configProperties.setProperty(property2, "prop2");

        assertEquals("prop1", configProperties.getProperty(property1));
        assertEquals("prop2", configProperties.getProperty(property2));
    }


    @Test
    void getPropertyDefaultValue() {
        ConfigProperty<String> property1 = ConfigProperty.named("test1").withDefaultValue("prop1").notNull();
        ConfigProperty<String> property2 = ConfigProperty.named("test2").typed(String.class);

        ConfigProperties configProperties = new ConfigProperties();

        assertEquals("prop1", configProperties.getProperty(property1));
        assertNull(configProperties.getProperty(property2));
    }

    @Test
    void getNotNullProperty() {
        ConfigProperty<String> property = ConfigProperty.named("prop1").notNull().typed(String.class);

        ConfigProperties configProperties = new ConfigProperties();
        assertThrows(IllegalStateException.class, () -> configProperties.getProperty(property));
    }


    @Test
    void setNotNullPropery() {
        ConfigProperty<String> property = ConfigProperty.named("test1").notNull().typed(String.class);

        ConfigProperties configProperties = new ConfigProperties();

        assertThrows(IllegalArgumentException.class, () -> configProperties.setProperty(property, null));
    }
}