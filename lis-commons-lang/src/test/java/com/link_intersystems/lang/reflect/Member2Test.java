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
import org.powermock.api.easymock.PowerMock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Member2Test {

    private Member2<?> overriding;
    private Member2<?> overridden;

    @BeforeEach
    public void setup() {
        overriding = PowerMock.createPartialMock(Member2.class, "getModifiers");
        overridden = PowerMock.createPartialMock(Member2.class, "getModifiers");
    }

    @Test
    void overriddenPublic() {
        expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
        expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
        expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overriding.getModifiers()).andReturn(0);
        expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

    }

    @Test
    void overriddenProtected() {
        expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);

        expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
        replay(overriding, overridden);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);
        expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);
        expect(overriding.getModifiers()).andReturn(0);
        replay(overriding, overridden);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

    }

    @Test
    void overriddenDefault() {
        expect(overridden.getModifiers()).andReturn(0);
        expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overridden.getModifiers()).andReturn(0);
        expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
        replay(overriding, overridden);
        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overridden.getModifiers()).andReturn(0);
        expect(overriding.getModifiers()).andReturn(0);
        replay(overriding, overridden);

        areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertTrue(areAccessModifierCompatible);
    }

    @Test
    void privateMethods() {
        expect(overridden.getModifiers()).andReturn(Modifier.PRIVATE);
        expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
        replay(overriding, overridden);
        boolean areAccessModifierCompatible = overriding.isAccessModifierOverriddingCompatible(overridden);
        Assertions.assertFalse(areAccessModifierCompatible);

        reset(overriding, overridden);
        expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
        expect(overriding.getModifiers()).andReturn(Modifier.PRIVATE);
        replay(overriding, overridden);
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

    @Test
    void isDeclaredExceptionForNullClass() {
        Method[] declaredMethods = TestInvokableMember.class.getDeclaredMethods();
        TestInvokableMember testInvokableMember = new TestInvokableMember(declaredMethods[0]);
        assertThrows(IllegalArgumentException.class, () -> testInvokableMember.isDeclaredException((Class<? extends Exception>) null));
    }

    @Test
    void isDeclaredExceptionForNullException() {
        Method[] declaredMethods = TestInvokableMember.class.getDeclaredMethods();
        TestInvokableMember testInvokableMember = new TestInvokableMember(declaredMethods[0]);
        assertThrows(IllegalArgumentException.class, () -> testInvokableMember.isDeclaredException((Exception) null));
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
