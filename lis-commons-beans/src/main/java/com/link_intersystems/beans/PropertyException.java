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
package com.link_intersystems.beans;

import static java.util.Objects.requireNonNull;

/**
 * Base exception for all {@link Exception}s regard to {@link Property}s.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public abstract class PropertyException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6903221576684428394L;
    protected final String propertyName;
    protected final Class<?> beanType;

    PropertyException(Class<?> beanType, String propertyName) {
        this(beanType, propertyName, null);
    }

    PropertyException(Class<?> beanType, String propertyName, Throwable cause) {
        super(cause);
        requireNonNull(beanType);
        requireNonNull(propertyName);
        this.beanType = beanType;
        this.propertyName = propertyName;
    }


    /**
     * The bean's class that holds the property that caused this
     * {@link PropertyException}.
     *
     * @return bean's class that holds the property that caused this
     * {@link PropertyException}.
     * @since 1.2.0;
     */
    public Class<?> getBeanType() {
        return beanType;
    }

    /**
     * The name of the property.
     *
     * @return the name of the property.
     * @since 1.2.0;
     */
    public String getPropertyName() {
        return propertyName;
    }

}