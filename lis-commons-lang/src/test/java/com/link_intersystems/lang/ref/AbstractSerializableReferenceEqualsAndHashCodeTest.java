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

import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.EqualsAndHashCodeTest;

public class AbstractSerializableReferenceEqualsAndHashCodeTest extends
		EqualsAndHashCodeTest {

	public static final String TEST1 = "Test1";

	public static final String TEST2 = "Test2";

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected Object createInstance() throws Exception {
		AbstractSerializableReference<String> abstractSerializableReference = new TestSerializableReference(
				"Test");
		return abstractSerializableReference;
	}

	@Override
	protected Object createNotEqualInstance() throws Exception {
		AbstractSerializableReference<String> abstractSerializableReference = new TestSerializableReference();
		return abstractSerializableReference;
	}

	@Test
	public void toStringTest() {
		AbstractSerializableReference<String> abstractSerializableReference = new TestSerializableReference(
				"Test");
		abstractSerializableReference.toString();
	}

	@Test
	public void toStringForNullTest() {
		AbstractSerializableReference<String> abstractSerializableReference = new TestSerializableReference(
				null);
		abstractSerializableReference.toString();
	}

	private static class TestSerializableReference extends
			AbstractSerializableReference<String> {

		/**
		 *
		 */
		private static final long serialVersionUID = -627193017011155130L;

		public TestSerializableReference() {
		}

		public TestSerializableReference(String transientReferent) {
			super(transientReferent);
		}

		@Override
		protected Serializable serialize(String nonSerializableObject) {
			return nonSerializableObject;
		}

		@Override
		protected String deserialize(Serializable restoreInfo) {
			return restoreInfo.toString();
		}
	};

}
