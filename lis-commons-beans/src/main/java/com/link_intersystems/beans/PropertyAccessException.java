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
 * Thrown whenever an access to a {@link Property} failed of some reason.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @since 1.2.0.0
 * 
 */
public class PropertyAccessException extends PropertyException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4095841875394979112L;
	private final PropertyAccessType propertyAccessType;
	private Property<?> property;

	/**
	 * An enum that defines the type of property access.
	 * 
	 * @author René Link <a
	 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
	 *         intersystems.com]</a>
	 * @since 1.2.0.0
	 * 
	 */
	public enum PropertyAccessType {
		/**
		 * The {@link PropertyAccessType} that signals getter access.
		 * 
		 * @since 1.2.0.0
		 */
		READ,
		/**
		 * The {@link PropertyAccessType} that signals setter access.
		 * 
		 * @since 1.2.0.0
		 */
		WRITE;
	}

	PropertyAccessException(Property<?> property,
			PropertyAccessType propertyAccessType) {
		super(property);
		this.property = property;
		this.propertyAccessType = propertyAccessType;
	}

	PropertyAccessException(Property<?> property,
			PropertyAccessType propertyAccessType, Throwable cause) {
		super(property, cause);
		this.property = property;
		this.propertyAccessType = propertyAccessType;
	}

	/**
	 * The type of access that caused this {@link PropertyAccessException}.
	 * 
	 * @return type of access that caused this {@link PropertyAccessException}.
	 * @since 1.2.0.0
	 */
	public PropertyAccessType getPropertyAccessType() {
		return propertyAccessType;
	}

	@Override
	public String getLocalizedMessage() {
		String message = null;

		if (PropertyAccessType.READ.equals(propertyAccessType)) {
			message = Messages.formatPropertyNotReadable(property);
		} else {
			message = Messages.formatPropertyNotWritable(property);
		}

		return message;
	}

}
