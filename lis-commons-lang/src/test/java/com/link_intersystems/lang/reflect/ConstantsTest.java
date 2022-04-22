package com.link_intersystems.lang.reflect;

import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
class ConstantsTest {

    private static class ConstantsTestValues {
        public static final String PUBLIC_CONSTANT = "PUBLIC_CONSTANT_VALUE";
        public static final String ANOTHER_PUBLIC_CONSTANT = "ANOTHER_PUBLIC_CONSTANT_VALUE";
        protected static final String PROTECTED_CONSTANT = "PROTECTED_CONSTANT_VALUE";
        static final String PACKAGE_PRIVATE_CONSTANT = "PACKAGE_PRIVATE_CONSTANT_VALUE";
        public static String PUBLIC_STATIC = "PUBLIC_STATIC_VALUE";
        public String PUBLIC = "PUBLIC_VALUE";
    }

    private Constants<String> testConstants;

    @BeforeEach
    void setUp() {
        testConstants = new Constants<>(ConstantsTestValues.class, String.class);
    }

    @Test
    void byName() {
        assertEquals("PUBLIC_CONSTANT_VALUE", testConstants.getValue("PUBLIC_CONSTANT"));
    }

    @Test
    void byNameNotExistent() {
        assertNull(testConstants.getValue("TEST"));
    }

    @Test
    void size() {
        assertEquals(2, testConstants.size());
    }

    @Test
    void get() {
        assertEquals("PUBLIC_CONSTANT_VALUE", testConstants.get(0));
        assertEquals("ANOTHER_PUBLIC_CONSTANT_VALUE", testConstants.get(1));
    }
}
