package com.link_intersystems.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BeansFacadeTest  {

    private Predicate<Method> propertyAccessorPredicate;

    @BeforeEach
    public void setup() {
        propertyAccessorPredicate = BeansFacade.getPropertyAccessorPredicate();
    }

    @Test
    void isPropertyAccessorGetMethod() throws NoSuchMethodException, SecurityException {
        boolean isPropertyAccessor = propertyAccessorPredicate.test(Component.class.getDeclaredMethod("getBackground"));
        assertTrue(isPropertyAccessor);
    }

    @Test
    void isPropertyAccessorSetMethod() throws NoSuchMethodException, SecurityException {
        boolean isPropertyAccessor = propertyAccessorPredicate.test(Component.class.getDeclaredMethod("setBackground", Color.class));
        assertTrue(isPropertyAccessor);
    }

}
