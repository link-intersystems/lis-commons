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

import com.link_intersystems.lang.ref.AbstractSerializableReference;
import com.link_intersystems.lang.ref.Reference;

import java.io.Serializable;
import java.lang.reflect.Method;

import static java.util.Objects.*;

/**
 * Represents a {@link Method} object {@link Reference} that is
 * {@link Serializable}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0;
 *
 */
class SerializableMethod extends AbstractSerializableReference<Method> {

    /**
     *
     */
    private static final long serialVersionUID = -6636456646515992155L;

    private static class MethodSerializationInfo extends
            MemberSerialization<Method> {

        /**
         *
         */
        private static final long serialVersionUID = 7724035123604282695L;

        /**
         * A serializable reference to the classes of the parameter types of the
         * method this {@link SerializableMethod} represents.
         */
        private Class<?>[] parameterTypes;

        public MethodSerializationInfo(Method method) {
            super(method);
            parameterTypes = method.getParameterTypes();
        }

        private Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

    }

    /**
     * Constructs a serializable wrapper for the given method.
     *
     * @param method
     *            that should be wrapped in a serializable way.
     *
     * @since 1.0.0;
     */
    public SerializableMethod(Method method) {
        super(requireNonNull(method));
    }

    protected Method getMethod(Class<?> declaringClass, String methodName,
                               Class<?>[] parameterTypes) throws NoSuchMethodException {
        Method method = declaringClass.getDeclaredMethod(methodName,
                parameterTypes);
        return method;
    }

    @Override
    protected Serializable serialize(Method nonSerializableObject) {
        MethodSerializationInfo methodSerializationInfo = new MethodSerializationInfo(
                nonSerializableObject);
        return methodSerializationInfo;
    }

    @Override
    protected Method deserialize(Serializable restoreInfo) throws Exception {
        MethodSerializationInfo methodSerializationInfo = (MethodSerializationInfo) restoreInfo;
        Class<?> declaringClass = methodSerializationInfo.getDeclaringClass();
        String methodName = methodSerializationInfo.getMemberName();
        Class<?>[] parameterTypes = methodSerializationInfo.getParameterTypes();
        Method method = getMethod(declaringClass, methodName,
                parameterTypes);
        return method;
    }
}