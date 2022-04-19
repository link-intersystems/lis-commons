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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Conversions} primitive widening as defined by the java
 * language specification 5.1.2 Widening Primitive Conversion
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
class ConversionsPrimitiveWideningTest {

    @Test
    void nonPrimitiveFrom() {
        assertThrows(IllegalArgumentException.class, () -> Conversions.isPrimitiveWidening(Byte.class, Double.TYPE));
    }

    @Test
    void nonPrimitiveTo() {
        assertThrows(IllegalArgumentException.class, () -> Conversions.isPrimitiveWidening(Byte.TYPE, Double.class));
    }

    /**
     * byte to short, int, long, float, or double
     */
    @Test
    void byteWidening() {
        Class<?> from = Byte.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * short to int, long, float, or double
     */
    @Test
    void shortWidening() {
        Class<?> from = Short.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * char to int, long, float, or double
     */
    @Test
    void characterWidening() {
        Class<?> from = Character.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * int to long, float, or double
     */
    @Test
    void integerWidening() {
        Class<?> from = Integer.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * long to float or double
     */
    @Test
    void longWidening() {
        Class<?> from = Long.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * float to double
     */
    @Test
    void floatWidening() {
        Class<?> from = Float.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }

    /**
     * double widening is not supported because it is the biggest primitive
     * type.
     */
    @Test
    void doubleWidening() {
        Class<?> from = Double.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveWidening(from, Double.TYPE));
    }
}
