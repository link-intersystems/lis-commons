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
package com.link_intersystems.lang.ref;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NullSafeReferenceTest  {

    @Test
    void nullObjectReferent() {
        NullSafeReference<String> nullSafeReference = new NullSafeReference<String>("");
        String referent = nullSafeReference.get();
        assertEquals("", referent);
    }

    @Test
    void referent() {
        NullSafeReference<String> nullSafeReference = new NullSafeReference<String>("");
        nullSafeReference.setReferent("hello world");
        String referent = nullSafeReference.get();
        assertEquals("hello world", referent);

        nullSafeReference.setReferent((String) null);
        referent = nullSafeReference.get();
        assertEquals("", referent);
    }

    @Test
    void referentByReference() {
        NullSafeReference<String> nullSafeReference = new NullSafeReference<String>("");
        nullSafeReference.setReferent(new HardReference<String>("hello world"));
        String referent = nullSafeReference.get();
        assertEquals("hello world", referent);

        Reference<String> reference = NullReference.getInstance();
        nullSafeReference.setReferent(reference);
        referent = nullSafeReference.get();
        assertEquals("", referent);
    }
}
