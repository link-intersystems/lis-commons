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

class PrimitiveArrayCallbackTest {

    @Test
    void nullConstructorArg() {
        assertThrows(IllegalArgumentException.class, () -> new PrimitiveArrayCallback(null));
    }

    @Test
    void notAnArrayConstructorArg() {
        assertThrows(IllegalArgumentException.class, () -> new PrimitiveArrayCallback(new Object()));
    }

    @Test
    void notAnPrimitiveArrayConstructorArg() {
        assertThrows(IllegalArgumentException.class, () -> new PrimitiveArrayCallback(new Object[0]));
    }

    @Test
    void setIndexIsOutOfUpperBounds() {
        int[] arr = new int[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(arr);
        assertThrows(IndexOutOfBoundsException.class, () -> callback.setIndex(10));
    }

    @Test
    void setIndexIsOutOfLowerBounds() {
        int[] arr = new int[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(arr);
        assertThrows(IndexOutOfBoundsException.class, () -> callback.setIndex(-1));
    }

    @Test
    void getIndex() {
        int[] arr = new int[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(arr);
        callback.setIndex(6);
        int index = callback.getIndex();
        Assertions.assertEquals(6, index);

    }

    @Test
    void autoincrement() {
        int[] arr = new int[10];
        PrimitiveArrayCallback callback = new PrimitiveArrayCallback(arr);
        callback.setIndex(8);
        Assertions.assertEquals(0, arr[8]);
        callback.primitive(13);
        Assertions.assertEquals(13, arr[8]);
        callback.primitive(15);
        Assertions.assertEquals(15, arr[9]);
    }
}
