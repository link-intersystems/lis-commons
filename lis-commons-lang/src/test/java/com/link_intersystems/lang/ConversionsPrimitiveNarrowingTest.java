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
 * Tests the {@link Conversions} primitive narrowing as defined by the
 * <em>java language specification - 5.1.3 Narrowing Primitive Conversions</em>.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
class ConversionsPrimitiveNarrowingTest {

    @Test
    void nonPrimitiveFrom() {
        assertThrows(IllegalArgumentException.class, () -> Conversions.isPrimitiveNarrowing(Byte.class, Double.TYPE));
    }

    @Test
    void nonPrimitiveTo() {
        assertThrows(IllegalArgumentException.class, () -> Conversions.isPrimitiveNarrowing(Byte.TYPE, Double.class));
    }

    /**
     * byte narrowing not defined - smallest primitive type
     */
    @Test
    void byteNarrowing() {
        Class<?> from = Byte.TYPE;
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * short to byte or char
     */
    @Test
    void shortNarrowing() {
        Class<?> from = Short.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * char to byte or short
     */
    @Test
    void characterNarrowing() {
        Class<?> from = Character.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * int to byte, short, or char
     */
    @Test
    void integerNarrowing() {
        Class<?> from = Integer.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * long to byte, short, char, or int
     */
    @Test
    void longNarrowing() {
        Class<?> from = Long.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * float to byte, short, char, int, or long
     */
    @Test
    void floatNarrowing() {
        Class<?> from = Float.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }

    /**
     * double to byte, short, char, int, long, or float
     */
    @Test
    void doubleNarrowing() {
        Class<?> from = Double.TYPE;
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Byte.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Short.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Character.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Integer.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Long.TYPE));
        Assertions.assertTrue(Conversions.isPrimitiveNarrowing(from, Float.TYPE));
        Assertions.assertFalse(Conversions.isPrimitiveNarrowing(from, Double.TYPE));
    }
}
