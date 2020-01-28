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
package com.link_intersystems.lang.reflect.criteria;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public abstract class ElementCriteriaTest {

	protected abstract ElementCriteria getElementCriteria();

	@Test(expected = IllegalArgumentException.class)
	public void nullResultSelection() {
		ElementCriteria criteria = getElementCriteria();
		criteria.setResult(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAddPredicate() {
		ElementCriteria criteria = getElementCriteria();
		criteria.add(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addNonSerializablePredicate() {
		ElementCriteria criteria = getElementCriteria();
		criteria.add(new SomePredicateNotSerializable());
	}

	@Test
	public void elementCriteriaDefaultConstructor() {
		/*
		 * Should not throw any exception
		 */
		new ElementCriteria() {

			/**
			 *
			 */
			private static final long serialVersionUID = -6057707149446648089L;
		};
	}

	@Test
	public void elementCriteriaSerializable() {
		SerializationUtils.clone(getElementCriteria());
	}

	private static class SomePredicateNotSerializable implements Predicate {

		public boolean evaluate(Object object) {
			return false;
		}

	}
}
