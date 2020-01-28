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
 * An object factory is responsible for creating objects of some type.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 * @see ParameterizedObjectFactory
 * @since 1.0.0.0
 */
public interface ObjectFactory<T> {

	/**
	 *
	 * @return a new instance of the type that this {@link ObjectFactory}
	 *         creates instances of. Don't be confused about this method's name
	 *         it is only named {@link #getObject()} to be complaint with the
	 *         java bean specification. Nevertheless this method creates
	 *         objects.
	 * @since 1.0.0.0
	 */
	public T getObject();
}
