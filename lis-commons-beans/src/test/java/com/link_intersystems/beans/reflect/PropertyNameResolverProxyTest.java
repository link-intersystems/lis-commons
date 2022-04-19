package com.link_intersystems.beans.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import static org.junit.jupiter.api.Assertions.*;

class PropertyNameResolverProxyTest {

    private PropertyNameResolverProxy<SomeBeanInterface> propertyNameResolverProxy;
    private SomeBeanInterface someBeanInterface;

    @BeforeEach
    public void setup() throws IntrospectionException {
        propertyNameResolverProxy = new PropertyNameResolverProxy<>(SomeBeanInterface.class);
        someBeanInterface = propertyNameResolverProxy.createProxy();
    }

    private void assertLatestProperty(String propertyName) {
        PropertyDescriptor latestCallPropertyDescriptor = propertyNameResolverProxy.getLatestCallPropertyDescriptor();
        assertNotNull(latestCallPropertyDescriptor, "latestCallPropertyDescriptor");

        String name = latestCallPropertyDescriptor.getName();
        assertEquals(propertyName, name);
    }

    @Test
    void noProperyAccessed() {
        PropertyDescriptor latestCallPropertyDescriptor = propertyNameResolverProxy.getLatestCallPropertyDescriptor();
        assertNull(latestCallPropertyDescriptor, "latestCallPropertyDescriptor");
    }

    @Test
    void booleanGetter() {
        someBeanInterface.isEnabled();

        assertLatestProperty("enabled");
    }

    @Test
    void booleanSetter() {
        someBeanInterface.setEnabled(false);

        assertLatestProperty("enabled");
    }

    @Test
    void objectGetter() {
        someBeanInterface.getTitle();

        assertLatestProperty("title");
    }

    @Test
    void objectSetter() {
        someBeanInterface.setTitle("");

        assertLatestProperty("title");
    }

    @Test
    void latestPropertyDescriptorReturned() {
        someBeanInterface.setTitle("");
        someBeanInterface.setEnabled(true);

        assertLatestProperty("enabled");
    }

    public static interface SomeBeanInterface {

        public boolean isEnabled();

        public void setEnabled(boolean enabled);

        public String getTitle();

        public void setTitle(String title);

    }

}
