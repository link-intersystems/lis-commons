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

import java.io.Serializable;

public class NullReference<T> extends AbstractSerializableReference<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5122539221094403724L;

	@SuppressWarnings("rawtypes")
	private static final NullReference INSTANCE = new NullReference();

	@SuppressWarnings("unchecked")
	public static <T> SerializableReference<T> getInstance() {
		return INSTANCE;
	}

	NullReference() {
	}

	@Override
	protected Serializable serialize(T nonSerializableObject) {
		return null;
	}

	@Override
	protected T deserialize(Serializable restoreInfo) {
		return null;
	}

}
