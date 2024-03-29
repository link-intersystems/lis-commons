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

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility class for handling primitive types.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
public abstract class Primitives {

    private static final Map<Class<?>, Object> DEFAULT_VALUES;

    static {
        HashMap<Class<?>, Object> defaultValues = new HashMap<>();
        defaultValues.put(Integer.TYPE, 0);
        defaultValues.put(Boolean.TYPE, false);
        defaultValues.put(Long.TYPE, 0L);
        defaultValues.put(Short.TYPE, (short) 0);
        defaultValues.put(Float.TYPE, 0f);
        defaultValues.put(Character.TYPE, '\u0000');
        defaultValues.put(Byte.TYPE, (byte) 0);
        defaultValues.put(Double.TYPE, 0d);
        DEFAULT_VALUES = Collections.unmodifiableMap(defaultValues);
    }

    public static final Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap<>();

    static {
        primitiveToWrapper.put(boolean.class, Boolean.class);
        primitiveToWrapper.put(byte.class, Byte.class);
        primitiveToWrapper.put(short.class, Short.class);
        primitiveToWrapper.put(char.class, Character.class);
        primitiveToWrapper.put(int.class, Integer.class);
        primitiveToWrapper.put(long.class, Long.class);
        primitiveToWrapper.put(float.class, Float.class);
        primitiveToWrapper.put(double.class, Double.class);
    }

    public static Class<?> getWrapperType(Class<?> primitiveType) {
        return primitiveToWrapper.get(primitiveType);
    }


    public static Class<?> getPrimitiveType(Class<?> primitiveWrapper) {
        for (Map.Entry<Class<?>, Class<?>> primitiveToWrapper : primitiveToWrapper.entrySet()) {
            if (primitiveToWrapper.getValue().equals(primitiveWrapper)) {
                return primitiveToWrapper.getKey();
            }
        }
        return null;
    }


    public static boolean isAutoboxingType(Class<?> type) {
        return getAutoboxingType(type) != null;
    }

    /**
     * @param type
     * @return the corresponding autoboxing type of parameter type. Returns
     * null, if parameter type is neither a primitive nor a primitive
     * wrapper type. If the parameter is already an autoboxing type
     * (e.g. Integer) it is returned.
     * @since 1.0.0;
     */
    public static Class<?> getAutoboxingType(Class<?> type) {
        if (isPrimitive(type)) {
            return Conversions.getBoxingConversion(type);
        } else if (isPrimitiveWrapper(type)) {
            return type;
        } else {
            return null;
        }
    }

    /**
     * @param type
     * @return true if the parameter type is a primitive type (boolean, byte,
     * short, char, int, long, float or double).
     * @since 1.0.0;
     */
    public static boolean isPrimitive(Class<?> type) {
        if (type == null) {
            return false;
        } else {
            return type.isPrimitive();
        }
    }

    /**
     * @param type
     * @return true if the parameter type is a primitive wrapper type (Boolean,
     * Byte, Short, Character, Integer, Long, Float or Double).
     * @since 1.0.0;
     */
    public static boolean isPrimitiveWrapper(Class<?> type) {
        return Conversions.getUnboxingConversion(type) != null;
    }

    public static boolean isWideningConversionAllowed(Class<?> type,
                                                      Class<?> typeToConvertTo) {
        Class<?> from = type;
        Class<?> to = typeToConvertTo;

        if (!Conversions.isBoxingType(from) || !Conversions.isBoxingType(to)) {
            return false;
        }

        if (isPrimitiveWrapper(from)) {
            from = Conversions.getUnboxingConversion(from);
        }
        if (isPrimitiveWrapper(to)) {
            to = Conversions.getUnboxingConversion(to);
        }

        return Conversions.isPrimitiveWidening(from, to);
    }

