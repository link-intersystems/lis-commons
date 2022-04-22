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

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * A {@link MethodInvokingParameterizedObjectFactory} calls a method on each
 * parameter object and returns the result as the created object. This class is
 * based on the {@link MethodInvokingTransformer}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 *
 * @param <T>
 *            the type of object that is used as parameter object for this
 *            factory.
 * @param <METHOD_RETURN_TYPE>
 *            the return type of the factory method that is invoked on the
 *            parameter object.
 *
 * @since 1.0.0;
 * @see MethodInvokingTransformer
 */
public class MethodInvokingParameterizedObjectFactory<T, METHOD_RETURN_TYPE>
        implements ParameterizedObjectFactory<METHOD_RETURN_TYPE, T> {

    private MethodInvokingTransformer methodInvokingTransformer;

    /**
     * Creates a {@link MethodInvokingParameterizedObjectFactory} that invokes
     * the given method on objects of the object type when the
     * {@link #getObject(Object)} method is called.
     *
     * @param objectType
     * @param methodName
     *
     * @since 1.0.0;
     */
    public MethodInvokingParameterizedObjectFactory(
			Class<T> objectType, String methodName,
			Class<?>... parameterTypes) {
        requireNonNull(objectType);
        requireNonNull(methodName);
        requireNonNull(parameterTypes);
        Class2<T> class2 = Class2.get(objectType);
        Method2 applicableMethod = class2.getApplicableMethod(methodName,
                parameterTypes);
        if (applicableMethod == null) {
            throw new IllegalArgumentException("object " + objectType
                    + " doesn't have an applicable method named " + methodName
                    + " for parameter types " + Arrays.asList(parameterTypes));
        }
        methodInvokingTransformer = new MethodInvokingTransformer(
                applicableMethod);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public METHOD_RETURN_TYPE getObject(T parameter) {
        if (parameter == null) {
            return null;
        }
        Object returnObject = methodInvokingTransformer.apply(parameter);
        return (METHOD_RETURN_TYPE) returnObject;
    }
}
