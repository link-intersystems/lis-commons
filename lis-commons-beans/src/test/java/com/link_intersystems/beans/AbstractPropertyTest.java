package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class AbstractPropertyTest {

    class TestProperty extends AbstractProperty {

        private final Object bean;

        public TestProperty(Object bean, PropertyDesc propertyDescriptor) {
            super(propertyDescriptor);
            this.bean = bean;
        }

        public Object getBean() {
            return bean;
        }

        public TestProperty() {
            this(new Object(), mock(PropertyDesc.class));
        }

        public void setTestValue(Object testValue) {
            PropertyDesc propertyDesc = getPropertyDesc();
            when(propertyDesc.getPropertyValue(eq(bean))).thenReturn(testValue);
            propertyDesc.setPropertyValue(bean, testValue);
        }

        public Object getTestValue() {
            ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
            PropertyDesc propertyDesc = getPropertyDesc();
            verify(propertyDesc).setPropertyValue(eq(bean), argumentCaptor.capture());
            return argumentCaptor.getValue();
        }

        @Override
        protected Object getBeanObject() {
            return bean;
        }
    }

    private TestProperty testProperty;


    @BeforeEach
    void setUp() {
        testProperty = new TestProperty();
    }

    @Test
    void getValue() {
        testProperty.setTestValue("result");
        assertEquals("result", testProperty.getTestValue());

        Object value = testProperty.getValue();

        assertEquals("result", value);
    }


    @Test
    void setValue() {
        testProperty.setValue("arg");

        assertEquals("arg", testProperty.getTestValue());
    }

    @Nested
    class Equals {

        private TestProperty otherProperty;
        private TestProperty otherEqualProperty;

        @BeforeEach
        void setUp() {
            otherProperty = new TestProperty(new Object(), testProperty.getPropertyDesc());
            otherEqualProperty = new TestProperty(new Object(), testProperty.getPropertyDesc());
        }

        @Test
        void equalsValue() {
            testProperty.setTestValue("test");
            otherEqualProperty.setTestValue("test");
            otherProperty.setTestValue("other");

            assertEquals(testProperty, otherEqualProperty);
            assertEquals(testProperty.hashCode(), otherEqualProperty.hashCode());

            assertNotEquals(testProperty, otherProperty);
        }

        @Test
        void nullValue() {
            testProperty.setTestValue(null);
            otherEqualProperty.setTestValue(null);
            otherProperty.setTestValue("other");

            assertNotEquals(testProperty, otherProperty);
            assertNotEquals(otherProperty, testProperty);

            assertEquals(testProperty, otherEqualProperty);
        }

        @Test
        void typeMismatchValue() {
            testProperty.setTestValue("test");
            otherProperty.setTestValue(new String[]{"test"});

            assertNotEquals(testProperty, otherProperty);
            assertNotEquals(otherProperty, testProperty);
        }

        @Test
        void equalsArrays() {
            testProperty.setTestValue(new String[]{"test1", "test2"});
            otherEqualProperty.setTestValue(new String[]{"test1", "test2"});
            otherProperty.setTestValue(new String[]{"test1", "notEqual"});

            assertEquals(testProperty, otherEqualProperty);
            assertEquals(otherEqualProperty, testProperty);
            assertEquals(testProperty.hashCode(), otherEqualProperty.hashCode());

            assertNotEquals(testProperty, otherProperty);
            assertNotEquals(otherEqualProperty, otherProperty);
        }
    }


}