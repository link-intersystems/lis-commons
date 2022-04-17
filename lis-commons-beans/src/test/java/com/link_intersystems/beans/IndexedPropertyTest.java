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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.link_intersystems.beans.java.JavaBean;
import com.link_intersystems.beans.java.JavaIndexedProperty;
import com.link_intersystems.beans.java.JavaIndexedPropertyDesc;
import com.link_intersystems.beans.java.JavaProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexedPropertyTest extends AbstractPropertyTest <String[]> {

	private static final String[] STRING_ARRAY = new String[] { "Hello", "World" };
	private JavaIndexedProperty<String> readOnlyProperty;
	private JavaIndexedProperty<String> writeOnlyProperty;
	private JavaIndexedProperty<String> stringProperty;

	@BeforeEach
	public void setup() throws IntrospectionException {
		SomeBean someBean = new SomeBean() {
			{
				setStringArrayProperty(STRING_ARRAY);
			}
		};
		someBean.setStringArrayProperty(STRING_ARRAY);

		JavaBean<SomeBean> bean = new JavaBean<>(someBean);

		BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String name = propertyDescriptor.getName();
			if ("readOnlyIndexedProperty".equals(name)) {
				readOnlyProperty = new JavaIndexedProperty<>(bean, new JavaIndexedPropertyDesc<>((IndexedPropertyDescriptor) propertyDescriptor));
			} else if ("writeOnlyIndexedProperty".equals(name)) {
				writeOnlyProperty = new JavaIndexedProperty<>(bean, new JavaIndexedPropertyDesc<>((IndexedPropertyDescriptor) propertyDescriptor));
			} else if ("stringArrayProperty".equals(name)) {
				stringProperty = new JavaIndexedProperty<>(bean, new JavaIndexedPropertyDesc<>((IndexedPropertyDescriptor) propertyDescriptor));
			}
		}

	}

	@Test
	void getValueByIndex() {
		Object propertyValue = stringProperty.getValue(0);
		assertEquals(STRING_ARRAY[0], propertyValue);
	}

	@Test
	void setValueByIndex() {
		stringProperty.setValue(0, "test");
		Object propertyValue = stringProperty.getValue(0);
		assertEquals("test", propertyValue);
	}

	@Test
	void isIndexedReadable() {
		assertTrue(stringProperty.isIndexedReadable());
		assertTrue(readOnlyProperty.isIndexedReadable());
		assertFalse(writeOnlyProperty.isIndexedReadable());

	}

	@Test
	void isIndexedWriteable() {
		assertTrue(stringProperty.isIndexedWritable());
		assertTrue(writeOnlyProperty.isIndexedWritable());
		assertFalse(readOnlyProperty.isIndexedWritable());
	}

	@Override
	protected JavaProperty<String[]> getReadOnlyProperty() {
		return readOnlyProperty;
	}

	@Override
	protected JavaProperty<String[]> getWriteOnlyProperty() {
		return writeOnlyProperty;
	}

	@Override
	protected JavaProperty<String[]> getProperty() {
		return stringProperty;
	}

	@Override
	protected String getPropertyName() {
		return "stringArrayProperty";
	}

	@Override
	protected String[] getPropertySetValue() {
		return STRING_ARRAY;
	}

	@Override
	protected String[] getExpectedPropertyValue() {
		return STRING_ARRAY;
	}

}
