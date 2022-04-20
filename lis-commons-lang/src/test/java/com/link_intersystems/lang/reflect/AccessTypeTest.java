package com.link_intersystems.lang.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AccessTypeTest {

    private static class TestClass {
        private Object privateField;
        Object defaultField;
        protected Object protectedField;
        public Object publicField;
    }

    private Field privateField;
    private Field defaultField;
    private Field protectedField;
    private Field publicField;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        privateField = TestClass.class.getDeclaredField("privateField");
        defaultField = TestClass.class.getDeclaredField("defaultField");
        protectedField = TestClass.class.getDeclaredField("protectedField");
        publicField = TestClass.class.getDeclaredField("publicField");
    }

    @Test
    void privateAccessType() {
        AccessType accessType = AccessType.PRIVATE;

        assertTrue(accessType.isMatching(privateField.getModifiers()));

        assertFalse(accessType.isMatching(defaultField.getModifiers()));
        assertFalse(accessType.isMatching(protectedField.getModifiers()));
        assertFalse(accessType.isMatching(publicField.getModifiers()));
    }

    @Test
    void defaultAccessType() {
        AccessType accessType = AccessType.DEFAULT;

        assertTrue(accessType.isMatching(defaultField.getModifiers()));

        assertFalse(accessType.isMatching(privateField.getModifiers()));
        assertFalse(accessType.isMatching(protectedField.getModifiers()));
        assertFalse(accessType.isMatching(publicField.getModifiers()));
    }

    @Test
    void protectedAccessType() {
        AccessType accessType = AccessType.PROTECTED;

        assertTrue(accessType.isMatching(protectedField.getModifiers()));

        assertFalse(accessType.isMatching(privateField.getModifiers()));
        assertFalse(accessType.isMatching(defaultField.getModifiers()));
        assertFalse(accessType.isMatching(publicField.getModifiers()));
    }

    @Test
    void publicAccessType() {
        AccessType accessType = AccessType.PUBLIC;

        assertTrue(accessType.isMatching(publicField.getModifiers()));

        assertFalse(accessType.isMatching(privateField.getModifiers()));
        assertFalse(accessType.isMatching(defaultField.getModifiers()));
        assertFalse(accessType.isMatching(protectedField.getModifiers()));
    }

}