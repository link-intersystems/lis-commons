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

import com.link_intersystems.beans.java.JavaBean;
import com.link_intersystems.beans.java.JavaProperty;
import com.link_intersystems.beans.java.JavaPropertyDesc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest extends AbstractPropertyTest<String> {

    private JavaBean<SomeBean> bean;
    private PropertyDescriptor readOnlyProperty;
    private PropertyDescriptor writeOnlyProperty;
    private PropertyDescriptor stringProperty;

    @BeforeEach
    public void setup() throws IntrospectionException {
        SomeBean someBean = new SomeBean();
        someBean.setStringProperty("HELLO");
        someBean.setStringArrayProperty(new String[]{"Hello", "World"});

        bean = new JavaBean<>(someBean);

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
        JavaProperty<String> property = createJavaProperty(writeOnlyProperty);
        Class<?> type = property.getType();
        type = property.getType();
        Class<?> expectedType = getExpectedType();
        assertEquals(expectedType, type);
    }

    private JavaProperty<String> createJavaProperty(PropertyDescriptor property) {
        return new JavaProperty<>(bean, new JavaPropertyDesc<>(property));
    }

    @Test
    void invocationTargetExceptionOnGet() {
        JavaProperty<String> javaProperty = createJavaProperty(stringProperty);
        JavaProperty<String> property = new ExceptionThrowingPropertyOnAccess<>(bean, javaProperty.getPropertyDescriptor(), new InvocationTargetException(new RuntimeException()));

        PropertyReadException propertyAccessException = assertThrows(PropertyReadException.class, () -> property.getValue());
        assertPropertyAccessException(propertyAccessException, InvocationTargetException.class);
    }

    @Test
    void illeagalAccessExceptionOnGet() {
        JavaProperty<String> javaProperty = createJavaProperty(stringProperty);
        JavaProperty<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, javaProperty.getPropertyDescriptor(), new IllegalAccessException());
        PropertyReadException propertyAccessException = assertThrows(PropertyReadException.class, () -> property.getValue());
        assertPropertyAccessException(propertyAccessException, IllegalAccessException.class);
    }

    @Test
    void invocationTargetExceptionOnSet() {
        JavaProperty<String> javaProperty = createJavaProperty(stringProperty);
        JavaProperty<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, javaProperty.getPropertyDescriptor(), new InvocationTargetException(new RuntimeException()));
        PropertyWriteException propertyAccessException = assertThrows(PropertyWriteException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, InvocationTargetException.class);
    }

    @Test
    void isReadable() {
        JavaProperty<String> writableOnly = createJavaProperty(writeOnlyProperty);
        assertFalse(writableOnly.isReadable());

        JavaProperty<String> readOnly = createJavaProperty(readOnlyProperty);
        assertTrue(readOnly.isReadable());

        JavaProperty<String> readWritable = createJavaProperty(stringProperty);
        assertTrue(readWritable.isReadable());
    }

    @Test
    void isWritable() {
        JavaProperty<String> writableOnly = createJavaProperty(writeOnlyProperty);
        assertTrue(writableOnly.isWritable());

        JavaProperty<String> readOnly = createJavaProperty(readOnlyProperty);
        assertFalse(readOnly.isWritable());

        JavaProperty<String> readWritable = createJavaProperty(stringProperty);
        assertTrue(readWritable.isWritable());
    }

    @Test
    void illeagalAccessExceptionOnSet() {
        JavaProperty<String> javaProperty = createJavaProperty(stringProperty);
        JavaProperty<String> property = new ExceptionThrowingPropertyOnAccess<String>(bean, javaProperty.getPropertyDescriptor(), new IllegalAccessException());
        PropertyWriteException propertyAccessException = assertThrows(PropertyWriteException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, IllegalAccessException.class);
    }

    @Test
    void toStringTest() {
        JavaProperty<String> writableOnly = createJavaProperty(writeOnlyProperty);
        String toString = writableOnly.toString();
        assertEquals("writeOnlyProperty", toString);
    }

    @Override
    protected JavaProperty<String> getReadOnlyProperty() {
        JavaProperty<String> property = createJavaProperty(readOnlyProperty);
        return property;
    }

    @Override
    protected JavaProperty<String> getWriteOnlyProperty() {
        JavaProperty<String> property = createJavaProperty(writeOnlyProperty);
        return property;
    }

    @Override
    protected JavaProperty<String> getProperty() {
        JavaProperty<String> property = createJavaProperty(stringProperty);
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

    private static class ExceptionThrowingPropertyOnAccess<T> extends JavaProperty<T> {

        /**
         *
         */
        private static final long serialVersionUID = -2230066673757778170L;
        private IllegalAccessException illegalAccessException;
        private InvocationTargetException invocationTargetException;

        ExceptionThrowingPropertyOnAccess(JavaBean<?> bean, JavaPropertyDesc propertyDescriptor, IllegalAccessException illegalAccessException) {
            super(bean, propertyDescriptor);
            this.illegalAccessException = illegalAccessException;
        }

        ExceptionThrowingPropertyOnAccess(JavaBean<?> bean, JavaPropertyDesc propertyDescriptor, InvocationTargetException invocationTargetException) {
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
