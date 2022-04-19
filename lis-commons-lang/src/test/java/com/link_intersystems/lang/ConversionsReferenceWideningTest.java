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

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Conversions} widening reference conversion as defined by the
 * java language specification 5.1.5 Widening Reference Conversions
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 */
class ConversionsReferenceWideningTest {

    @Test
    void nullFrom() {
        Class<?> s = null;
        Class<?> t = Collection.class;
        assertThrows(IllegalArgumentException.class, () -> Conversions.isWideningReference(s, t));
    }

    @Test
    void nullTo() {
        Class<?> s = List.class;
        Class<?> t = null;
        assertThrows(IllegalArgumentException.class, () -> Conversions.isWideningReference(s, t));
    }

    /**
     * A widening reference conversion exists from any type S to any type T,
     * provided S is a subtype (§4.10) of T.
     */
    @Test
    void subtypeWidening() {
        Class<?> s = List.class;
        Class<?> t = Collection.class;
        boolean wideningReference = false;
        wideningReference = Conversions.isWideningReference(s, t);
        Assertions.assertTrue(wideningReference);
        wideningReference = Conversions.isWideningReference(t, s);
        Assertions.assertFalse(wideningReference);
    }

    @Test
    void arrayWidening() {
        Class<?> s = List[].class;
        Class<?> t = Collection[].class;
        boolean wideningReference = false;
        wideningReference = Conversions.isWideningReference(s, t);
        Assertions.assertTrue(wideningReference);
        wideningReference = Conversions.isWideningReference(t, s);
        Assertions.assertFalse(wideningReference);
    }

}
