package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opentest4j.AssertionFailedError;

import java.beans.Introspector;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PropertyListCopyTest {

    private PropertyList propertyList;
    private Property nameProperty;
    private Property ageProperty;

    @BeforeEach
    void setUp() {
        nameProperty = PropertyMocks.createProperty(String.class, "name", "René");
        ageProperty = PropertyMocks.createProperty(Integer.class, "age", 45);

        propertyList = new PropertyList(Arrays.asList(nameProperty, ageProperty));
    }

    @Test
    void copyByName() {
        Property targetName = PropertyMocks.createProperty(String.class, "name", "John");
        Property targetLastname = PropertyMocks.createProperty(String.class, "lastname", "Doe");

        PropertyList targetPropertyList = new PropertyList(Arrays.asList(targetName, targetLastname));

        propertyList.copy(targetPropertyList);

        verify(targetName).setValue("René");
        verify(targetLastname, never()).setValue(anyString());
    }

    @Test
    void copyIndexedPropertiesByValue() {
        IndexedProperty sourceNames = PropertyMocks.createIndexedProperty(String.class, "names", "René", "Link");
        PropertyList sourcePropertyList = new PropertyList(Arrays.asList(sourceNames));

        IndexedProperty targetNames = PropertyMocks.createIndexedProperty(String.class, "names", "John", "Doe");
        PropertyList targetPropertyList = new PropertyList(Arrays.asList(targetNames));


        sourcePropertyList.copy(targetPropertyList);

        verify(targetNames).setValue(new String[]{"René", "Link"});
        verify(targetNames, never()).setValue(eq(0), anyString());
        verify(targetNames, never()).setValue(eq(1), anyString());
    }

    @Test
    void copyIndexedPropertiesByIndex() {
        IndexedProperty sourceNames = PropertyMocks.createIndexedProperty(String.class, "names", "René", "Link");
        when(sourceNames.getPropertyDesc().isIndexedReadable()).thenReturn(false);
        when(sourceNames.getValue(Mockito.any(int.class))).thenThrow(new AssertionFailedError("indexed getter should not be called."));
        PropertyList sourcePropertyList = new PropertyList(Arrays.asList(sourceNames));

        IndexedProperty targetNames = PropertyMocks.createIndexedProperty(String.class, "names", "John", "Doe");
        when(targetNames.getPropertyDesc().isWritable()).thenReturn(false);
        PropertyList targetPropertyList = new PropertyList(Arrays.asList(targetNames));


        sourcePropertyList.copy(targetPropertyList);

        verify(targetNames, never()).setValue(Mockito.any(String[].class));
        verify(targetNames).setValue(0, "René");
        verify(targetNames).setValue(1, "Link");
    }

}