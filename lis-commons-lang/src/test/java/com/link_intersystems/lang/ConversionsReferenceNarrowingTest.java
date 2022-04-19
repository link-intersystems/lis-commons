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

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Conversions} narrowing reference conversion as defined by
 * the java language specification 5.1.6 Narrowing Reference Conversions
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
class ConversionsReferenceNarrowingTest {

    @Test
    void nullFrom() {
        Class<?> s = null;
        Class<?> t = Collection.class;
        assertThrows(IllegalArgumentException.class, () -> Conversions.isNarrowingReference(s, t));
    }

    @Test
    void nullTo() {
        Class<?> s = List.class;
        Class<?> t = null;
        assertThrows(IllegalArgumentException.class, () -> Conversions.isNarrowingReference(s, t));
    }

    /**
     * From any reference type S to any reference type T, provided that S is a
     * proper supertype (§4.10) of T. (An important special case is that there
     * is a narrowing conversion from the class type Object to any other
     * reference type.)
     */
    @Test
    void supertype() {
        Class<?> s = Collection.class;
        Class<?> t = List.class;
        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(s, t);
        Assertions.assertTrue(narrowingReference);
        narrowingReference = Conversions.isNarrowingReference(t, s);
        Assertions.assertFalse(narrowingReference);
    }

    /**
     * From any class type C to any non-parameterized interface type K, provided
     * that C is not final and does not implement K.
     */
    @Test
    void notFinalClassDoesntImplementInterface() {
        Class<?> c = Thread.class;
        Class<?> k = Appendable.class;

        Assertions.assertFalse(Modifier.isFinal(c.getModifiers()));
        Type genericSuperclass = c.getGenericSuperclass();
        Assertions.assertFalse(genericSuperclass instanceof ParameterizedType);

        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(c, k);
        Assertions.assertTrue(narrowingReference);
    }

    /**
     * From any interface type J to any non-parameterized class type C that is
     * not final.
     */
    @Test
    void interfaceToNonFinalClass() {
        Class<?> j = Appendable.class;
        Class<?> c = Thread.class;

        Assertions.assertTrue(j.isInterface());
        Type genericSuperclass = c.getGenericSuperclass();
        Assertions.assertFalse(genericSuperclass instanceof ParameterizedType);
        Assertions.assertFalse(Modifier.isFinal(c.getModifiers()));

        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(j, c);
        Assertions.assertTrue(narrowingReference);
    }

    /**
     * From the interface types Cloneable and java.io.Serializable to any array
     * type T[].
     */
    @Test
    void interfaceClonableAndSerializableToAnyArray() {
        Class<?> c = Cloneable.class;
        Class<?> s = Serializable.class;
        Class<?> a = Thread[].class;

        Assertions.assertTrue(a.isArray());

        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(c, a);
        Assertions.assertTrue(narrowingReference);
        narrowingReference = Conversions.isNarrowingReference(s, a);
        Assertions.assertTrue(narrowingReference);
    }

    /**
     * From any interface type J to any non-parameterized interface type K,
     * provided that J is not a subinterface of K.
     */
    @Test
    void interfaceToNonParameterizedInterface() {
        Class<?> j = Appendable.class;
        Class<?> k = CharSequence.class;

        Assertions.assertTrue(j.isInterface());
        Assertions.assertTrue(k.isInterface());
        Type genericSuperclass = k.getGenericSuperclass();
        Assertions.assertFalse(genericSuperclass instanceof ParameterizedType);
        Assertions.assertFalse(k.isAssignableFrom(j));

        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(j, k);
        Assertions.assertTrue(narrowingReference);
    }

    /**
     * From any array type SC[] to any array type TC[], provided that SC and TC
     * are reference types and there is a narrowing conversion from SC to TC.
     */
    @Test
    void arrayNarrowing() {
        Class<?> sc = Appendable[].class;
        Class<?> tc = CharSequence[].class;

        Assertions.assertTrue(sc.isArray());
        Assertions.assertTrue(tc.isArray());

        boolean narrowingReference = false;
        narrowingReference = Conversions.isNarrowingReference(sc, tc);
        Assertions.assertTrue(narrowingReference);
    }

}
