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
package com.link_intersystems.lang.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignablePredicateTest  {

    private AssignablePredicate listAssignable;

    @BeforeEach
    public void setup() {
        listAssignable = new AssignablePredicate(List.class);

    }

    @Test
    void evaluateAgainstObject() {
        assertTrue(listAssignable.test(new ArrayList<Object>()));
    }

    @Test
    void evaluateAgainstClass() {
        assertTrue(listAssignable.test(ArrayList.class));
    }

    @Test
    void evaluateAgainstNull() {
        assertFalse(listAssignable.test(null));
    }

    @Test
    void evaluateAgainstUnassignableClass() {
        assertFalse(listAssignable.test(Collection.class));
    }

    @SuppressWarnings("rawtypes")
    @Test
    void evaluateAgainstUnassignableObject() {
        assertFalse(listAssignable.test(new HashMap()));
    }
}
