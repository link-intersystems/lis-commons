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
package com.link_intersystems.lang;

/**
 * Adapter that clients may subclass and override the necessary methods. All
 * methods implemented by this adapter do nothing at all.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * 
 * @since 1.0.0.0
 * 
 */
public class PrimitiveCallbackAdapter implements PrimitiveCallback {

	public void primitive(boolean value) {
	}

	public void primitive(byte value) {
	}

	public void primitive(char value) {
	}

	public void primitive(short value) {
	}

	public void primitive(int value) {
	}

	public void primitive(long value) {
	}

	public void primitive(float value) {
	}

	public void primitive(double value) {
	}

}
