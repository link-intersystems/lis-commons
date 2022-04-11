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

import com.link_intersystems.lang.reflect.MemberModifierPredicate.Match;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class MemberModifierPredicatetTest {

    @SuppressWarnings("unused")
    private String privateTest;

    public String publicTest;

    private Field privateTestField;

    private Field publicTestField;

    private Method privateMethod;

    private Method publicMethod;

    @SuppressWarnings("unused")
    private static final String TEST_CONSTANT = "TEST_CONSTANT";

    @SuppressWarnings("unused")
    private static String TEST_CONSTANT_2 = "TEST_CONSTANT_2";

    private Constructor<? extends MemberModifierPredicatetTest> privateConstructor;

    private Constructor<? extends MemberModifierPredicatetTest> publicConstructor;

    private Field privateStaticFinalConstField;

    private Field privateStaticConstField;

    public MemberModifierPredicatetTest() {
    }

    MemberModifierPredicatetTest(int testConstructor) {
    }

    @SuppressWarnings("unused")
    private void privateMethod() {
    }

    @Before
    public void setup() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        privateStaticFinalConstField = getClass().getDeclaredField("TEST_CONSTANT");
        privateStaticConstField = getClass().getDeclaredField("TEST_CONSTANT_2");
        privateTestField = getClass().getDeclaredField("privateTest");
        publicTestField = getClass().getDeclaredField("publicTest");
        privateMethod = getClass().getDeclaredMethod("privateMethod");
        publicMethod = getClass().getDeclaredMethod("setup");
        privateConstructor = getClass().getDeclaredConstructor(int.class);
        publicConstructor = getClass().getDeclaredConstructor();
    }

    @Test(expected = IllegalArgumentException.class)
    public void newWithNullMatch() {
        new MemberModifierPredicate(Modifier.PUBLIC, null);
    }

    @Test
    public void evaluateAgainstMethod() {
        MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PUBLIC);
        boolean evaluate = memberModifierPredicate.test(privateMethod);
        assertFalse(evaluate);
        evaluate = memberModifierPredicate.test(publicMethod);
        assertTrue(evaluate);
    }

    @Test
    public void evaluateAgainstConstructor() {
        MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PUBLIC);
        boolean evaluate = memberModifierPredicate.test(privateConstructor);
        assertFalse(evaluate);
        evaluate = memberModifierPredicate.test(publicConstructor);
        assertTrue(evaluate);
    }

    @Test
    public void evaluateAgainstField() {
        MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PUBLIC);
        boolean evaluate = memberModifierPredicate.test(privateTestField);
        assertFalse(evaluate);
        evaluate = memberModifierPredicate.test(publicTestField);
        assertTrue(evaluate);
    }

    @Test
    public void matchExace() {
        MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL, Match.EXACT);
        boolean evaluate = memberModifierPredicate.test(privateStaticConstField);
        assertFalse(evaluate);
        evaluate = memberModifierPredicate.test(privateStaticFinalConstField);
        assertTrue(evaluate);
    }

    @Test
    public void matchAtLeastOne() {
        MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PROTECTED | Modifier.STATIC, Match.AT_LEAST_ONE);
        boolean evaluate = memberModifierPredicate.test(privateStaticConstField);
        assertTrue(evaluate);
        evaluate = memberModifierPredicate.test(privateStaticFinalConstField);
        assertTrue(evaluate);

        memberModifierPredicate = new MemberModifierPredicate(Modifier.PROTECTED | Modifier.NATIVE, Match.AT_LEAST_ONE);
        evaluate = memberModifierPredicate.test(privateStaticFinalConstField);
        assertFalse(evaluate);
    }

}
