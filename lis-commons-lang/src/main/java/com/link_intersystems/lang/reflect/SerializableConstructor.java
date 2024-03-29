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
import java.lang.reflect.Constructor;

import static java.util.Objects.*;

/**
 * Represents a {@link Constructor} object {@link Reference} that is
 * {@link Serializable}.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class SerializableConstructor extends
        AbstractSerializableReference<Constructor<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -6636456646515992155L;

    private static class ConstructorSerializationInfo extends
            MemberSerialization<Constructor<?>> {

        /**
         *
         */
        private static final long serialVersionUID = -8447552263782991338L;
        private Class<?>[] parameterTypes;

        public ConstructorSerializationInfo(Constructor<?> constructor) {
            super(constructor);
            parameterTypes = constructor.getParameterTypes();
        }

        private Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

    }

    /**
     * Constructs a serializable wrapper for the given constructor.
     *
     * @param constructor that should be wrapped in a serializable way.
     * @since 1.0.0;
     */
    public SerializableConstructor(Constructor<?> constructor) {
        super(requireNonNull(constructor));
    }

    @Override
    protected Serializable serialize(Constructor<?> nonSerializableObject) {
        return new ConstructorSerializationInfo(nonSerializableObject);
    }

    @Override
    protected Constructor<?> deserialize(Serializable restoreInfo) throws Exception {
        ConstructorSerializationInfo constructorSerializationInfo = (ConstructorSerializationInfo) restoreInfo;
        Class<?> declaringClass = constructorSerializationInfo
                .getDeclaringClass();
        Class<?>[] parameterTypes = constructorSerializationInfo
                .getParameterTypes();

        Constructor<?> constructor = getConstructor(declaringClass,
                parameterTypes);
        return constructor;

    }

    protected Constructor<?> getConstructor(Class<?> declaringClass,
                                            Class<?>[] parameterTypes) throws NoSuchMethodException {
        Constructor<?> constructor = declaringClass
                .getDeclaredConstructor(parameterTypes);
        return constructor;
    }

}
