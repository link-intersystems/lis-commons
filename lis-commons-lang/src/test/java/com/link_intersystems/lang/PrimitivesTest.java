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

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

class PrimitivesTest  {

    @Test
    void subclassPrimitives() {
        new Primitives() {
        };

    }

    @Test
    void isPriitiveForNull() {
        boolean isPrimitive = Primitives.isPrimitive(null);
        assertFalse(isPrimitive);
    }

    @Test
    void autoboxingTypes() {
        Collection<Class<?>> primitiveTypes = Arrays.asList(new Class<?>[]{Integer.class, Integer.TYPE, Short.class, Short.TYPE, Byte.class, Byte.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE, Long.class, Long.TYPE, Character.class, Character.TYPE});
        for (Class<?> primitiveType : primitiveTypes) {
            assertTrue(Primitives.isAutoboxingType(primitiveType), "Type " + primitiveType.getName() + " must be recognized as autoboxing type");
        }
    }

    @Test
    void byteWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Short.class, Short.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void shortWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Short.class, Short.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Integer.class, Integer.TYPE, Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void charWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Character.class, Character.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Integer.class, Integer.TYPE, Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE, Short.class, Short.TYPE});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void intWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Integer.class, Integer.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE, Short.class, Short.TYPE, Character.class, Character.TYPE});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void longWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Long.class, Long.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Float.class, Float.TYPE, Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE, Short.class, Short.TYPE, Character.class, Character.TYPE, Integer.class, Integer.TYPE});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void floatWideningConversionAllowed() {
        Collection<Class<?>> fromClasses = Arrays.asList(new Class<?>[]{Float.class, Float.TYPE});
        Collection<Class<?>> allowedConversionTypes = Arrays.asList(new Class<?>[]{Double.class, Double.TYPE});
        Collection<Class<?>> notAllowedConversionTypes = Arrays.asList(new Class<?>[]{Byte.class, Byte.TYPE, Short.class, Short.TYPE, Character.class, Character.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE});
        verify(fromClasses, allowedConversionTypes, notAllowedConversionTypes);
    }

    @Test
    void noPrimitiveTypeConversion() {
        assertFalse(Primitives.isWideningConversionAllowed(String.class, Integer.class));

        assertFalse(Primitives.isWideningConversionAllowed(Integer.class, String.class));
    }

    @Test
    void getAutoboxingType() {
        Class<?> autoboxingType = null;
        /*
         * for object types
         */
        autoboxingType = Primitives.getAutoboxingType(Integer.class);
        assertEquals(Integer.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Byte.class);
        assertEquals(Byte.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Short.class);
        assertEquals(Short.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Double.class);
        assertEquals(Double.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Float.class);
        assertEquals(Float.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Character.class);
        assertEquals(Character.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Boolean.class);
        assertEquals(Boolean.class, autoboxingType);
        /*
         * for primitives
         */
        autoboxingType = Primitives.getAutoboxingType(Integer.TYPE);
        assertEquals(Integer.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Byte.TYPE);
        assertEquals(Byte.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Short.TYPE);
        assertEquals(Short.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Double.TYPE);
        assertEquals(Double.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Float.TYPE);
        assertEquals(Float.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Character.TYPE);
        assertEquals(Character.class, autoboxingType);
        autoboxingType = Primitives.getAutoboxingType(Boolean.TYPE);
        assertEquals(Boolean.class, autoboxingType);
    }

    @Test
    void getDefaultValues() {
        Object defaultValue = Primitives.getDefaultValue(boolean.class);
        assertEquals(false, defaultValue);
        defaultValue = Primitives.getDefaultValue(byte.class);
        assertEquals((byte) 0, defaultValue);
        defaultValue = Primitives.getDefaultValue(short.class);
        assertEquals((short) 0, defaultValue);
        defaultValue = Primitives.getDefaultValue(int.class);
        assertEquals(0, defaultValue);
        defaultValue = Primitives.getDefaultValue(long.class);
        assertEquals(0l, defaultValue);
        defaultValue = Primitives.getDefaultValue(float.class);
        assertEquals(0.0f, defaultValue);
        defaultValue = Primitives.getDefaultValue(double.class);
        assertEquals(0.0d, defaultValue);
        defaultValue = Primitives.getDefaultValue(char.class);
        assertEquals('\u0000', defaultValue);
    }

    @Test
    void getDefaultValueForNonPrimitiveType() {

        assertThrows(IllegalArgumentException.class, () -> Primitives.getDefaultValue(Object.class));
    }

    @Test
    void primitiveCallbackWithNonPrimitiveWrapper() {
        PrimitiveCallback primitiveCallback = createStrictMock(PrimitiveCallback.class);
        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveCallback("", primitiveCallback));
    }

    @Test
    void primitiveCallbackWithNullPrimitiveCallback() {
        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveCallback(Integer.valueOf(1), null));
    }

    @Test
    void primitiveCallbackWithNullPrimitiveWrapper() {
        PrimitiveCallback primitiveCallback = createStrictMock(PrimitiveCallback.class);
        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveCallback(null, primitiveCallback));
    }

    @Test
    void primitiveCallback() {
        PrimitiveCallback primitiveCallback = createStrictMock(PrimitiveCallback.class);

        Object primitiveObject = Byte.valueOf((byte) 1);
        primitiveCallback.primitive((byte) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Long.valueOf(1);
        primitiveCallback.primitive((long) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Short.valueOf((short) 1);
        primitiveCallback.primitive((short) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Integer.valueOf(1);
        primitiveCallback.primitive((int) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Double.valueOf(1);
        primitiveCallback.primitive((double) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Float.valueOf(1);
        primitiveCallback.primitive((float) 1);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Character.valueOf('a');
        primitiveCallback.primitive('a');
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);

        primitiveObject = Boolean.valueOf(true);
        primitiveCallback.primitive(true);
        expectLastCall();
        replay(primitiveCallback);
        Primitives.primitiveCallback(primitiveObject, primitiveCallback);
        EasyMock.verify(primitiveCallback);
        reset(primitiveCallback);
    }

    @Test
    void wrapperToPrimitiveArrayWithNullWrapperArray() {
        assertThrows(IllegalArgumentException.class, () -> Primitives.wrapperToPrimitiveArray(null));
    }

    @Test
    void wrapperToPrimitiveArrayWithNonWrapperArray() {
        assertThrows(IllegalArgumentException.class, () -> Primitives.wrapperToPrimitiveArray(new String[]{""}));
    }

    @Test
    void wrapperToPrimitiveArray() {
        int[] primitiveArray = Primitives.wrapperToPrimitiveArray(new Integer[]{1, 2, 3, 4, 5});

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, primitiveArray);
    }

    @Test
    void primitiveArrayWithNonPrimitiveArray() {

        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveToWrapperArray(new String[0]));
    }

    @Test
    void primitiveArrayWithNullPrimitiveArray() {
        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveToWrapperArray(null));
    }

    @Test
    void primitiveArrayWithNonArrayArg() {
        assertThrows(IllegalArgumentException.class, () -> Primitives.primitiveToWrapperArray(""));
    }

    @Test
    void primitiveArray() {
        Integer[] wrapperArray = Primitives.primitiveToWrapperArray(new int[]{1, 2, 3, 4, 5});

        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, wrapperArray);
    }

    protected void verify(Collection<Class<?>> fromClasses, Collection<Class<?>> allowedConversionTypes, Collection<Class<?>> notAllowedConversionTypes) {
        for (Class<?> fromClass : fromClasses) {
            for (Class<?> allowedConversionType : allowedConversionTypes) {
                assertTrue(Primitives.isWideningConversionAllowed(fromClass, allowedConversionType));
            }

            for (Class<?> notAllowedConversionType : notAllowedConversionTypes) {
                assertFalse(Primitives.isWideningConversionAllowed(fromClass, notAllowedConversionType));
            }
        }
    }

}
