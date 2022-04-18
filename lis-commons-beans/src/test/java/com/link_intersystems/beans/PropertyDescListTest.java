package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */

class PropertyDescListTest {

    private PropertyDescList<? extends PropertyDesc> propertyDescList;
    private PropertyDesc firstnamePropertyDesc;
    private PropertyDesc lastnamePropertyDesc;
    private IndexedPropertyDesc favoriteColorsPropertyDesc;

    @BeforeEach
    void setUp() {
        List<PropertyDesc> descriptors = new ArrayList<>();

        firstnamePropertyDesc = Mockito.mock(PropertyDesc.class);
        when(firstnamePropertyDesc.getName()).thenReturn("firstname");

        lastnamePropertyDesc = Mockito.mock(PropertyDesc.class);
        when(lastnamePropertyDesc.getName()).thenReturn("lastname");

        favoriteColorsPropertyDesc = Mockito.mock(IndexedPropertyDesc.class);
        when(favoriteColorsPropertyDesc.getName()).thenReturn("favoriteColors");

        descriptors.add(firstnamePropertyDesc);
        descriptors.add(lastnamePropertyDesc);
        descriptors.add(favoriteColorsPropertyDesc);

        propertyDescList = new PropertyDescList<>(descriptors);
    }

    @Test
    void getAllPropertyNames() {
        assertEquals(Arrays.asList("firstname", "lastname", "favoriteColors"), propertyDescList.getAllPropertyNames());
    }

    @Test
    void getPropertyNames() {
        assertEquals(Arrays.asList("firstname", "lastname"), propertyDescList.getPropertyNames());
    }

    @Test
    void getIndexedPropertyNames() {
        assertEquals(Arrays.asList("favoriteColors"), propertyDescList.getIndexedPropertyNames());
    }

    @Test
    void getByName() {
        assertEquals(lastnamePropertyDesc, propertyDescList.getByName("lastname"));
        assertEquals(favoriteColorsPropertyDesc, propertyDescList.getByName("favoriteColors"));

        assertNull(propertyDescList.getByName("favoritecolors"));
    }

    @Test
    void getByNameWithEquality() {
        assertEquals(favoriteColorsPropertyDesc, propertyDescList.getByName("favoritecolors", String::equalsIgnoreCase));
    }

    @Test
    void get() {
        assertEquals(firstnamePropertyDesc, propertyDescList.get(0));
        assertEquals(lastnamePropertyDesc, propertyDescList.get(1));
        assertEquals(favoriteColorsPropertyDesc, propertyDescList.get(2));
    }

    @Test
    void size() {
        assertEquals(3, propertyDescList.size());
    }
}