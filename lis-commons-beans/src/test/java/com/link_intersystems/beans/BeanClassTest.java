package com.link_intersystems.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

public class BeanClassTest {

	@Test
	public void getPropertyDescriptorWrongClassProperty() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass.getPropertyDescriptor(
				BeanClassTest.class.getDeclaredMethod("getPropertyDescriptorWrongClassProperty"));
		assertNull("propertyDescriptor", propertyDescriptor);
	}

	@Test
	public void getPropertyDescriptorByGetter() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass
				.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("getStringProperty"));
		assertNotNull("propertyDescriptor", propertyDescriptor);
		assertEquals("stringProperty", propertyDescriptor.getName());
	}

	@Test
	public void getPropertyDescriptorBySetter() throws NoSuchMethodException, SecurityException {
		BeanClass<SomeBean> someBeanClass = BeanClass.get(SomeBean.class);
		PropertyDescriptor propertyDescriptor = someBeanClass
				.getPropertyDescriptor(SomeBean.class.getDeclaredMethod("setStringProperty", String.class));
		assertNotNull("propertyDescriptor", propertyDescriptor);
		assertEquals("stringProperty", propertyDescriptor.getName());
	}

	@Test
	public void consistency() throws NoSuchMethodException, SecurityException {
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
