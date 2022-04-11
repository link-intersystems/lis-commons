package com.link_intersystems.beans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class BeansFacadeTest {

    private Predicate<Method> propertyAccessorPredicate;

    @Before
    public void setup() {
        propertyAccessorPredicate = BeansFacade.getPropertyAccessorPredicate();
    }

    @Test
    public void isPropertyAccessorGetMethod() throws NoSuchMethodException, SecurityException {
        boolean isPropertyAccessor = propertyAccessorPredicate.test(Component.class.getDeclaredMethod("getBackground"));
        Assert.assertTrue(isPropertyAccessor);
    }

    @Test
    public void isPropertyAccessorSetMethod() throws NoSuchMethodException, SecurityException {
        boolean isPropertyAccessor = propertyAccessorPredicate.test(Component.class.getDeclaredMethod("setBackground", Color.class));
        Assert.assertTrue(isPropertyAccessor);
    }

}
