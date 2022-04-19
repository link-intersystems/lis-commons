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

class PrimitiveArrayCallbackForAllPrimitivesTest {

    @Test
    void intCallback() {
        int[] array = new int[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive(8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        int value = array[6];
        Assertions.assertEquals(8, value);
    }

    @Test
    void byteCallback() {
        byte[] array = new byte[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((byte) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        byte value = array[6];
        Assertions.assertEquals(8, value);
    }

    @Test
    void charCallback() {
        char[] array = new char[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((char) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        char value = array[6];
        Assertions.assertEquals(8, value);
    }

    @Test
    void shortCallback() {
        short[] array = new short[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((short) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        short value = array[6];
        Assertions.assertEquals(8, value);
    }

    @Test
    void longCallback() {
        long[] array = new long[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((long) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        long value = array[6];
        Assertions.assertEquals(8, value);
    }

    @Test
    void floatCallback() {
        float[] array = new float[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((float) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        float value = array[6];
        Assertions.assertEquals(8, value, 0.00000f);
    }

    @Test
    void doubleCallback() {
        double[] array = new double[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive((double) 8);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        double value = array[6];
        Assertions.assertEquals(8, value, 0.00000f);
    }

    @Test
    void booleanCallback() {
        boolean[] array = new boolean[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(array);
        callback.setAutoincrementEnabled(true);
        callback.setIndex(6);

        int index = callback.getIndex();
        callback.primitive(true);
        int indexAfterCallback = callback.getIndex();
        Assertions.assertEquals(index + 1, indexAfterCallback);
        boolean value = array[6];
        Assertions.assertEquals(true, value);
    }

}
