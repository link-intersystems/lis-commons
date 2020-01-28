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

import static junit.framework.Assert.assertNull;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class NullReferenceTest {

	@Test
	public void isNull() {
		SerializableReference<Object> instance = NullReference.getInstance();
		Object object = instance.get();
		assertNull(object);
	}

	@Test
	public void serialization() {
		SerializableReference<Object> instance = NullReference.getInstance();
		SerializableReference<Object> clone = (SerializableReference<Object>) SerializationUtils
				.clone(instance);
		Object object = clone.get();
		assertNull(object);
	}

	@Test
	public void serializationMethods() {
		NullReference<Object> nullReference = new NullReference<Object>();
		Serializable serialize = nullReference.serialize(null);
		assertNull(serialize);
		Object deserialize = nullReference.deserialize(serialize);
		assertNull(deserialize);
	}
}
