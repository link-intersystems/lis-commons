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
}