    /**
     * <table summary="Default values for primitive types" border="1">
     * <tbody>
     * <tr>
     * <th id="table_dvpt_datatype" align="LEFT"><strong>Data Type</strong></th>
     * <th id="table_dvpt_defaultvalue" align="LEFT"><strong>Default Value (for
     * fields)</strong></th>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">byte</td>
     * <td headers="table_dvpt_defaultvalue">0</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">short</td>
     * <td headers="table_dvpt_defaultvalue">0</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">int</td>
     * <td headers="table_dvpt_defaultvalue">0</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">long</td>
     * <td headers="table_dvpt_defaultvalue">0L</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">float</td>
     * <td headers="table_dvpt_defaultvalue">0.0f</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">double</td>
     * <td headers="table_dvpt_defaultvalue">0.0d</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">char</td>
     * <td headers="table_dvpt_defaultvalue">'\u0000'</td>
     * </tr>
     * <tr>
     * <td headers="table_dvpt_datatype">boolean</td>
     * <td headers="table_dvpt_defaultvalue">false</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param <T>
     * @param primitiveType
     * @return the default value for the given primitive type.
     * @throws IllegalArgumentException if the given primitiveType argument is not a primitive type
     *                                  in terms of the {@link #isPrimitive(Class)} method.
     * @since 1.0.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDefaultValue(Class<?> primitiveType) {
        if (!isPrimitive(primitiveType)) {
            throw new IllegalArgumentException(
                    "primitiveType is not a primitive type.");
        }
        return (T) DEFAULT_VALUES.get(primitiveType);
    }

    /**
     * Calls the appropriated {@link PrimitiveCallback} method for the
     * primitiveWrapper object.
     *
     * @param primitiveWrapper  a primitive wrapper object like {@link Byte}, {@link Integer}
     *                          and so on.
     * @param primitiveCallback the callback to call for the primitive wrapper type.
     * @since 1.2.0;
     */
    public static void primitiveCallback(Object primitiveWrapper, PrimitiveCallback primitiveCallback) {
        Class<?> primitiveWrapperClass = primitiveWrapper.getClass();
        if (!Primitives.isPrimitiveWrapper(primitiveWrapperClass)) {
            throw new IllegalArgumentException(
                    "Argument primitiveWrapper must be a primitive wrapper type");
        }
        if (Integer.class.equals(primitiveWrapperClass)) {
            Integer primitiveWrapperCasted = (Integer) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Long.class.equals(primitiveWrapperClass)) {
            Long primitiveWrapperCasted = (Long) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Double.class.equals(primitiveWrapperClass)) {
            Double primitiveWrapperCasted = (Double) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Character.class.equals(primitiveWrapperClass)) {
            Character primitiveWrapperCasted = (Character) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Boolean.class.equals(primitiveWrapperClass)) {
            Boolean primitiveWrapperCasted = (Boolean) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Byte.class.equals(primitiveWrapperClass)) {
            Byte primitiveWrapperCasted = (Byte) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else if (Float.class.equals(primitiveWrapperClass)) {
            Float primitiveWrapperCasted = (Float) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        } else {
            Short primitiveWrapperCasted = (Short) primitiveWrapper;
            primitiveCallback.primitive(primitiveWrapperCasted);
        }
    }

    /**
     * Converts a primitive wrapper array to the appropriated primitive array
     * according to the primitive wrapper array's component type.</br>
     *
     * <pre>
     * Byte[] primitiveWrapperArray = new Byte[] { 1, 2, 3, 4 };
     *
     * byte[] primitiveArray = Primitives
     * 		.wrapperToPrimitiveArray(primitiveWrapperArray);
     * </pre>
     *
     * @param primitiveWrapperArray
     * @return the appropriated primitive array for the primitive wrapper array.
     * Casts must respect the primitive wrapper array's component type.
     * Due to the fact that generic definitions do not allow primitive
     * types the return type can not be constraint further.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrapperToPrimitiveArray(Object[] primitiveWrapperArray) {
        Class<?> primitiveWrapperArrayClass = primitiveWrapperArray.getClass();
        Class<?> primitiveWrapperComponentType = primitiveWrapperArrayClass
                .getComponentType();
        boolean isPrimitiveWrapperArray = Primitives
                .isPrimitiveWrapper(primitiveWrapperComponentType);

        Assert.isTrue(isPrimitiveWrapperArray,
                "primitiveWrapperArray's component type is not a primitive: %",
                primitiveWrapperComponentType);

        Class<?> primitiveArrayComponentType = Conversions
                .getUnboxingConversion(primitiveWrapperComponentType);
        Object primitiveArray = Array.newInstance(primitiveArrayComponentType,
                primitiveWrapperArray.length);
        PrimitiveArrayCallback primitiveArrayCallback = new PrimitiveArrayCallback(
                primitiveArray);
        primitiveArrayCallback.setAutoincrementEnabled(false);
        for (int i = 0; i < primitiveWrapperArray.length; i++) {
            Object primitiveWrapper = primitiveWrapperArray[i];
            primitiveArrayCallback.setIndex(i);
            primitiveCallback(primitiveWrapper, primitiveArrayCallback);
        }
        return (T) primitiveArray;
    }

    /**
     * Converts a primitive array to the appropriated primitive wrapper array
     * according to the primitive array's component type.</br>
     *
     * <pre>
     * byte[] primitiveArray = new byte[] { 1, 2, 3, 4 };
     *
     * Byte[] primitiveArray = Primitives.primitiveToWrapperArray(primitiveArray);
     * </pre>
     *
     * @param primitiveArray
     * @return the appropriated primitive wrapper array for the primitive array.
     * Casts must respect the primitive array's component type.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] primitiveToWrapperArray(Object primitiveArray) {
        Class<?> primitiveArrayClass = primitiveArray.getClass();
        if (!primitiveArrayClass.isArray()) {
            throw new IllegalArgumentException(
                    "primitiveArray must be a primitive array, but was "
                            + primitiveArrayClass);
        }
        Class<?> primitiveComponentType = primitiveArrayClass
                .getComponentType();
        if (!Primitives.isPrimitive(primitiveComponentType)) {
            throw new IllegalArgumentException(
                    "primitiveArray's component type is not a primitive type: "
                            + primitiveComponentType);
        }

        Class<?> wrapperType = Conversions
                .getBoxingConversion(primitiveComponentType);
        int length = Array.getLength(primitiveArray);
        Object[] wrapperArray = (Object[]) Array.newInstance(wrapperType,
                length);

        for (int i = 0; i < length; i++) {
            Object primitiveObject = Array.get(primitiveArray, i);
            wrapperArray[i] = primitiveObject;
        }
        return (T[]) wrapperArray;

    }

}
