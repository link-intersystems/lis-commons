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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Conversions} boxing conversion as defined by the java
 * language specification 5.1.7 Boxing Conversion
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 */
class ConversionsBoxingTest {

    @Test
    void nonPrimitiveType() {
        Class<?> boxingConversion = Conversions
                .getBoxingConversion(Boolean.class);
        Assertions.assertNull(boxingConversion);
    }

    /**
     * From type boolean to type Boolean
     */
    @Test
    void booleanBoxing() {
        Class<?> primitive = boolean.class;
        Class<?> expectedWrapper = Boolean.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type byte to type Byte
     */
    @Test
    void byteBoxing() {
        Class<?> primitive = byte.class;
        Class<?> expectedWrapper = Byte.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type char to type Character
     */
    @Test
    void charBoxing() {
        Class<?> primitive = char.class;
        Class<?> expectedWrapper = Character.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type short to type Short
     */
    @Test
    void shortBoxing() {
        Class<?> primitive = short.class;
        Class<?> expectedWrapper = Short.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type int to type Integer
     */
    @Test
    void intBoxing() {
        Class<?> primitive = int.class;
        Class<?> expectedWrapper = Integer.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type long to type Long
     */
    @Test
    void longBoxing() {
        Class<?> primitive = long.class;
        Class<?> expectedWrapper = Long.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type float to type Float
     */
    @Test
    void floatBoxing() {
        Class<?> primitive = float.class;
        Class<?> expectedWrapper = Float.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    /**
     * From type double to type Double
     */
    @Test
    void doubleBoxing() {
        Class<?> primitive = double.class;
        Class<?> expectedWrapper = Double.class;
        boolean isBoxingConversion = Conversions.isBoxingConversion(primitive,
                expectedWrapper);
        Assertions.assertTrue(isBoxingConversion);
        Class<?> boxingConversion = Conversions.getBoxingConversion(primitive);
        Assertions.assertNotNull(boxingConversion);
        Assertions.assertEquals(expectedWrapper, boxingConversion);
        boolean boxingType = Conversions.isBoxingType(primitive);
        Assertions.assertTrue(boxingType);
    }

    @Test
    void isBoxingToNull() {
        boolean boxingConversion = Conversions.isBoxingConversion(
                Integer.class, null);
        Assertions.assertFalse(boxingConversion);
    }
}
