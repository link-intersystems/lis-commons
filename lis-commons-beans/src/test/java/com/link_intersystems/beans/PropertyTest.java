/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans;

import com.link_intersystems.beans.PropertyAccessException.PropertyAccessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest extends AbstractPropertyTest <String> {

    private Bean<SomeBean> bean;
    private PropertyDescriptor readOnlyProperty;
    private PropertyDescriptor writeOnlyProperty;
    private PropertyDescriptor stringProperty;

    @BeforeEach
    public void setup() throws IntrospectionException {
        SomeBean someBean = new SomeBean();
        someBean.setStringProperty("HELLO");
        someBean.setStringArrayProperty(new String[]{"Hello", "World"});

        bean = new Bean<>(someBean);

        BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();
            if ("readOnlyProperty".equals(name)) {
                readOnlyProperty = propertyDescriptor;
            } else if ("writeOnlyProperty".equals(name)) {
                writeOnlyProperty = propertyDescriptor;
            } else if ("stringProperty".equals(name)) {
                stringProperty = propertyDescriptor;
            }
        }
    }

    @Test
    void typeCache() {
        Property<String> property = new Property<String>(bean, writeOnlyProperty);
        Class<?> type = property.getType();
        type = property.getType();
        Class<?> expectedType = getExpectedType();
        assertEquals(expectedType, type);
    }

    @Test
    void invocationTargetExceptionOnGet() {
        Property<String> property = new ExceptionThrowingPropertyOnAccess<>(bean, stringProperty, new InvocationTargetException(new RuntimeException()));

        PropertyAccessException propertyAccessException = assertThrows(PropertyAccessException.class, () -> property.getValue());
        assertPropertyAccessException(propertyAccessException, PropertyAccessType.READ, InvocationTargetException.class);
    }

    @Test
    void illeagalAccessExceptionOnGet() {
        Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, stringProperty, new IllegalAccessException());
        PropertyAccessException propertyAccessException = assertThrows(PropertyAccessException.class, () -> property.getValue());
        assertPropertyAccessException(propertyAccessException, PropertyAccessType.READ, IllegalAccessException.class);
    }

    @Test
    void invocationTargetExceptionOnSet() {
        Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, stringProperty, new InvocationTargetException(new RuntimeException()));
        PropertyAccessException propertyAccessException = assertThrows(PropertyAccessException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, PropertyAccessType.WRITE, InvocationTargetException.class);
    }

    @Test
    void isReadable() {
        Property<String> writableOnly = new Property<String>(bean, writeOnlyProperty);
        assertFalse(writableOnly.isReadable());

        Property<String> readOnly = new Property<String>(bean, readOnlyProperty);
        assertTrue(readOnly.isReadable());

        Property<String> readWritable = new Property<String>(bean, stringProperty);
        assertTrue(readWritable.isReadable());
    }

    @Test
    void isWritable() {
        Property<String> writableOnly = new Property<String>(bean, writeOnlyProperty);
        assertTrue(writableOnly.isWritable());

        Property<String> readOnly = new Property<String>(bean, readOnlyProperty);
        assertFalse(readOnly.isWritable());

        Property<String> readWritable = new Property<String>(bean, stringProperty);
        assertTrue(readWritable.isWritable());
    }

    @Test
    void illeagalAccessExceptionOnSet() {
        Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, stringProperty, new IllegalAccessException());
        PropertyAccessException propertyAccessException = assertThrows(PropertyAccessException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, PropertyAccessType.WRITE, IllegalAccessException.class);
    }

    @Test
    void toStringTest() {
        Property<String> writableOnly = new Property<String>(bean, writeOnlyProperty);
        String toString = writableOnly.toString();
        assertEquals("writeOnlyProperty", toString);
    }

    @Override
    protected Property<String> getReadOnlyProperty() {
        Property<String> property = new Property<String>(bean, readOnlyProperty);
        return property;
    }

    @Override
    protected Property<String> getWriteOnlyProperty() {
        Property<String> property = new Property<String>(bean, writeOnlyProperty);
        return property;
    }

    @Override
    protected Property<String> getProperty() {
        Property<String> property = new Property<String>(bean, stringProperty);
        return property;
    }

    @Override
    protected String getPropertyName() {
        return "stringProperty";
    }

    @Override
    protected String getPropertySetValue() {
        return "HELLO TEST";
    }

    @Override
    protected String getExpectedPropertyValue() {
        return "HELLO";
    }

    private static class ExceptionThrowingPropertyOnAccess<T> extends Property<T> {

        /**
         *
         */
        private static final long serialVersionUID = -2230066673757778170L;
        private IllegalAccessException illegalAccessException;
        private InvocationTargetException invocationTargetException;

        ExceptionThrowingPropertyOnAccess(Bean<?> bean, PropertyDescriptor propertyDescriptor, IllegalAccessException illegalAccessException) {
            super(bean, propertyDescriptor);
            this.illegalAccessException = illegalAccessException;
        }

        ExceptionThrowingPropertyOnAccess(Bean<?> bean, PropertyDescriptor propertyDescriptor, InvocationTargetException invocationTargetException) {
            super(bean, propertyDescriptor);
            this.invocationTargetException = invocationTargetException;
        }

        @Override
        protected Object invoke(Method method, Object target, Object... args) throws IllegalAccessException, InvocationTargetException {
            if (illegalAccessException != null) {
                throw illegalAccessException;
            }
            if (invocationTargetException != null) {
                throw invocationTargetException;
            }
            return super.invoke(method, target, args);
        }

    }
}
