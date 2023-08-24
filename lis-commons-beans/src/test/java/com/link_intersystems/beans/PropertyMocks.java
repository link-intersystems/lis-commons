package com.link_intersystems.beans;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class PropertyMocks {
    public static <T> Property createProperty(Class<T> type, String name, T value) {
        Property property = mock(Property.class);
        PropertyDesc propDesc = createPropertyDesc((Class) type, name);
        when(property.getValue()).thenReturn(value);
        when(property.getPropertyDesc()).thenReturn(propDesc);
        return property;
    }

    public static <T> PropertyDesc createPropertyDesc(Class type, String name) {
        PropertyDesc propDesc = mock(PropertyDesc.class);
        setDefaultValue(propDesc, type, name);
        return propDesc;
    }


    public static <T> IndexedProperty createIndexedProperty(Class<T> type, String name, T... values) {
        IndexedProperty indexedProperty = mock(IndexedProperty.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String methodName = invocation.getMethod().getName();
                if ("getValue".equals(methodName) && invocation.getMethod().getParameterTypes().length == 1) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                return null;
            }
        });
        doReturn(values).when(indexedProperty).getValue();
        for (int i = 0; i < values.length; i++) {
            doReturn(values[i]).when(indexedProperty).getValue(i);
        }
        IndexedPropertyDesc propertyDesc = createIndexedPropertyDesc(type, name);
        doReturn(propertyDesc).when(indexedProperty).getPropertyDesc();
        return indexedProperty;
    }

    public static <T> IndexedPropertyDesc createIndexedPropertyDesc(Class type, String name) {
        IndexedPropertyDesc propDesc = mock(IndexedPropertyDesc.class);
        setDefaultValue(propDesc, type, name);
        when(propDesc.isIndexedReadable()).thenReturn(true);
        when(propDesc.isIndexedWritable()).thenReturn(true);
        return propDesc;
    }

    private static void setDefaultValue(PropertyDesc propDesc, Class type, String name) {
        when(propDesc.getType()).thenReturn(type);
        when(propDesc.getName()).thenReturn(name);
        when(propDesc.isReadable()).thenReturn(true);
        when(propDesc.isWritable()).thenReturn(true);
    }
}
