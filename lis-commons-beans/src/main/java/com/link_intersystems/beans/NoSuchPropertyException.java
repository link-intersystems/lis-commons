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

/**
 * Thrown when a {@link Property} is requested, but the {@link Property} does
 * not exist in the context (bean class) it is requested.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @since 1.2.0;
 * 
 */
public class NoSuchPropertyException extends PropertyException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1234841254431039123L;

	NoSuchPropertyException(Class<?> beanType, String propertyName) {
		super(beanType, propertyName);
	}

	@Override
	public String getLocalizedMessage() {
		String localizedMessage = Messages.formatNoSuchProperty(beanType,
				propertyName);
		return localizedMessage;
	}
}
