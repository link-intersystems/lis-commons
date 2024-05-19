package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AbstractPropertyTest {

    private AbstractProperty abstractProperty;
    private PropertyDesc propertyDesc;

    @BeforeEach
    void setUp() {
        propertyDesc = mock(PropertyDesc.class);
        abstractProperty = new AbstractProperty(propertyDesc) {

            @Override
            protected Object getBeanObject() {
                return AbstractPropertyTest.this;
            }
        };
    }

    @Test
    void getValue() {
        when(propertyDesc.getPropertyValue(this)).thenReturn("result");

        Object value = abstractProperty.getValue();

        assertEquals("result", value);
    }


    @Test
    void setValue() {
        abstractProperty.setValue("arg");

        verify(propertyDesc).setPropertyValue(this, "arg");
    }
}