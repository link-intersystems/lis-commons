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

import java.io.Serializable;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.util.SerializationException;
import com.link_intersystems.lang.ref.AbstractSerializableReference;

class SerializablePackage extends AbstractSerializableReference<Package> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7408462485150280625L;

	public SerializablePackage(Package packageObject) {
		super(packageObject);
		Assert.notNull("packageObject", packageObject);
	}

	@Override
	protected Serializable serialize(Package nonSerializableObject2) {
		return nonSerializableObject2.getName();
	}

	@Override
	protected Package deserialize(Serializable restoreInfo) {
		String packageName = (String) restoreInfo;
		Package packageObject = Package.getPackage(packageName);
		if (packageObject == null) {
			throw new SerializationException("Unable to restore package "
					+ packageName);
		}
		return packageObject;
	}

}