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

import com.link_intersystems.lang.Signature;
import com.link_intersystems.lang.ref.SerializableReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

class Member2Test {

    private Member2<?> overriding;
    private Member2<?> overridden;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        overriding = Mockito.spy(new Method2(Member2Test.class.getDeclaredMethod("setup")));
        overridden = Mockito.spy(new Method2(Member2Test.class.getDeclaredMethod("setup")));
    }

    @Test
    void overriddenPublic() {
        when(overriding.getModifiers()).thenReturn(Modifier.PUBLIC);
        when(overridden.getModifiers()).thenReturn(Modifier.PUBLIC);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overriding.getModifiers()).thenReturn(Modifier.PROTECTED);
        when(overridden.getModifiers()).thenReturn(Modifier.PUBLIC);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overriding.getModifiers()).thenReturn(0);
        when(overridden.getModifiers()).thenReturn(Modifier.PUBLIC);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

    }

    @Test
    void overriddenProtected() {
        when(overridden.getModifiers()).thenReturn(Modifier.PROTECTED);

        when(overriding.getModifiers()).thenReturn(Modifier.PROTECTED);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overridden.getModifiers()).thenReturn(Modifier.PROTECTED);
        when(overriding.getModifiers()).thenReturn(Modifier.PUBLIC);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overridden.getModifiers()).thenReturn(Modifier.PROTECTED);
        when(overriding.getModifiers()).thenReturn(0);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

    }

    @Test
    void overriddenDefault() {
        when(overridden.getModifiers()).thenReturn(0);
        when(overriding.getModifiers()).thenReturn(Modifier.PUBLIC);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overridden.getModifiers()).thenReturn(0);
        when(overriding.getModifiers()).thenReturn(Modifier.PROTECTED);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overridden.getModifiers()).thenReturn(0);
        when(overriding.getModifiers()).thenReturn(0);

        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);
    }

    @Test
    void privateMethods() {
        when(overridden.getModifiers()).thenReturn(Modifier.PRIVATE);
        when(overriding.getModifiers()).thenReturn(Modifier.PUBLIC);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

        reset(overriding, overridden);
        when(overridden.getModifiers()).thenReturn(Modifier.PUBLIC);
        when(overriding.getModifiers()).thenReturn(Modifier.PRIVATE);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);
    }

    @Test
    void getSerializableRefForMember2() throws NoSuchMethodException {
        Class2<Member2Test> class2 = Class2.get(Member2Test.class);
        Method2 declaringMethod2 = class2.getDeclaringMethod2("getSerializableRefForMember2");
        SerializableReference<Method2> serializableReference = Member2.getSerializableReference(declaringMethod2);
        Method2 method2 = serializableReference.get();
        Assertions.assertSame(declaringMethod2, method2);
    }

    @Test
    void isDeclaredException() {
        Method[] declaredMethods = TestInvokableMember.class.getDeclaredMethods();
        TestInvokableMember testInvokableMember = new TestInvokableMember(declaredMethods[0]);
        Assertions.assertTrue(testInvokableMember.isDeclaredException(IOException.class));
        Assertions.assertTrue(testInvokableMember.isDeclaredException(new IOException()));
        Assertions.assertTrue(testInvokableMember.isDeclaredException(FileNotFoundException.class));
        Assertions.assertFalse(testInvokableMember.isDeclaredException(ClassCastException.class));
    }

    private static class TestInvokableMember extends Member2<Method> {

        protected TestInvokableMember(Method member) {
            super(member);
        }

        /**
         *
         */
        private static final long serialVersionUID = -5699959517523716890L;

        @Override
        public Class<?>[] getDeclaredExceptionTypes() {
            return new Class<?>[]{ClassNotFoundException.class, IOException.class};
        }

        @Override
        public Signature getSignature() {
            return null;
        }

        @Override
        public Type[] getGenericParameterTypes() {
            return null;
        }

        @Override
        public boolean isVarArgs() {
            return false;
        }

        @Override
        protected Class<?> getReturnType() {
            return null;
        }

        @Override
        public Class<?>[] getParameterTypes() {
            return null;
        }

    }
}
