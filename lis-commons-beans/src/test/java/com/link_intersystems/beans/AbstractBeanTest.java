package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractBeanTest {


    private static class TestBean extends AbstractBean {

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
    void cacheWellKnownProperties() {
        Property property = PropertyMocks.createProperty(String.class, "firstname", "René");
        testBean.setProperties(property);

        PropertyList firstCall = testBean.getProperties(Property.PREDICATE);
        PropertyList secondCall = testBean.getProperties(Property.PREDICATE);

        assertSame(firstCall, secondCall);
    }

    @Test
    void doNotCacheUnknownProperties() {
        Property property = PropertyMocks.createProperty(String.class, "firstname", "René");
        testBean.setProperties(property);

        Predicate<? super Property> predicate = (p) -> true;
        PropertyList firstCall = testBean.getProperties(predicate);
        PropertyList secondCall = testBean.getProperties(predicate);

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
        testBean.setProperties(property);

        PropertyList properties = testBean.getProperties(Property.PREDICATE);

        assertThrows(UnsupportedOperationException.class, () -> properties.clear());
    }
}