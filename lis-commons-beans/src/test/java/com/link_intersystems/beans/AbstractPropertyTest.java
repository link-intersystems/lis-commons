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

import org.junit.Assert;
import org.junit.Test;

import com.link_intersystems.beans.PropertyAccessException.PropertyAccessType;
import com.link_intersystems.lang.reflect.Class2;

public abstract class AbstractPropertyTest<T> {

	protected abstract Property<T> getReadOnlyProperty();

	protected abstract Property<T> getWriteOnlyProperty();

	protected abstract Property<T> getProperty();

	protected abstract T getPropertySetValue();

	protected abstract T getExpectedPropertyValue();

	protected abstract String getPropertyName();

	protected final Class<?> getExpectedType() {
		Class2<?> thisClass = Class2.get(getClass());
		return thisClass.getBoundClass("T");
	}

	@Test
	public void typeForReadOnlyProperty() {
		Property<T> property = getReadOnlyProperty();
		Class<?> type = property.getType();
		Class<?> expectedType = getExpectedType();
		Assert.assertEquals(expectedType, type);
	}

	@Test
	public void typeForWriteOnlyProperty() {
		Property<T> property = getWriteOnlyProperty();
		Class<?> type = property.getType();
		Class<?> expectedType = getExpectedType();
		Assert.assertEquals(expectedType, type);
	}

	@Test
	public void name() {
		Property<T> property = getProperty();
		String name = property.getName();
		String propertyName = getPropertyName();
		Assert.assertEquals(propertyName, name);
	}

	@Test
	public void getValue() {
		Property<T> property = getProperty();
		Object propertyValue = property.getValue();
		T expected = getExpectedPropertyValue();

		boolean array = expected.getClass().isArray();
		if (array) {
			Assert.assertArrayEquals((Object[]) expected,
					(Object[]) propertyValue);
		} else {
			Assert.assertEquals(expected, propertyValue);
		}
	}

	@Test
	public void setValue() {
		Property<T> property = getProperty();
		T propertySetValue = getPropertySetValue();
		T value = property.getValue();
		Assert.assertFalse(propertySetValue.equals(value));
		property.setValue(propertySetValue);
		value = property.getValue();

		boolean array = propertySetValue.getClass().isArray();
		if (array) {
			Assert.assertArrayEquals((Object[]) propertySetValue,
					(Object[]) value);
		} else {
			Assert.assertEquals(propertySetValue, value);
		}
	}

	@Test(expected = PropertyAccessException.class)
	public void getWriteOnlyPropertyValue() {
		Property<T> property = getWriteOnlyProperty();
		property.getValue();
	}

	@Test(expected = PropertyAccessException.class)
	public void setReadOnlyPropertyValue() {
		Property<T> property = getReadOnlyProperty();
		T propertySetValue = getPropertySetValue();
		property.setValue(propertySetValue);
	}

	protected void assertPropertyAccessException(PropertyAccessException e,
			PropertyAccessType accessType, Class<? extends Throwable> causeType) {
		PropertyAccessType propertyAccessType = e.getPropertyAccessType();
		Assert.assertEquals(accessType, propertyAccessType);

		Class<?> beanType = e.getBeanType();
		Assert.assertEquals(SomeBean.class, beanType);

		String localizedMessage = e.getLocalizedMessage();
		Assert.assertNotNull(localizedMessage);

		Throwable cause = e.getCause();
		Assert.assertTrue(causeType.isInstance(cause));
	}

}