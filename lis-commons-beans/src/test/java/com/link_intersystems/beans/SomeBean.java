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

public class SomeBean {

	@SuppressWarnings("unused")
	private String writeOnlyProperty;

	private String readOnlyProperty;

	private String stringProperty;

	private String[] writeOnlyIndexedProperty = new String[0];

	private String[] readOnlyIndexedProperty = new String[0];

	private String[] arrayPropertyNoIndexAccess = new String[0];

	private String[] indexedPropertyWriteOnlyIndexOnlyAccess = new String[0];

	private String[] indexedPropertyReadOnlyIndexOnlyAccess = new String[2];

	private String[] stringArrayProperty = new String[0];

	public String getStringProperty() {
		return stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public String[] getStringArrayProperty() {
		return stringArrayProperty;
	}

	public void setStringArrayProperty(String[] stringArray) {
		this.stringArrayProperty = stringArray.clone();
	}

	public String getStringArrayProperty(int index) {
		return stringArrayProperty[index];
	}

	public void setStringArrayProperty(int index, String stringValue) {
		this.stringArrayProperty[index] = stringValue;
	}

	public String getReadOnlyProperty() {
		return readOnlyProperty;
	}

	public void setWriteOnlyProperty(String writeOnlyProperty) {
		this.writeOnlyProperty = writeOnlyProperty;
	}

	public void setWriteOnlyIndexedProperty(String[] writeOnlyIndexedProperty) {
		this.writeOnlyIndexedProperty = writeOnlyIndexedProperty;
	}

	public void setWriteOnlyIndexedProperty(int index, String value) {
		this.writeOnlyIndexedProperty[index] = value;
	}

	public String getReadOnlyIndexedProperty(int index) {
		return readOnlyIndexedProperty[index];
	}

	public String[] getReadOnlyIndexedProperty() {
		return readOnlyIndexedProperty;
	}

	public String[] getArrayPropertyNoIndexAccess() {
		return arrayPropertyNoIndexAccess;
	}

	public void setArrayPropertyNoIndexAccess(String[] arrayPropertyNoIndexAccess) {
		this.arrayPropertyNoIndexAccess = arrayPropertyNoIndexAccess;
	}

	public void setIndexedPropertyWriteOnlyIndexOnlyAccess(int index, String value) {
		this.indexedPropertyWriteOnlyIndexOnlyAccess[index] = value;
	}

	public String getIndexedPropertyReadOnlyIndexOnlyAccess(int index) {
		return indexedPropertyReadOnlyIndexOnlyAccess[index];
	}

	protected void setIndexedPropertyReadOnlyIndexOnlyAccess(String[] indexedPropertyReadOnlyIndexOnlyAccess) {
		this.indexedPropertyReadOnlyIndexOnlyAccess = indexedPropertyReadOnlyIndexOnlyAccess;
	}
}
