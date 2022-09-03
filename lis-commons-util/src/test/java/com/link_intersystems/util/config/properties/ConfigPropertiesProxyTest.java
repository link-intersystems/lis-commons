package com.link_intersystems.util.config.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ConfigPropertiesProxyTest {

    @Test
    void propertyAccess() {
        ConfigProperties configProperties = new ConfigProperties();
        TestProperties testProperties = ConfigPropertiesProxy.create(configProperties, TestProperties.class);

        testProperties.setName("SomeName");
        assertEquals("SomeName", configProperties.getProperty(TestProperties.NAME));

        configProperties.setProperty(TestProperties.NAME, "OtherName");
        assertEquals("OtherName", testProperties.getName());

        assertTrue(testProperties.isRegistered());

        configProperties.setProperty(TestProperties.REGISTERED, false);
        assertFalse(testProperties.isRegistered());

    }

    @Test
    void camelCaseProperty() {
        ConfigProperties configProperties = new ConfigProperties();
        TestProperties testProperties = ConfigPropertiesProxy.create(configProperties, TestProperties.class);

        configProperties.setProperty(TestProperties.FIRST_NAME, "firstname");
        assertEquals("firstname", testProperties.getFirstName());
    }

    @Test
    void superPropertyAccess() {
        ConfigProperties configProperties = new ConfigProperties();
        TestProperties testProperties = ConfigPropertiesProxy.create(configProperties, TestProperties.class);

        assertEquals(1, testProperties.getCount());

    }

    @Test
    void noProperty() {
        ConfigProperties configProperties = new ConfigProperties();
        TestProperties testProperties = ConfigPropertiesProxy.create(configProperties, TestProperties.class);

        assertThrows(IllegalStateException.class, testProperties::getNoProperty);
        assertThrows(IllegalStateException.class, testProperties::otherMethod);
    }

    @Test
    void objectMethods() {
        ConfigProperties configProperties = new ConfigProperties();
        TestProperties testProperties = ConfigPropertiesProxy.create(configProperties, TestProperties.class);

        assertEquals(testProperties, testProperties);
        assertNotEquals("testProperties", testProperties);
        testProperties.hashCode();
        testProperties.toString();
    }
}