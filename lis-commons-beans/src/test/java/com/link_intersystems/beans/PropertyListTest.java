package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PropertyListTest {

    private PropertyList propertyList;
    private Property firstnameProperty;
    private Property lastnameProperty;

    @BeforeEach
    void setUp() {
        firstnameProperty = createProperty("firstname", "René");
        lastnameProperty = createProperty("lastname", "Link");

        propertyList = new PropertyList(Arrays.asList(firstnameProperty, lastnameProperty));
    }

    private Property createProperty(String name, String value) {
        Property property = mock(Property.class);
        PropertyDesc propDesc = mock(PropertyDesc.class);
        when(propDesc.getName()).thenReturn(name);
        when(property.getValue()).thenReturn(value);
        when(property.getPropertyDesc()).thenReturn(propDesc);
        return property;
    }

    @Test
    void getByName() {
        assertEquals(firstnameProperty, propertyList.getByName("firstname"));
        assertEquals(lastnameProperty, propertyList.getByName("lastname"));
    }

    @Test
    void getByDesc() {
        assertEquals(firstnameProperty, propertyList.getByDesc(firstnameProperty.getPropertyDesc()));
        assertEquals(lastnameProperty, propertyList.getByDesc(lastnameProperty.getPropertyDesc()));
    }

    @Test
    void filter() {
        PropertyList filteredList = propertyList.filter(p -> p.getPropertyDesc().getName().startsWith("first"));
        assertEquals(1, filteredList.size());
        assertEquals(firstnameProperty, filteredList.getByDesc(firstnameProperty.getPropertyDesc()));
    }

    @Test
    void get() {
        assertEquals(firstnameProperty, propertyList.get(0));
        assertEquals(lastnameProperty, propertyList.get(1));
    }

    @Test
    void size() {
        assertEquals(2, propertyList.size());
    }

    @Test
    void copy() {
        Property firstname = PropertyMocks.createProperty(String.class, "firstname", "René");
        Property lastname = PropertyMocks.createProperty(String.class, "lastname", "Link");
        PropertyList properties = new PropertyList(Arrays.asList(firstname, lastname));

        Property firstname2 = PropertyMocks.createProperty(String.class, "firstname", "John");
        Property lastname2 = PropertyMocks.createProperty(String.class, "lastname", "Doe");
        PropertyList properties2 = new PropertyList(Arrays.asList(firstname2, lastname2));

        properties.copy(properties2);

        verify(firstname2).setValue("René");
        verify(lastname2).setValue("Link");
    }

    @Test
    void propertiesEqual() {
        PropertyList properties = new PropertyList(propertyList);
        PropertyList properties2 = new PropertyList(propertyList);

        assertEquals(properties, properties2);
    }

}