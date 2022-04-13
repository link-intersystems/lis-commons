package com.link_intersystems.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.Test;

class BeanClassTest  {

	@Test
	void getPropertyDescriptorWrongClassProperty() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(
				BeanClassTest.class.getDeclaredMethod("getPropertyDescriptorWrongClassProperty"));
		assertNull(propertyDescriptor, "propertyDescriptor");
	}

	@Test
	void getPropertyDescriptorByGetter() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass
				.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("getStringProperty"));
		assertNotNull(propertyDescriptor, "propertyDescriptor");
		assertEquals("stringProperty", propertyDescriptor.getName());
	}

	@Test
	void getPropertyDescriptorBySetter() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass
				.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("setStringProperty", String.class));
		assertNotNull(propertyDescriptor, "propertyDescriptor");
		assertEquals("stringProperty", propertyDescriptor.getName());
	}

	@Test
	void consistency() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		Map<String, PropertyDescriptor> propertyDescriptors = someBeanClass.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors.values()) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null) {
				PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescriptor(readMethod);
				assertSame(propertyDescriptor, propertyDescriptorByMethod);
			}

			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (writeMethod != null) {
				PropertyDescriptor propertyDescriptorByMethod = someBeanClass.getPropertyDescriptor(writeMethod);
				assertSame(propertyDescriptor, propertyDescriptorByMethod);
			}
	}
	}

}
