package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeansFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PropertyAccessorPredicateTest {

    @Test
    void isPropertyAccessor() throws NoSuchMethodException {
        assertTrue(PropertyAccessorPredicate.INSTANCE.test(Object.class.getDeclaredMethod("getClass")));
    }

    @Test
    void isNotPropertyAccessor() throws NoSuchMethodException {
        assertFalse(PropertyAccessorPredicate.INSTANCE.test(Object.class.getDeclaredMethod("toString")));
    }
}