package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BeanTest {


    private static class TestBean extends Bean {

        private List<Property> propertyList = new ArrayList<>();

        public void setProperties(Property... properties) {
            this.propertyList = Arrays.asList(properties);
        }

        protected TestBean() {
            super(Mockito.mock(BeanClass.class), "bean");
        }

        @Override
        public PropertyList getProperties() {
            return new PropertyList(propertyList);
        }

        @Override
        protected BeanEvent getApplicableBeanEvent(Object listener) {
            return null;
        }
    }


    private TestBean testBean;

    @BeforeEach
    void setUp() {
        testBean = new TestBean();
    }

    @Test
    void removeListener() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        assertThrows(UnsupportedOperationException.class, () -> testBean.removeListener(listener));
    }

    @Test
    void addListener() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);

        assertThrows(UnsupportedOperationException.class, () -> testBean.addListener(listener));
    }

    @Test
    void propertiesEqual() {
        Property property = PropertyMocks.createProperty(String.class, "propertyName",null);

        testBean.setProperties(property);

        TestBean testBean2 = new TestBean();
        testBean2.setProperties(property);


        assertTrue(testBean.propertiesEqual(testBean2));
    }


    @Test
    void copy() {
        Property firstname = PropertyMocks.createProperty(String.class, "firstname","René");
        Property lastname = PropertyMocks.createProperty(String.class, "lastname","Link");
        testBean.setProperties(firstname, lastname);

        TestBean testBean2 = new TestBean();
        Property firstname2 = PropertyMocks.createProperty(String.class, "firstname","John");
        Property lastname2 = PropertyMocks.createProperty(String.class, "lastname","Doe");
        testBean2.setProperties(firstname2, lastname2);

        testBean.copyProperties(testBean2);

        verify(firstname2).setValue("René");
        verify(lastname2).setValue("Link");
    }
}