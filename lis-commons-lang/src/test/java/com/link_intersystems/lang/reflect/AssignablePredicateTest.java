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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AssignablePredicateTest {

    private AssignablePredicate listAssignable;

    @Before
    public void setup() {
        listAssignable = new AssignablePredicate(List.class);

    }

    @Test(expected = IllegalArgumentException.class)
    public void newWithNullConstructor() {
        new AssignablePredicate(null);
    }

    @Test
    public void evaluateAgainstObject() {
        Assert.assertTrue(listAssignable.test(new ArrayList<Object>()));
    }

    @Test
    public void evaluateAgainstClass() {
        Assert.assertTrue(listAssignable.test(ArrayList.class));
    }

    @Test
    public void evaluateAgainstNull() {
        Assert.assertFalse(listAssignable.test(null));
    }

    @Test
    public void evaluateAgainstUnassignableClass() {
        Assert.assertFalse(listAssignable.test(Collection.class));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void evaluateAgainstUnassignableObject() {
        Assert.assertFalse(listAssignable.test(new HashMap()));
    }
}
