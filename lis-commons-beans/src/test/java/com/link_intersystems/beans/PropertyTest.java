/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.beans.PropertyAccessException.PropertyAccessType;

public class PropertyTest extends AbstractPropertyTest<String> {

	private SomeBean someBean;
	private PropertyDescriptor readOnlyProperty;
	private PropertyDescriptor writeOnlyProperty;
	private PropertyDescriptor stringProperty;

	@Before
	public void setup() throws IntrospectionException {
		someBean = new SomeBean();
		someBean.setStringProperty("HELLO");
		someBean.setStringArrayProperty(new String[] { "Hello", "World" });
		BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
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
	public void typeCache() {
		Property<String> property = new Property<String>(someBean,
				writeOnlyProperty);
		Class<?> type = property.getType();
		type = property.getType();
		Class<?> expectedType = getExpectedType();
		Assert.assertEquals(expectedType, type);
	}

	@Test(expected = PropertyAccessException.class)
	public void invocationTargetExceptionOnGet() {
		Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(
				someBean, stringProperty, new InvocationTargetException(
						new RuntimeException()));
		try {
			property.getValue();
		} catch (PropertyAccessException e) {
			assertPropertyAccessException(e, PropertyAccessType.READ,
					InvocationTargetException.class);
			throw e;
		}
	}

	@Test(expected = PropertyAccessException.class)
	public void illeagalAccessExceptionOnGet() {
		Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(
				someBean, stringProperty, new IllegalAccessException());
		try {
			property.getValue();
		} catch (PropertyAccessException e) {
			assertPropertyAccessException(e, PropertyAccessType.READ,
					IllegalAccessException.class);
			throw e;
		}
	}

	@Test(expected = PropertyAccessException.class)
	public void invocationTargetExceptionOnSet() {
		Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(
				someBean, stringProperty, new InvocationTargetException(
						new RuntimeException()));
		try {
			property.setValue("");
		} catch (PropertyAccessException e) {
			assertPropertyAccessException(e, PropertyAccessType.WRITE,
					InvocationTargetException.class);
			throw e;
		}
	}

	@Test
	public void isReadable() {
		Property<String> writableOnly = new Property<String>(someBean,
				writeOnlyProperty);
		assertFalse(writableOnly.isReadable());

		Property<String> readOnly = new Property<String>(someBean,
				readOnlyProperty);
		assertTrue(readOnly.isReadable());

		Property<String> readWritable = new Property<String>(someBean,
				stringProperty);
		assertTrue(readWritable.isReadable());
	}

	@Test
	public void isWritable() {
		Property<String> writableOnly = new Property<String>(someBean,
				writeOnlyProperty);
		assertTrue(writableOnly.isWritable());

		Property<String> readOnly = new Property<String>(someBean,
				readOnlyProperty);
		assertFalse(readOnly.isWritable());

		Property<String> readWritable = new Property<String>(someBean,
				stringProperty);
		assertTrue(readWritable.isWritable());
	}

	@Test(expected = PropertyAccessException.class)
	public void illeagalAccessExceptionOnSet() {
		Property<String> property = new ExceptionThrowingPropertyOnAccess<String>(
				someBean, stringProperty, new IllegalAccessException());
		try {
			property.setValue("");
		} catch (PropertyAccessException e) {
			assertPropertyAccessException(e, PropertyAccessType.WRITE,
					IllegalAccessException.class);
			throw e;
		}
	}

	@Test
	public void toStringTest() {
		Property<String> writableOnly = new Property<String>(someBean,
				writeOnlyProperty);
		String toString = writableOnly.toString();
		assertEquals("writeOnlyProperty", toString);
	}

	@Override
	protected Property<String> getReadOnlyProperty() {
		Property<String> property = new Property<String>(someBean,
				readOnlyProperty);
		return property;
	}

	@Override
	protected Property<String> getWriteOnlyProperty() {
		Property<String> property = new Property<String>(someBean,
				writeOnlyProperty);
		return property;
	}

	@Override
	protected Property<String> getProperty() {
		Property<String> property = new Property<String>(someBean,
				stringProperty);
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

	private static class ExceptionThrowingPropertyOnAccess<T> extends
			Property<T> {

		/**
		 *
		 */
		private static final long serialVersionUID = -2230066673757778170L;
		private IllegalAccessException illegalAccessException;
		private InvocationTargetException invocationTargetException;

		ExceptionThrowingPropertyOnAccess(Object bean,
				PropertyDescriptor propertyDescriptor,
				IllegalAccessException illegalAccessException) {
			super(bean, propertyDescriptor);
			this.illegalAccessException = illegalAccessException;
		}

		ExceptionThrowingPropertyOnAccess(Object bean,
				PropertyDescriptor propertyDescriptor,
				InvocationTargetException invocationTargetException) {
			super(bean, propertyDescriptor);
			this.invocationTargetException = invocationTargetException;
		}

		@Override
		protected Object invoke(Method method, Object target, Object... args)
				throws IllegalAccessException, InvocationTargetException {
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
