/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans;

import com.link_intersystems.lang.Assert;

/**
 * Base exception for all {@link Exception}s regard to {@link Property}s.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
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
		Assert.notNull("beanType", beanType);
		Assert.notNull("propertyName", propertyName);
		this.beanType = beanType;
		this.propertyName = propertyName;
	}

	protected PropertyException(Property<?> property) {
		this(property.getBean().getClass(), property.getName());
	}

	protected PropertyException(Property<?> property, Throwable cause) {
		this(property.getBean().getClass(), property.getName(), cause);
	}

	/**
	 * The bean's class that holds the property that caused this
	 * {@link PropertyException}.
	 * 
	 * @return bean's class that holds the property that caused this
	 *         {@link PropertyException}.
	 * @since 1.2.0;
	 */
	public Class<?> getBeanType() {
		return beanType;
	}

	/**
	 * The name of the property.
	 * 
	 * @return the name of the property.
	 * 
	 * @since 1.2.0;
	 */
	public String getPropertyName() {
		return propertyName;
	}

}