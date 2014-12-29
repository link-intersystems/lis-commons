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
package com.link_intersystems.util;

/**
 * This exception signals that in an context where unique keys are used, two
 * identical keys exist.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 */
public class KeyCollisionException extends RuntimeException {

	/**
     *
     */
	private static final long serialVersionUID = -8703472027541859562L;

	private final Object key;

	/**
	 * @return the key that causes the collision.
	 */
	public Object getKey() {
		return key;
	}

	public KeyCollisionException(Object key) {
		this.key = key;
	}

	public KeyCollisionException(String message, Object key) {
		super(message);
		this.key = key;
	}
}
