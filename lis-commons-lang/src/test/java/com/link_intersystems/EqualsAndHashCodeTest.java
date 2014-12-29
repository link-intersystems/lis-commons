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
package com.link_intersystems;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import junit.framework.AssertionFailedError;
import junitx.framework.Assert;

import org.junit.Test;

public abstract class EqualsAndHashCodeTest {

	private Object eq1;
	private Object eq2;
	private Object eq3;
	private Object neq;
	private static final int NUM_ITERATIONS = 20;

	/**
	 * Creates and returns an instance of the class under test.
	 *
	 * @return a new instance of the class under test; each object returned from
	 *         this method should compare equal to each other.
	 * @throws Exception
	 */
	protected abstract Object createInstance() throws Exception;

	/**
	 * Creates and returns an instance of the class under test.
	 *
	 * @return a new instance of the class under test; each object returned from
	 *         this method should compare equal to each other, but not to the
	 *         objects returned from {@link #createInstance() createInstance}.
	 * @throws Exception
	 */
	protected abstract Object createNotEqualInstance() throws Exception;

	/**
	 * Sets up the test fixture.
	 *
	 * @throws Exception
	 */
	protected void setUp() throws Exception {
		eq1 = createInstance();
		eq2 = createInstance();
		eq3 = createInstance();
		neq = createNotEqualInstance();

		// We want these assertions to yield errors, not failures.
		try {
			assertNotNull("createInstance() returned null", eq1);
			assertNotNull("2nd createInstance() returned null", eq2);
			assertNotNull("3rd createInstance() returned null", eq3);
			assertNotNull("createNotEqualInstance() returned null", neq);

			assertNotSame(eq1, eq2);
			assertNotSame(eq1, eq3);
			assertNotSame(eq1, neq);
			assertNotSame(eq2, eq3);
			assertNotSame(eq2, neq);
			assertNotSame(eq3, neq);

			assertEquals("1st and 2nd equal instances of different classes",
					eq1.getClass(), eq2.getClass());
			assertEquals("1st and 3rd equal instances of different classes",
					eq1.getClass(), eq3.getClass());
			assertEquals(
					"1st equal instance and not-equal instance of different classes",
					eq1.getClass(), neq.getClass());
		} catch (AssertionFailedError ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * Tests whether <code>equals</code> holds up against a new
	 * <code>Object</code> (should always be <code>false</code>).
	 */
	@Test
	public final void equalsAgainstNewObject() {
		Object o = new Object();

		Assert.assertNotEquals(eq1, o);
		Assert.assertNotEquals(eq2, o);
		Assert.assertNotEquals(eq3, o);
		Assert.assertNotEquals(neq, o);
	}

	/**
	 * Tests whether <code>equals</code> holds up against <code>null</code>.
	 */
	@Test
	public final void equalsAgainstNull() {
		Assert.assertNotEquals("1st vs. null", eq1, null);
		Assert.assertNotEquals("2nd vs. null", eq2, null);
		Assert.assertNotEquals("3rd vs. null", eq3, null);
		Assert.assertNotEquals("not-equal vs. null", neq, null);
	}

	/**
	 * Tests whether <code>equals</code> holds up against objects that should
	 * not compare equal.
	 */
	@Test
	public final void equalsAgainstUnequalObjects() {
		Assert.assertNotEquals("1st vs. not-equal", eq1, neq);
		Assert.assertNotEquals("2nd vs. not-equal", eq2, neq);
		Assert.assertNotEquals("3rd vs. not-equal", eq3, neq);

		Assert.assertNotEquals("not-equal vs. 1st", neq, eq1);
		Assert.assertNotEquals("not-equal vs. 2nd", neq, eq2);
		Assert.assertNotEquals("not-equal vs. 3rd", neq, eq3);
	}

	/**
	 * Tests whether <code>equals</code> is <em>consistent</em>.
	 */
	@Test
	public final void equalsIsConsistentAcrossInvocations() {
		for (int i = 0; i < NUM_ITERATIONS; ++i) {
			equalsAgainstNewObject();
			equalsAgainstNull();
			equalsAgainstUnequalObjects();
			equalsIsReflexive();
			equalsIsSymmetricAndTransitive();
		}
	}

	/**
	 * Tests whether <code>equals</code> is <em>reflexive</em>.
	 */
	@Test
	public final void equalsIsReflexive() {
		assertEquals("1st equal instance", eq1, eq1);
		assertEquals("2nd equal instance", eq2, eq2);
		assertEquals("3rd equal instance", eq3, eq3);
		assertEquals("not-equal instance", neq, neq);
	}

	/**
	 * Tests whether <code>equals</code> is <em>symmetric</em> and
	 * <em>transitive</em>.
	 */
	@Test
	public final void equalsIsSymmetricAndTransitive() {
		assertEquals("1st vs. 2nd", eq1, eq2);
		assertEquals("2nd vs. 1st", eq2, eq1);

		assertEquals("1st vs. 3rd", eq1, eq3);
		assertEquals("3rd vs. 1st", eq3, eq1);

		assertEquals("2nd vs. 3rd", eq2, eq3);
		assertEquals("3rd vs. 2nd", eq3, eq2);
	}

	/**
	 * Tests the <code>hashCode</code> contract.
	 */
	@Test
	public final void hashCodeContract() {
		assertEquals("1st vs. 2nd", eq1.hashCode(), eq2.hashCode());
		assertEquals("1st vs. 3rd", eq1.hashCode(), eq3.hashCode());
		assertEquals("2nd vs. 3rd", eq2.hashCode(), eq3.hashCode());
	}

	/**
	 * Tests the consistency of <code>hashCode</code>.
	 */
	@Test
	public final void hashCodeIsConsistentAcrossInvocations() {
		int eq1Hash = eq1.hashCode();
		int eq2Hash = eq2.hashCode();
		int eq3Hash = eq3.hashCode();
		int neqHash = neq.hashCode();

		for (int i = 0; i < NUM_ITERATIONS; ++i) {
			assertEquals("1st equal instance", eq1Hash, eq1.hashCode());
			assertEquals("2nd equal instance", eq2Hash, eq2.hashCode());
			assertEquals("3rd equal instance", eq3Hash, eq3.hashCode());
			assertEquals("not-equal instance", neqHash, neq.hashCode());
		}
	}

	/**
	 * Tests the consistency of <code>hashCode</code>.
	 *
	 * @throws Exception
	 */
	@Test
	public final void equalsWithIncompatibleObjectType() throws Exception {
		boolean equals = eq1.equals(createIncompatibleObjectType());
		assertFalse(
				"equal instance should not be equal to incompatible object type",
				equals);
	}

	protected Object createIncompatibleObjectType() throws Exception {
		return this;
	}

}
