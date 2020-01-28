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
package com.link_intersystems.lang.ref;

/**
 * Represents a reference to an object. In contrast to the
 * {@link java.lang.ref.Reference} provided by standard java, this reference is
 * intended to be implemented by clients. Every time you need an indirect
 * reference to another object (lazy loading,etc.) a {@link Reference} is the
 * simplest way to implement this. The return value of {@link Reference#get()}
 * should never be cached by a client. Instead the client should call
 * {@link #get()} whenever it needs a referent and clients must be prepared to
 * handle referent changes.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T>
 *            the reference object's referent type.
 * 
 * @since 1.0.0.0
 * @version 1.2.0.0
 */
public interface Reference<T> {

	/**
	 * 
	 * @return reference object's referent.
	 * @since 1.0.0.0
	 */
	public T get();
}
