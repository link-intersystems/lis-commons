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
package com.link_intersystems.lang.reflect;

import static junit.framework.Assert.assertEquals;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class SerializablePackageTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullConstructor() {
		new SerializablePackage(null);
	}

	@Test
	public void serialize() {
		Package packageObject = SerializablePackageTest.class.getPackage();
		SerializablePackage serializablePackage = new SerializablePackage(
				packageObject);

		SerializablePackage deserialized = (SerializablePackage) SerializationUtils
				.clone(serializablePackage);

		Package deserializedPackageObject = deserialized.get();

		assertEquals(packageObject, deserializedPackageObject);
	}

	@Test(expected = SerializationException.class)
	public void classNotFound() throws Exception {
		Package packageObject = SerializablePackageTest.class.getPackage();
		String name = packageObject.getName();
		Whitebox.setInternalState(packageObject, String.class,
				"packagepath.that.does.not.exists", Package.class);
		try {
			SerializablePackage serializablePackage = new SerializablePackage(
					packageObject);
			SerializablePackage deserialized = (SerializablePackage) SerializationUtils
					.clone(serializablePackage);
			deserialized.get();
		} finally {
			Whitebox.setInternalState(packageObject, String.class, name,
					Package.class);
		}
	}
}
