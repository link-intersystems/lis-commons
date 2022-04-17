/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang;

import java.util.Objects;

/**
 * Encapsulates the conversion logic as defined by the <em>java language
 * specification - CHAPTER 5 Conversions and Promotions</em>.
 *
 * Conversions implemented.
 * <ul>
 * <li>Identity conversions</li>
 * <li>Widening primitive conversions</li>
 * <li>Narrowing primitive conversions</li>
 * <li>Widening reference conversions</li>
 * <li>Narrowing reference conversions</li>
 * <li>Boxing conversions</li>
 * <li>Unboxing conversions</li>
 * </ul>
 *
 * <strong>note</strong> Generics are not supported at the moment.
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0;
 */
public abstract class Conversions {

	private static final String PARAMETER_MUST_BE_A_PRIMITIVE = "%s must be a primitive";

	/**
	 * <em>java language specification - 5.1.1 Identity Conversions</em>.
	 * <blockquote> A conversion from a type to that same type is permitted for any
	 * type. </blockquote>
	 *
	 * @param from
	 * @param to
	 * @return true if both types are the same.
	 * @since 1.0.0;
	 */
	public static boolean isIdentity(Class<?> from, Class<?> to) {
		return from == to;
	}

	/**
	 * <em>java language specification - 5.1.2 Widening Primitive Conversion</em> .
	 *
	 * <code>
	 * <blockquote>The following 19 specific conversions on primitive types
	 *  are called the widening primitive conversions:
	 * <ul>
	 * <li>byte to short, int, long, float, or double</li>
	 * <li>short to int, long, float, or double</li>
	 * <li>char to int, long, float, or double</li>
	 * <li>int to long, float, or double</li>
	 * <li>long to float or double</li>
	 * <li>float to double</li>
	 * </blockquote>
	 * </code>
	 *
	 * @param from the base type
	 * @param to   the widening type
	 * @return true if from to to is a primitive widening.
	 * @since 1.0.0;
	 */
	public static boolean isPrimitiveWidening(Class<?> from, Class<?> to) {
		Assert.isTrue(Primitives.isPrimitive(from), PARAMETER_MUST_BE_A_PRIMITIVE, "form");
		Assert.isTrue(Primitives.isPrimitive(to), PARAMETER_MUST_BE_A_PRIMITIVE, "to");

		boolean isPrimitiveWidening = false;

		if (isIdentity(from, Byte.TYPE)) {
			isPrimitiveWidening = isPrimitiveByteWidening(to);
		} else if (isIdentity(from, Short.TYPE)) {
			isPrimitiveWidening = isPrimitiveShortWidening(to);
		} else if (isIdentity(from, Character.TYPE)) {
			isPrimitiveWidening = isPrimitiveCharacterWidening(to);
		} else if (isIdentity(from, Integer.TYPE)) {
			isPrimitiveWidening = isPrimitiveIntegerWidening(to);
		} else if (isIdentity(from, Long.TYPE)) {
			isPrimitiveWidening = isPrimitiveLongWidening(to);
		} else if (isIdentity(from, Float.TYPE)) {
			isPrimitiveWidening = isPrimitiveFloatWidening(to);
		}

		/*
		 * must be a double - no widening available
		 */
		return isPrimitiveWidening;
	}

	/**
	 * byte to short, int, long, float, or double
	 */
	private static boolean isPrimitiveByteWidening(Class<?> to) {
		boolean isWidening = isPrimitiveShortWidening(to);

		isWidening |= isIdentity(to, Short.TYPE);

		return isWidening;
	}

	/**
	 * short to int, long, float, or double
	 */
	private static boolean isPrimitiveShortWidening(Class<?> to) {
		boolean isWidening = isPrimitiveIntegerWidening(to);

		isWidening |= isIdentity(to, Integer.TYPE);

		return isWidening;
	}

	/**
	 * char to int, long, float, or double
	 */
	private static boolean isPrimitiveCharacterWidening(Class<?> to) {
		boolean isWidening = isPrimitiveIntegerWidening(to);

		isWidening |= isIdentity(to, Integer.TYPE);

		return isWidening;
	}

	/**
	 * int to long, float, or double
	 */
	private static boolean isPrimitiveIntegerWidening(Class<?> to) {
		boolean isWidening = isPrimitiveLongWidening(to);

		isWidening |= isIdentity(to, Long.TYPE);

		return isWidening;
	}

	/**
	 * long to float or double
	 */
	private static boolean isPrimitiveLongWidening(Class<?> to) {
		boolean isWidening = isPrimitiveFloatWidening(to);

		isWidening |= isIdentity(to, Float.TYPE);

		return isWidening;
	}

	/**
	 * float to double
	 */
	private static boolean isPrimitiveFloatWidening(Class<?> to) {
		boolean isWidening = isIdentity(to, Double.TYPE);
		return isWidening;
	}

