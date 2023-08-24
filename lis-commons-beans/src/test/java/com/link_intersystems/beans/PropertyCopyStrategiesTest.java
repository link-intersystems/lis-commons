package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static com.link_intersystems.beans.PropertyMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PropertyCopyStrategiesTest {

    @Nested
    class Exact {

        protected PropertyCopyStrategy strategy;

        @BeforeEach
        void setUp() {
            strategy = PropertyCopyStrategies.EXACT;
        }

        protected void copy(Property sourceProperty, Property targetProperty) {
            strategy.copy(sourceProperty, targetProperty);
        }

        @Test
        void copyIndexedToNonIndexedProperty() {
            Property sourceProperty = createIndexedProperty(String.class, "firstname", "René");
            Property targetProperty = createProperty(String.class, "firstname", "John");

            assertThrows(IllegalArgumentException.class, () -> copy(sourceProperty, targetProperty));
        }

        @Test
        void copyNonIndexedToIndexedProperty() {
            Property sourceProperty = createProperty(String.class, "firstname", "René");
            Property targetProperty = createIndexedProperty(String.class, "firstname", "John");

            assertThrows(IllegalArgumentException.class, () -> copy(sourceProperty, targetProperty));
        }

        @Test
        void copyProperties() {
            Property sourceProperty = createProperty(String.class, "firstname", "René");
            Property targetProperty = createProperty(String.class, "firstname", "John");

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue("René");
            verify(sourceProperty, never()).setValue(anyString());
        }


        @Test
        void copyIndexedPropertyByValue() {
            Property sourceProperty = createIndexedProperty(String.class, "names", "René", "John");
            Property targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue(new String[]{"René", "John"});
            verify(sourceProperty, never()).setValue(any(String[].class));
        }

        @Test
        void copyIndexedPropertyByIndex() {
            IndexedProperty sourceProperty = createIndexedProperty(String.class, "names", "René", "John");
            when(sourceProperty.getPropertyDesc().isReadable()).thenReturn(false);
            when(sourceProperty.getValue()).thenThrow(new AssertionFailedError("getValue should not be called"));

            IndexedProperty targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");
            when(targetProperty.getPropertyDesc().isWritable()).thenReturn(false);
            doThrow(new AssertionFailedError("setValue should not be called")).when(targetProperty).setValue(any());

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue(0);
            verify(targetProperty).setValue(0, "René");

            verify(sourceProperty).getValue(1);
            verify(targetProperty).setValue(1, "John");
        }

        @Test
        void copyIndexedPropertyByIndexSourceReadableArray() {
            IndexedProperty sourceProperty = createIndexedProperty(String.class, "names", "René", "John");
            when(sourceProperty.getPropertyDesc().isReadable()).thenReturn(true);

            IndexedProperty targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");
            when(targetProperty.getPropertyDesc().isWritable()).thenReturn(false);
            when(targetProperty.getValue()).thenThrow(new AssertionFailedError("setValue should not be called"));

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue();
            verify(targetProperty).setValue(0, "René");
            verify(targetProperty).setValue(1, "John");
        }

        @Test
        void copyIndexedPropertyByIndexTargetWritableArray() {
            IndexedProperty sourceProperty = createIndexedProperty(String.class, "names", "René", "John");
            when(sourceProperty.getPropertyDesc().isReadable()).thenReturn(false);
            when(sourceProperty.getValue()).thenThrow(new AssertionFailedError("getValue should not be called"));

            IndexedProperty targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");
            when(targetProperty.getPropertyDesc().isWritable()).thenReturn(true);

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue(0);
            verify(sourceProperty).getValue(1);
            verify(targetProperty).setValue(new String[]{"René", "John"});
        }

        @Test
        void copyIncreaseTempArray() {
            int size = 'z' - 'A' + 1;
            String[] sourceValues = new String[size];
            for (int i = 'A'; i <= 'z'; i++) {
                sourceValues[i - 'A'] = Character.toString((char) i);
            }

            IndexedProperty sourceProperty = createIndexedProperty(String.class, "names", sourceValues);
            when(sourceProperty.getPropertyDesc().isReadable()).thenReturn(false);
            when(sourceProperty.getValue()).thenThrow(new AssertionFailedError("getValue should not be called"));

            IndexedProperty targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");
            when(targetProperty.getPropertyDesc().isWritable()).thenReturn(true);

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue(0);
            verify(sourceProperty).getValue(1);
            verify(targetProperty).setValue(sourceValues);
        }

        @Test
        void copyTempEmptyArray() {

            IndexedProperty sourceProperty = createIndexedProperty(String.class, "names", new String[0]);
            when(sourceProperty.getPropertyDesc().isReadable()).thenReturn(false);
            when(sourceProperty.getValue()).thenThrow(new AssertionFailedError("getValue should not be called"));

            IndexedProperty targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");
            when(targetProperty.getPropertyDesc().isWritable()).thenReturn(true);

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue(new String[0]);
        }
    }

    @Nested
    class Adapted {

        private PropertyCopyStrategy strategy;

        @BeforeEach
        void setUp() {
            strategy = PropertyCopyStrategies.ADAPTED;
        }

        protected void copy(Property sourceProperty, Property targetProperty) {
            strategy.copy(sourceProperty, targetProperty);
        }

        @Test
        void indexedToNonIndexed() {
            IndexedProperty sourceProperty = createIndexedProperty(String.class, "firstname", "René");
            Property targetProperty = createProperty(String[].class, "firstname", new String[]{"John"});

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue();
            verify(targetProperty).setValue(new String[]{"René"});
        }

        @Test
        void indexedToNonIndexedUsingIndexGetter() {
            IndexedProperty sourceProperty = createIndexedProperty(String.class, "firstname", "René");
            PropertyDesc propertyDesc = sourceProperty.getPropertyDesc();
            doReturn(false).when(propertyDesc).isReadable();
            Property targetProperty = createProperty(String[].class, "firstname", new String[]{"John"});

            copy(sourceProperty, targetProperty);

            verify(sourceProperty).getValue(0);
            verify(targetProperty).setValue(new String[]{"René"});
        }

        @Test
        void nonIndexedToIndexed() {
            Property sourceProperty = createProperty(String.class, "firstname", "René");
            IndexedProperty targetProperty = createIndexedProperty(String.class, "firstname", "John");

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue(new String[]{"René"});
        }

        @Test
        void nonIndexedNullToIndexed() {
            Property sourceProperty = createProperty(String.class, "firstname", null);
            IndexedProperty targetProperty = createIndexedProperty(String.class, "firstname", "John");

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue(new String[]{null});
        }

        @Test
        void nonIndexedToIndexedUsingIndexSetter() {
            Property sourceProperty = createProperty(String.class, "firstname", "René");
            IndexedProperty targetProperty = createIndexedProperty(String.class, "firstname", "John");
            IndexedPropertyDesc targetPropertyDesc = targetProperty.getPropertyDesc();
            doReturn(false).when(targetPropertyDesc).isWritable();

            copy(sourceProperty, targetProperty);

            verify(targetProperty).setValue(0, "René");
        }

        @Test
        void nonWriteableTargetProperty() {
            Property sourceProperty = createIndexedProperty(String.class, "firstname", "René");
            Property targetProperty = createProperty(String[].class, "firstname", new String[]{"John"});
            PropertyDesc propertyDesc = targetProperty.getPropertyDesc();
            doReturn(false).when(propertyDesc).isWritable();

            assertThrows(IllegalArgumentException.class, () -> PropertyCopyStrategies.ADAPTED.copy(sourceProperty, targetProperty));
        }


    }
}