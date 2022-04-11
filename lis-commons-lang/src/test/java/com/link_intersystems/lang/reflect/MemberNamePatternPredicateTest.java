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

import com.link_intersystems.util.ObjectFactory;
import com.link_intersystems.util.SerializableTemplateObjectFactory;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;

public class MemberNamePatternPredicateTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullConstructor() {
        new MemberNamePatternPredicate(null);
    }

    @Test
    public void aferDesrialization() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add", int.class, Object.class);
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        ObjectFactory<MemberNamePatternPredicate> objectFactory = new SerializableTemplateObjectFactory<MemberNamePatternPredicate>(memberNamePatternPredicate);
        MemberNamePatternPredicate deserialized = objectFactory.getObject();
        boolean evaluated = deserialized.test(declaredMethod);
        assertTrue(evaluated);
    }

    @Test
    public void match() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add", int.class, Object.class);
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        boolean evaluated = memberNamePatternPredicate.test(declaredMethod);
        assertTrue(evaluated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAMember() throws SecurityException, NoSuchMethodException {
        MemberNamePatternPredicate memberNamePatternPredicate = new MemberNamePatternPredicate(Pattern.compile("add.*"));
        memberNamePatternPredicate.test(ArrayList.class);
    }
}
