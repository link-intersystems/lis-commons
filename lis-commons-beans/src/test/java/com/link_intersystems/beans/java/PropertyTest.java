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
package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.PropertyReadException;
import com.link_intersystems.beans.PropertyWriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JavaBeansExtension.class)
class PropertyTest extends AbstractPropertyTest<String> {


    private SomeBeanFixture someBeanFixture;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws IntrospectionException, BeanClassException {
        SomeBean someBean = new SomeBean();
        someBean.setStringProperty("HELLO");
        someBean.setStringArrayProperty(new String[]{"Hello", "World"});
        someBeanFixture = new SomeBeanFixture(beansFactory, someBean);
    }

    @Test
    void typeCache() {
        JavaProperty property = someBeanFixture.writeOnlyProperty;
        Class<?> type = property.getPropertyDesc().getType();
        Class<?> expectedType = getExpectedType();
        assertEquals(expectedType, type);
    }

    @Test
    void invocationTargetExceptionOnGet() {
        JavaProperty property = new JavaProperty(someBeanFixture.bean, new ExceptionThrowingPropertyOnAccess(someBeanFixture.stringPropertyDescriptor, new InvocationTargetException(new RuntimeException())));
        PropertyReadException propertyAccessException = assertThrows(PropertyReadException.class, property::getValue);
        assertPropertyAccessException(propertyAccessException, InvocationTargetException.class);
    }

    @Test
    void illeagalAccessExceptionOnGet() {
        JavaProperty property = new JavaProperty(someBeanFixture.bean, new ExceptionThrowingPropertyOnAccess(someBeanFixture.stringPropertyDescriptor, new IllegalAccessException()));
        PropertyReadException propertyAccessException = assertThrows(PropertyReadException.class, property::getValue);
        assertPropertyAccessException(propertyAccessException, IllegalAccessException.class);
    }

    @Test
    void invocationTargetExceptionOnSet() {
        JavaProperty property = new JavaProperty(someBeanFixture.bean, new ExceptionThrowingPropertyOnAccess(someBeanFixture.stringPropertyDescriptor, new InvocationTargetException(new RuntimeException())));
        PropertyWriteException propertyAccessException = assertThrows(PropertyWriteException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, InvocationTargetException.class);
    }

    @Test
    void isReadable() {
        JavaProperty writableOnly = someBeanFixture.writeOnlyProperty;
        assertFalse(writableOnly.isReadable());

        JavaProperty readOnly = someBeanFixture.readOnlyProperty;
        assertTrue(readOnly.isReadable());

        JavaProperty readWritable = someBeanFixture.stringProperty;
        assertTrue(readWritable.isReadable());
    }

    @Test
    void isWritable() {
        JavaProperty writableOnly = someBeanFixture.writeOnlyProperty;
        assertTrue(writableOnly.isWritable());

        JavaProperty readOnly = someBeanFixture.readOnlyProperty;
        assertFalse(readOnly.isWritable());

        JavaProperty readWritable = someBeanFixture.stringProperty;
        assertTrue(readWritable.isWritable());
    }

    @Test
    void illeagalAccessExceptionOnSet() {
        JavaProperty property = new JavaProperty(someBeanFixture.bean, new ExceptionThrowingPropertyOnAccess(someBeanFixture.stringPropertyDescriptor, new IllegalAccessException()));
        PropertyWriteException propertyAccessException = assertThrows(PropertyWriteException.class, () -> property.setValue(""));
        assertPropertyAccessException(propertyAccessException, IllegalAccessException.class);
    }

    @Test
    void toStringTest() {
        JavaProperty writableOnly = someBeanFixture.writeOnlyProperty;
        String toString = writableOnly.toString();
        assertEquals("writeOnlyProperty", toString);
    }

    @Override
    protected JavaProperty getReadOnlyProperty() {
        return someBeanFixture.readOnlyProperty;
    }

    @Override
    protected JavaProperty getWriteOnlyProperty() {
        return someBeanFixture.writeOnlyProperty;
    }

    @Override
    protected JavaProperty getProperty() {
        return someBeanFixture.stringProperty;
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

    private static class ExceptionThrowingPropertyOnAccess extends JavaPropertyDesc {

        private IllegalAccessException illegalAccessException;
        private InvocationTargetException invocationTargetException;

        ExceptionThrowingPropertyOnAccess(JavaPropertyDesc propertyDescriptor, IllegalAccessException illegalAccessException) {
            super(propertyDescriptor.getJavaPropertyDescriptor());
            this.illegalAccessException = illegalAccessException;
        }

        ExceptionThrowingPropertyOnAccess(JavaPropertyDesc propertyDescriptor, InvocationTargetException invocationTargetException) {
            super(propertyDescriptor.getJavaPropertyDescriptor());
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
