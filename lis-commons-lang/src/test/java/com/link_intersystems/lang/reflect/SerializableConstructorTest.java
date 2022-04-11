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

import com.link_intersystems.Assertion;
import com.link_intersystems.lang.Serialization;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import static junit.framework.Assert.assertEquals;

public class SerializableConstructorTest {

    @Test(expected = IllegalArgumentException.class)
    public void nullConstructor() {
        new SerializableConstructor(null);
    }

    @Test
    public void serialize() throws SecurityException, NoSuchMethodException {
        Constructor<ConstructorSerializationTestClass> constructor = ConstructorSerializationTestClass.class.getDeclaredConstructor(String.class);
        SerializableConstructor serializableConstructor = new SerializableConstructor(constructor);

        SerializableConstructor deserialized = Serialization.clone(serializableConstructor);

        Constructor<?> deserializedMethod = deserialized.get();

        assertEquals(constructor, deserializedMethod);
    }

    @Test
    public void securityException() throws Throwable {
        Constructor<ConstructorSerializationTestClass> constructor = ConstructorSerializationTestClass.class.getDeclaredConstructor(String.class);
        final SerializableConstructor serializableConstructor = new SecurityExceptionSerializableConstructor(constructor);
        Assertion.assertCause(SecurityException.class, new Callable<Object>() {

            public Object call() throws Exception {
                return Serialization.clone(serializableConstructor);
            }
        });
    }

    @Test
    public void noSuchMethodOnSerialization() throws Throwable {
        Constructor<ConstructorSerializationTestClass> constructor = ConstructorSerializationTestClass.class.getDeclaredConstructor(String.class);
        final SerializableConstructor serializableConstructor = new NoSuchMethodSerializableConstructor(constructor);
        Assertion.assertCause(NoSuchMethodException.class, new Callable<Object>() {

            public Object call() throws Exception {
                return Serialization.clone(serializableConstructor);
            }
        });
    }
}

class NoSuchMethodSerializableConstructor extends SerializableConstructor {

    public NoSuchMethodSerializableConstructor(Constructor<?> constructor) {
        super(constructor);
    }

    /**
     *
     */
    private static final long serialVersionUID = 6654526264122107754L;

    @Override
    protected Constructor<?> getConstructor(Class<?> declaringClass, Class<?>[] parameterTypes) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }
}

class SecurityExceptionSerializableConstructor extends SerializableConstructor {

    public SecurityExceptionSerializableConstructor(Constructor<?> constructor) {
        super(constructor);
    }

    /**
     *
     */
    private static final long serialVersionUID = 6654526264122107754L;

    @Override
    protected Constructor<?> getConstructor(Class<?> declaringClass, Class<?>[] parameterTypes) throws NoSuchMethodException {
        throw new SecurityException();
    }
}

class ConstructorSerializationTestClass {

    public ConstructorSerializationTestClass() {
    }

    public ConstructorSerializationTestClass(String string) {
    }
}