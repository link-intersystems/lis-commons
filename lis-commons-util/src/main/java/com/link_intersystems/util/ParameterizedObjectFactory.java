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
package com.link_intersystems.util;

/**
 * A factory that can create objects based on parameter object.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 *            the type of the objects this {@link ParameterizedObjectFactory}
 *            creates.
 * @param <P>
 *            the type of the parameter object used when creating an object.
 * @see ObjectFactory
 * @since 1.0.0;
 */
@FunctionalInterface
public interface ParameterizedObjectFactory<T, P> {

    /**
     * Creates a key for the provided value.
     *
     * @param parameter
     *            the parameter object for creating an object of OBEJCT_TYPE.
     * @return an object of OBJECT_TYPE that was created using the paramter
     *         object.
     */
    public T getObject(P parameter);
}
