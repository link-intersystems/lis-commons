package com.link_intersystems.beans.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.beans.PropertyDescriptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyNameResolverProxyTest  {

	public static interface SomeBeanInterface {

		public boolean isEnabled();

		public void setEnabled(boolean enabled);

		public String getTitle();

		public void setTitle(String title);

	}

	private PropertyNameResolverProxy<SomeBeanInterface> propertyNameResolverProxy;
	private SomeBeanInterface someBeanInterface;

	@BeforeEach
	public void setup() {
		propertyNameResolverProxy = new PropertyNameResolverProxy<>(SomeBeanInterface.class);
		someBeanInterface = propertyNameResolverProxy.createProxy();
	}

	private void assertLatestProperty(String propertyName) {
		PropertyDescriptor latestCallPropertyDescriptor = propertyNameResolverProxy.getLatestCallPropertyDescriptor();
		assertNotNull(latestCallPropertyDescriptor, "latestCallPropertyDescriptor");

		String name = latestCallPropertyDescriptor.getName();
		assertEquals(propertyName, name);
	}

	@Test
	void noProperyAccessed() {
		PropertyDescriptor latestCallPropertyDescriptor = propertyNameResolverProxy.getLatestCallPropertyDescriptor();
		assertNull(latestCallPropertyDescriptor, "latestCallPropertyDescriptor");
	}

	@Test
	void booleanGetter() {
		someBeanInterface.isEnabled();

		assertLatestProperty("enabled");
	}

	@Test
	void booleanSetter() {
		someBeanInterface.setEnabled(false);

		assertLatestProperty("enabled");
	}

	@Test
	void objectGetter() {
		someBeanInterface.getTitle();

		assertLatestProperty("title");
	}

	@Test
	void objectSetter() {
		someBeanInterface.setTitle("");

		assertLatestProperty("title");
	}

	@Test
	void latestPropertyDescriptorReturned() {
		someBeanInterface.setTitle("");
		someBeanInterface.setEnabled(true);

		assertLatestProperty("enabled");
	}

}