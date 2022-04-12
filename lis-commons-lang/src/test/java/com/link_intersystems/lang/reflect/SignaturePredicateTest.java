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

import com.link_intersystems.util.SerializableTemplateObjectFactory;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SignaturePredicateTest {

    @Test
    public void constructorSignatureWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new SignaturePredicate((Constructor<?>) null));
    }

    @Test
    public void methodSignatureWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new SignaturePredicate((Method) null));
    }

    @Test
    public void invokableSignatureWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new SignaturePredicate((Member2<?>) null));
    }

    @Test
    public void methodEqual() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = Collection.class.getDeclaredMethod("add", Object.class);
        Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add", Object.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
        boolean evaluate = signaturePredicate.test(declaredMethod2);
        assertTrue(evaluate);
    }

    @Test
    public void methodEqualWithInvokable() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = Collection.class.getDeclaredMethod("add", Object.class);
        Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add", Object.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
        boolean evaluate = signaturePredicate.test(new Method2(declaredMethod2));
        assertTrue(evaluate);
    }

    @Test
    public void methodEqualAfterDeserialization() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = Collection.class.getDeclaredMethod("add", Object.class);
        Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add", Object.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
        SerializableTemplateObjectFactory<SignaturePredicate> serializableTemplateObjectFactory = new SerializableTemplateObjectFactory<SignaturePredicate>(signaturePredicate);
        SignaturePredicate object = serializableTemplateObjectFactory.getObject();
        boolean evaluate = object.test(new Method2(declaredMethod2));
        assertTrue(evaluate);
    }

    @Test
    public void methodNotEqual() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = JMenu.class.getDeclaredMethod("add", String.class);
        Method declaredMethod2 = ArrayList.class.getDeclaredMethod("add", Object.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
        boolean evaluate = signaturePredicate.test(declaredMethod2);
        assertFalse(evaluate);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void methodComparedWithConstructor() throws SecurityException, NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add", Object.class);
        Constructor<ArrayList> declaredConstructor = ArrayList.class.getDeclaredConstructor();
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
        boolean evaluate = signaturePredicate.test(declaredConstructor);
        assertFalse(evaluate);
    }

    @Test
    public void evaluateAgainstAField() throws SecurityException, NoSuchMethodException, NoSuchFieldException {
        assertThrows(IllegalArgumentException.class, () -> {
            Method declaredMethod = ArrayList.class.getDeclaredMethod("add", Object.class);
            Field field = JComponent.class.getDeclaredField("WHEN_FOCUSED");
            SignaturePredicate signaturePredicate = new SignaturePredicate(declaredMethod);
            boolean evaluate = signaturePredicate.test(field);
            assertFalse(evaluate);
        });
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void evaluateWithAnotherSignature() throws SecurityException, NoSuchMethodException {
        Constructor<ArrayList> declaredConstructor = ArrayList.class.getDeclaredConstructor(int.class);
        Class2<ArrayList> class2 = Class2.get(ArrayList.class);
        Constructor2<ArrayList> applicableConstructor = class2.getApplicableConstructor(int.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredConstructor);
        boolean evaluate = signaturePredicate.test(applicableConstructor.getSignature());
        assertTrue(evaluate);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void evaluateWithUnsupportedObject() throws SecurityException, NoSuchMethodException {
        Constructor<ArrayList> declaredConstructor = ArrayList.class.getDeclaredConstructor(int.class);
        SignaturePredicate signaturePredicate = new SignaturePredicate(declaredConstructor);
        boolean evaluate = signaturePredicate.test(Integer.valueOf(1));
        assertFalse(evaluate);
    }
}
