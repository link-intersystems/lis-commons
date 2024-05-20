package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void cacheWellKnownProperties() {
        Property property = PropertyMocks.createProperty(String.class, "firstname", "René");

        PropertyList properties = new PropertyList(List.of(property));

        PropertyList firstCall = properties.filter(Property.PREDICATE);
        PropertyList secondCall = properties.filter(Property.PREDICATE);

        assertSame(firstCall, secondCall);
    }

    @Test
    void doNotCacheUnknownProperties() {
        Property property = PropertyMocks.createProperty(String.class, "firstname", "René");
        PropertyList properties = new PropertyList(List.of(property));

        Predicate<? super Property> predicate = (p) -> true;
        PropertyList firstCall = properties.filter(predicate);
        PropertyList secondCall = properties.filter(predicate);

        assertEquals(firstCall, secondCall);
        assertNotSame(firstCall, secondCall);
    }

    /**
     * If the {@link PropertyList} would ever change its immutability we must detect it here,
     * because the caching will no longer work as expected.
     */
    @Test
    void cacheablePropertyListMustBeImmutable() {
        Property property = PropertyMocks.createProperty(String.class, "firstname", "René");
        PropertyList properties = new PropertyList(List.of(property));

        PropertyList filtered = properties.filter(Property.PREDICATE);

        assertThrows(UnsupportedOperationException.class, () -> filtered.clear());
    }

}