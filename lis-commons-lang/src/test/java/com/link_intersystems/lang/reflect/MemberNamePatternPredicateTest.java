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


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberNamePatternPredicateTest {

    @Test
    void nullConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new MemberNamePatternPredicate(null));
    }

    @Test
    void aferDesrialization() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add", int.class, Object.class);
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        MemberNamePatternPredicate deserialized = Serialization.clone(memberNamePatternPredicate);
        boolean evaluated = deserialized.test(declaredMethod);
        Assertions.assertTrue(evaluated);
    }

    @Test
    void match() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add", int.class, Object.class);
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        boolean evaluated = memberNamePatternPredicate.test(declaredMethod);
        Assertions.assertTrue(evaluated);
    }

    @Test
    void notAMember() throws SecurityException, NoSuchMethodException {
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        assertThrows(IllegalArgumentException.class, () -> memberNamePatternPredicate.test(ArrayList.class));
    }
}