	/**
	 * <em>java language specification - 5.1.3 Narrowing Primitive Conversions</em>
	 * .
	 *
	 * <code>
	 * <blockquote>The following 22 specific conversions on primitive types
	 *  are called the narrowing primitive conversions:
	 * <ul>
	 * <li>short to byte or char</li>
	 * <li>char to byte or short</li>
	 * <li>int to byte, short, or char</li>
	 * <li>long to byte, short, char, or int</li>
	 * <li>float to byte, short, char, int, or long</li>
	 * <li>double to byte, short, char, int, long, or float </li>
	 * </blockquote>
	 * </code>
	 *
	 * @param from the base type
	 * @param to   the widening type
	 * @return true if from to to is a primitive widening.
	 * @since 1.0.0;
	 */
	public static boolean isPrimitiveNarrowing(Class<?> from, Class<?> to) {
		Assert.isTrue(Primitives.isPrimitive(from), "mustBeAPrimitive", "form");
		Assert.isTrue(Primitives.isPrimitive(to), "mustBeAPrimitive", "to");
		boolean isPrimitiveNarrowing = false;

		if (isIdentity(from, Double.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveDoubleNarrowing(to);
		} else if (isIdentity(from, Short.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveShortNarrowing(to);
		} else if (isIdentity(from, Character.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveCharacterNarrowing(to);
		} else if (isIdentity(from, Integer.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveIntegerNarrowing(to);
		} else if (isIdentity(from, Long.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveLongNarrowing(to);
		} else if (isIdentity(from, Float.TYPE)) {
			isPrimitiveNarrowing = isPrimitiveFloatNarrowing(to);
		}
		/*
		 * must be a byte
		 */
		return isPrimitiveNarrowing;
	}

	/**
	 * short to byte or char
	 */
	private static boolean isPrimitiveShortNarrowing(Class<?> to) {
		boolean isNarrowing = false;

		isNarrowing |= isIdentity(to, Byte.TYPE);
		isNarrowing |= isIdentity(to, Character.TYPE);

		return isNarrowing;
	}

	/**
	 * char to byte or short
	 */
	private static boolean isPrimitiveCharacterNarrowing(Class<?> to) {
		boolean isNarrowing = false;

		isNarrowing |= isIdentity(to, Byte.TYPE);
		isNarrowing |= isIdentity(to, Short.TYPE);

		return isNarrowing;
	}

	/**
	 * int to byte, short, or char
	 */
	private static boolean isPrimitiveIntegerNarrowing(Class<?> to) {
		boolean isNarrowing = isPrimitiveCharacterNarrowing(to);

		isNarrowing |= isIdentity(to, Character.TYPE);

		return isNarrowing;
	}

	/**
	 * long to byte, short, char, or int
	 */
	private static boolean isPrimitiveLongNarrowing(Class<?> to) {
		boolean isNarrowing = isPrimitiveIntegerNarrowing(to);

		isNarrowing |= isIdentity(to, Integer.TYPE);

		return isNarrowing;
	}

	/**
	 * float to byte, short, char, int, or long
	 */
	private static boolean isPrimitiveFloatNarrowing(Class<?> to) {
		boolean isNarrowing = isPrimitiveLongNarrowing(to);

		isNarrowing |= isIdentity(to, Long.TYPE);

		return isNarrowing;
	}

	/**
	 * # double to byte, short, char, int, long, or float
	 */
	private static boolean isPrimitiveDoubleNarrowing(Class<?> to) {
		boolean isNarrowing = isPrimitiveFloatNarrowing(to);

		isNarrowing |= isIdentity(to, Float.TYPE);

		return isNarrowing;
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @return true if from is a subtype of t.
	 * @since 1.0.0;
	 */
	public static boolean isWideningReference(Class<?> from, Class<?> to) {
		Assert.notNull("from", from);
		Assert.notNull("to", to);
		if (from.isArray() && to.isArray()) {
			from = from.getComponentType();
			to = to.getComponentType();
		}
		return to.isAssignableFrom(from);
	}

	/**
	 * The following conversions are called the narrowing reference conversions :
	 * <ul>
	 * <li>From any reference type S to any reference type T, provided that S is a
	 * proper supertype (§4.10) of T. (An important special case is that there is a
	 * narrowing conversion from the class type Object to any other reference
	 * type.)</li>
	 * <li>From any class type C to any non-parameterized interface type K, provided
	 * that C is not final and does not implement K.</li>
	 * <li>From any interface type J to any non-parameterized class type C that is
	 * not final.</li>
	 * <li>From the interface types Cloneable and java.io.Serializable to any array
	 * type T[].</li>
	 * <li>From any interface type J to any non-parameterized interface type K,
	 * provided that J is not a subinterface of K.</li>
	 * <li>From any array type SC[] to any array type TC[], provided that SC and TC
	 * are reference types and there is a narrowing conversion from SC to TC.</li>
	 * </ul>
	 *
	 * @param from
	 * @param to
	 * @return true if a cast "to t = (to) from;" would be a downcast.
	 * @since 1.0.0;
	 */
	public static boolean isNarrowingReference(Class<?> from, Class<?> to) {
		boolean wideningReference = isWideningReference(from, to);
		return !wideningReference;
	}

	/**
	 *
	 * @param clazz
	 * @return true if clazz is either a primitive or a primitive wrapper type.
	 * @since 1.0.0;
	 */
	public static boolean isBoxingType(Class<?> clazz) {
		return getBoxingConversion(clazz) != null || getUnboxingConversion(clazz) != null;
	}

	/**
	 * Boxing conversion converts values of primitive type to corresponding values
	 * of reference type. Specifically, the following 8 conversion are called the
	 * boxing conversions:
	 * <ul>
	 * <li>From type boolean to type Boolean</li>
	 * <li>From type byte to type Byte</li>
	 * <li>From type char to type Character</li>
	 * <li>From type short to type Short</li>
	 * <li>From type int to type Integer</li>
	 * <li>From type long to type Long</li>
	 * <li>From type float to type Float</li>
	 * <li>From type double to type Double</li>
	 * </ul>
	 *
	 * @param primitive
	 * @return the wrapper type for the primitive or null if primitive is not a
	 *         primitive type.
	 * @since 1.0.0;
	 */
	public static Class<?> getBoxingConversion(Class<?> primitive) {
		Class<?> wrapper = Primitives.getWrapperType(primitive);
		boolean wrapperEqualsPrimitive = Objects.equals(wrapper, primitive);

		Class<?> autoBoxedClass = null;

		if (!wrapperEqualsPrimitive) {
			autoBoxedClass = wrapper;
		}

		return autoBoxedClass;
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @return true if the conversion of type from to type to is a boxing
	 *         conversion. E.g. int -> Integer.
	 * @since 1.0.0;
	 * @see #getBoxingConversion(Class)
	 */
	public static boolean isBoxingConversion(Class<?> from, Class<?> to) {
		boolean isBoxingConversion = false;
		if (to != null) {
			Class<?> boxingConversion = getBoxingConversion(from);
			isBoxingConversion = to.equals(boxingConversion);
		}
		return isBoxingConversion;
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @return true if the conversion of type from to type to is an unboxing
	 *         conversion. E.g. Integer -> int.
	 * @since 1.0.0;
	 * @see #getUnboxingConversion(Class)
	 */
	public static boolean isUnboxingConversion(Class<?> from, Class<?> to) {
		boolean isUnboxingConversion = false;
		if (to != null) {
			Class<?> unboxedClass = getUnboxingConversion(from);
			isUnboxingConversion = to.equals(unboxedClass);
		}
		return isUnboxingConversion;
	}

	/**
	 * Unboxing conversion converts values of reference type to corresponding values
	 * of primitive type. Specifically, the following 8 conversion are called the
	 * unboxing conversions:
	 * <ul>
	 * <li>From type Boolean to type boolean</li>
	 * <li>From type Byte to type byte</li>
	 * <li>From type Character to type char</li>
	 * <li>From type Short to type short</li>
	 * <li>From type Integer to type int</li>
	 * <li>From type Long to type long</li>
	 * <li>From type Float to type float</li>
	 * <li>From type Double to type double</li>
	 * </ul>
	 *
	 * @param primitiveWrapper
	 * @return the primitive type for the primitiveWrapper or null if parameter
	 *         primitiveWrapper is not a primitive wrapper type.
	 * @since 1.0.0;
	 */
	public static Class<?> getUnboxingConversion(Class<?> primitiveWrapper) {
		Class<?> primitive = Primitives.getPrimitiveType(primitiveWrapper);
		return primitive;
	}

	/**
	 * Unboxes the primitiveWrapper by calling the {@link PrimitiveCallback}.
	 *
	 * @param primitiveWrapper
	 * @param primitiveCallback
	 *
	 * @see PrimitiveCallbackAdapter
	 * @see PrimitiveArrayCallback
	 * @since 1.0.0;
	 */
	public static void unbox(Object primitiveWrapper, PrimitiveCallback primitiveCallback) {
		if (primitiveWrapper instanceof Boolean) {
			Boolean value = (Boolean) primitiveWrapper;
			primitiveCallback.primitive(value.booleanValue());
		} else if (primitiveWrapper instanceof Byte) {
			Byte value = (Byte) primitiveWrapper;
			primitiveCallback.primitive(value.byteValue());
		} else if (primitiveWrapper instanceof Character) {
			Character value = (Character) primitiveWrapper;
			primitiveCallback.primitive(value.charValue());
		} else if (primitiveWrapper instanceof Short) {
			Short value = (Short) primitiveWrapper;
			primitiveCallback.primitive(value.shortValue());
		} else if (primitiveWrapper instanceof Integer) {
			Integer value = (Integer) primitiveWrapper;
			primitiveCallback.primitive(value.intValue());
		} else if (primitiveWrapper instanceof Long) {
			Long value = (Long) primitiveWrapper;
			primitiveCallback.primitive(value.longValue());
		} else if (primitiveWrapper instanceof Float) {
			Float value = (Float) primitiveWrapper;
			primitiveCallback.primitive(value.floatValue());
		} else if (primitiveWrapper instanceof Double) {
			Double value = (Double) primitiveWrapper;
			primitiveCallback.primitive(value.doubleValue());
		} else {
			throw new IllegalArgumentException("primitiveWrapper is not a primitiveWrapper type");
		}
	}
}