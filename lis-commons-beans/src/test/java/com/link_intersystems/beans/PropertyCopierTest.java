package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.mockito.Mockito.*;

class PropertyCopierTest {

    private PropertyList propertyList;
    private Property nameProperty;
    private Property ageProperty;
    private PropertyCopier propertyCopier;

    @BeforeEach
    void setUp() {
        nameProperty = createProperty(String.class, "name", "René");
        ageProperty = createProperty(Integer.class, "age", 45);

        propertyList = new PropertyList(Arrays.asList(nameProperty, ageProperty));

        propertyCopier = new PropertyCopier(propertyList);
    }

    private Property createProperty(Class<?> type, String name, Object value) {
        Property property = mock(Property.class);
        PropertyDesc propDesc = mock(PropertyDesc.class);
        when(propDesc.getType()).thenReturn((Class) type);
        when(propDesc.getName()).thenReturn(name);
        when(property.getValue()).thenReturn(value);
        when(property.getPropertyDesc()).thenReturn(propDesc);
        return property;
    }

    @Test
    void applyTo() {
        Property targetName = createProperty(String.class, "name", "John");
        Property targetLastname = createProperty(String.class, "lastname", "Doe");

        PropertyList targetPropertyList = new PropertyList(Arrays.asList(targetName, targetLastname));

        propertyCopier.applyTo(targetPropertyList);

        verify(targetName).setValue("René");
        verify(targetLastname, never()).setValue(Mockito.anyString());
    }

    @Test
    void adoptFrom() {
        Property sourceName = createProperty(String.class, "name", "John");
        Property sourceLastname = createProperty(String.class, "lastname", "Doe");

        PropertyList sourcePropertyList = new PropertyList(Arrays.asList(sourceName, sourceLastname));

        propertyCopier.adoptFrom(sourcePropertyList);

        verify(nameProperty).setValue("John");
        verify(ageProperty, never()).setValue(Mockito.any(Integer.class));
    }
}