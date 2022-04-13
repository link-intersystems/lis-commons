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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MethodInvokingTransformerTest  {

    private InvocationArgumentsResolver argumentResolver;
    private Method charAtMethod;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        argumentResolver = new InvocationArgumentsResolver() {

            public Object[] getArguments(Object invokedObject, Method invokedMethod) {
                return new Object[]{1};
            }
        };
        charAtMethod = String.class.getDeclaredMethod("charAt", Integer.TYPE);
    }

    @Test
    void transformWrongType() {
        Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod, argumentResolver);
        Character valueOf = Character.valueOf('c');
        assertThrows(IllegalArgumentException.class, () -> transformer.apply(valueOf));
    }

    @Test
    void transformNullValue() {
        Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod, argumentResolver);
        assertThrows(NullPointerException.class, () -> transformer.apply(null));
    }

    @Test
    void wrongArguments() {
        Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod, new InvocationArgumentsResolver() {

            public Object[] getArguments(Object invokedObject, Method invokedMethod) {
                return new Object[]{"A"};
            }
        });
        assertThrows(IllegalArgumentException.class, () -> transformer.apply("Test"));
    }

    @Test
    void nullTypeConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new MethodInvokingTransformer((Method2) null, argumentResolver));
    }

    @Test
    void nullArgumentResolverConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new MethodInvokingTransformer(charAtMethod, null));
    }

    @Test
    void invokeMethod() {
        String testString = "HelloWorld";

        Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod, argumentResolver);
        Object transform = transformer.apply(testString);
        assertNotNull(transform);
        assertTrue(transform instanceof Character);
        Character charAt1 = (Character) transform;
        assertEquals('e', charAt1.charValue());
    }

    @Test
    void invokeInstanceMethodOnNull() {
        Function<Object, Object> transformer = new MethodInvokingTransformer(charAtMethod, argumentResolver);
        assertThrows(NullPointerException.class, () -> transformer.apply(null));
    }

    @Test
    void invokeMethodWithNoArguments() throws NoSuchMethodException {
        String testString = "HelloWorld";
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Function<Object, Object> transformer = new MethodInvokingTransformer(toStringMethod);
        Object transform = transformer.apply(testString);
        assertNotNull(transform);
        assertTrue(transform instanceof String);
        String string = (String) transform;
        assertEquals("HelloWorld", string);
    }

    @Test
    void invokeInstanceMethodThrowsUnknownException() throws NoSuchMethodException {
        String testString = "HelloWorld";
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Function<Object, Object> transformer = new MethodInvokingTransformer(toStringMethod) {

            @Override
            protected Object invokeInstanceMethod(Object targetObject) throws Exception {
                throw new IOException();
            }

        };
        assertThrows(IllegalStateException.class, () -> transformer.apply(testString));
    }

    @Test
    void invokeStaticMethodWithNullTarget() throws SecurityException, NoSuchMethodException {
        InvocationArgumentsResolver invocationArgumentsResolver = createNiceMock(InvocationArgumentsResolver.class);
        Method formatMethod = String.class.getDeclaredMethod("format", String.class, Object[].class);
        expect(invocationArgumentsResolver.getArguments(String.class, formatMethod)).andReturn(new Object[]{"Hello %s", "World"});
        replay(invocationArgumentsResolver);

        Function<Object, Object> transformer = new MethodInvokingTransformer(formatMethod, invocationArgumentsResolver);
        Object transform = transformer.apply(null);
        assertNotNull(transform);
        assertEquals("Hello World", transform);
    }

    @Test
    void invokeStaticMethodWithClassTarget() throws SecurityException, NoSuchMethodException {
        InvocationArgumentsResolver invocationArgumentsResolver = createNiceMock(InvocationArgumentsResolver.class);
        Method formatMethod = String.class.getDeclaredMethod("format", String.class, Object[].class);
        expect(invocationArgumentsResolver.getArguments(String.class, formatMethod)).andReturn(new Object[]{"Hello %s", "World"});
        replay(invocationArgumentsResolver);

        Function<Object, Object> transformer = new MethodInvokingTransformer(formatMethod, invocationArgumentsResolver);
        Object transform = transformer.apply(String.class);
        assertNotNull(transform);
        assertEquals("Hello World", transform);
    }

}
