/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class AssertTest  {

    @Test
    void instantiable() {
        new Assert() {

        };
    }

    @Test
    void defaultIfNull() {
        String defaultIfNull = Assert.defaultIfNull("notNull", "null");
        assertEquals("notNull", defaultIfNull);

        defaultIfNull = Assert.defaultIfNull(null, "null");
        assertEquals("null", defaultIfNull);
    }

    @Test
    void defaultIfNullWithNullDefault() {
        assertThrows(IllegalArgumentException.class, () -> Assert.defaultIfNull("notNull", null));
    }

    @Test
    void defaultIfNullWithFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Assert.defaultIfNull("notNull", null));
    }

    @Test
    void notNull() {
        try {
            Assert.notNull("test", null);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.notNull("test", "");
    }

    @Test
    void notBlank() {
        try {
            Assert.notBlank("test", null);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        try {
            Assert.notBlank("test", "");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        try {
            Assert.notBlank("test", "   	");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.notBlank("test", "	e ");
    }

    @Test
    void greater() {
        try {
            Assert.greater("test", 1, 0);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.greater("test", 0, 1);
    }

    @Test
    void greaterOrEqualInt() {
        try {
            Assert.greaterOrEqual("test", 1, 0);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.greaterOrEqual("test", 1, 1);
    }

    @Test
    void greaterOrEqualDouble() {
        try {
            Assert.greaterOrEqual("test", 1.0001, 1.0);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.greaterOrEqual("test", 1.0, 1.0);
    }

    @Test
    void lower() {
        try {
            Assert.lower("test", 0, 1);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }
        Assert.lower("test", 1, 0);
    }

    @Test
    void lowerOrEqual() {
        try {
            Assert.lowerOrEqual("test", 1, 2);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.lowerOrEqual("test", 1, 1);
    }

    @Test
    void condition() {
        try {
            Assert.isTrue(false, "%s must be true", "condition");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("condition must be true", localizedMessage);
        }
        Assert.isTrue(true, "% must be true", "condition");
    }

    @Test
    void instanceOf() {
        try {
            Assert.instanceOf("test", new Object(), Serializable.class, String.class);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("test must be an instance of [interface java.io.Serializable, class java.lang.String]", localizedMessage);
        }
        try {
            Assert.instanceOf("test", null, Serializable.class, String.class);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("test must be an instance of [interface java.io.Serializable, class java.lang.String]", localizedMessage);
        }
        Assert.instanceOf("test", "", String.class);
    }

    @Test
    void sameClass() {
        try {
            Assert.sameClass("test", Object.class, Serializable.class);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("test must be class java.lang.Object, but was interface java.io.Serializable", localizedMessage);
        }
        Assert.sameClass("test", Serializable.class, Serializable.class);
    }

    @Test
    void notEqualInt() {
        try {
            Assert.notEqual("test", 1, 1);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.notEqual("test", 0, 1);
    }

    @Test
    void equalInt() {
        try {
            Assert.equal("test", 1, 0);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.equal("test", 1, 1);
    }

    @Test
    void equalObject() {
        try {
            Assert.equal("test", "test", "test2");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.equal("test", "test", "test");
    }

    @Test
    void equalObjectNullExpected() {
        try {
            Assert.equal("test", null, "");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.equal("test", null, null);
    }

    @Test
    void equalObjectNullValue() {
        try {
            Assert.equal("test", "test", null);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertTrue(localizedMessage.contains("test"));
        }

        Assert.equal("test", "test", "test");
    }
}
