package com.link_intersystems.beans;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Method;

import org.apache.commons.collections4.Predicate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeansFacadeTest {

	private Predicate<Method> propertyAccessorPredicate;

	@Before
	public void setup() {
		propertyAccessorPredicate = BeansFacade.getPropertyAccessorPredicate();
	}

	@Test
	public void isPropertyAccessorGetMethod() throws NoSuchMethodException, SecurityException {
		boolean isPropertyAccessor = propertyAccessorPredicate
				.evaluate(Component.class.getDeclaredMethod("getBackground"));
		Assert.assertTrue(isPropertyAccessor);
	}

	@Test
	public void isPropertyAccessorSetMethod() throws NoSuchMethodException, SecurityException {
		boolean isPropertyAccessor = propertyAccessorPredicate
				.evaluate(Component.class.getDeclaredMethod("setBackground", Color.class));
		Assert.assertTrue(isPropertyAccessor);
	}

}
