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
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SerializableMethodTest  {

    @Test
    void nullConstructor() {
        assertThrows(IllegalArgumentException.class, () ->new SerializableMethod(null));
    }

    @Test
    void serialize() throws SecurityException, NoSuchMethodException {
        Method method = SerializableMethodTest.class.getDeclaredMethod("someTestMethod", int.class, int[].class, String.class);
        SerializableMethod serializableMethod = new SerializableMethod(method);

        SerializableMethod deserialized = cloneSerializable(serializableMethod);

        Method deserializedMethod = deserialized.get();

        assertEquals(method, deserializedMethod);
    }

    public String someTestMethod(int i, int[] js, String string) {
        return "";
    }

    @Test
    void securityException() throws Throwable {
        Method method = SerializableMethodTest.class.getDeclaredMethod("someTestMethod", int.class, int[].class, String.class);
        final SecurityExceptionSerializableMethod serializableMethod = new SecurityExceptionSerializableMethod(method);

        Assertion.assertCause(SecurityException.class, new Callable<Object>() {

            public Object call() throws Exception {
                SerializableMethod cloneSerializable = cloneSerializable(serializableMethod);
                return cloneSerializable;
            }
        });
    }

    @Test
    void noSuchMethod() throws Throwable {
        Method method = SerializableMethodTest.class.getDeclaredMethod("someTestMethod", int.class, int[].class, String.class);
        final SerializableMethod serializableMethod = new NoSuchMethodSerializableMethod(method);
        Assertion.assertCause(NoSuchMethodException.class, new Callable<Object>() {

            public Object call() throws Exception {
                SerializableMethod cloneSerializable = cloneSerializable(serializableMethod);
                return cloneSerializable;
            }
        });
    }

    /**
     * Helper method until commons-lang3 fixed the class loader problem. See
     * {@link <a href="https://issues.apache.org/jira/browse/LANG-788">...</a>}.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T cloneSerializable(T serializable) {
        T clone = (T) Serialization.deserialize(Serialization.serialize(serializable));
        return clone;
    }

}

class SecurityExceptionSerializableMethod extends SerializableMethod {
    public SecurityExceptionSerializableMethod(Method method) {
        super(method);
    }

    /**
     *
     */
    private static final long serialVersionUID = 6654526264122107754L;

    @Override
    protected Method getMethod(Class<?> declaringClass, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException {
        throw new SecurityException();
    }

};

class NoSuchMethodSerializableMethod extends SerializableMethod {
    public NoSuchMethodSerializableMethod(Method method) {
        super(method);
    }

    /**
     *
     */
    private static final long serialVersionUID = 6654526264122107754L;

    @Override
    protected Method getMethod(Class<?> declaringClass, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

};
