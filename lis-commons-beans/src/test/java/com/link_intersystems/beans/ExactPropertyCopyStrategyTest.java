package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static com.link_intersystems.beans.PropertyMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExactPropertyCopyStrategyTest {

    @Test
    void copyIndexedToNonIndexedProperty() {
        Property sourceProperty = createIndexedProperty(String.class, "firstname", "René");
        Property targetProperty = createProperty(String.class, "firstname", "John");

        assertThrows(IllegalArgumentException.class, () -> PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty));
    }

    @Test
    void copyNonIndexedToIndexedProperty() {
        Property sourceProperty = createProperty(String.class, "firstname", "René");
        Property targetProperty = createIndexedProperty(String.class, "firstname", "John");

        assertThrows(IllegalArgumentException.class, () -> PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty));
    }

    @Test
    void copyProperties() {
        Property sourceProperty = createProperty(String.class, "firstname", "René");
        Property targetProperty = createProperty(String.class, "firstname", "John");

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

        verify(targetProperty).setValue("René");
        verify(sourceProperty, never()).setValue(anyString());
    }


    @Test
    void copyIndexedPropertyByValue() {
        Property sourceProperty = createIndexedProperty(String.class, "names", "René", "John");
        Property targetProperty = createIndexedProperty(String.class, "names", "Link", "Doe");

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

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

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

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

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

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

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

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

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

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

        PropertyCopyStrategies.EXACT.copy(sourceProperty, targetProperty);

        verify(targetProperty).setValue(new String[0]);
    }
}