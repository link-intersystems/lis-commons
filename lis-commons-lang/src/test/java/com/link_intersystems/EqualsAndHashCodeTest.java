/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;

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
     * this method should compare equal to each other.
     */
    protected abstract Object createInstance() throws Exception;

    /**
     * Creates and returns an instance of the class under test.
     *
     * @return a new instance of the class under test; each object returned from
     * this method should compare equal to each other, but not to the
     * objects returned from {@link #createInstance() createInstance}.
     */
    protected abstract Object createNotEqualInstance() throws Exception;

    /**
     * Sets up the test fixture.
     */
    @BeforeEach
    public void createTestInstances() throws Exception {
        eq1 = createInstance();
        eq2 = createInstance();
        eq3 = createInstance();
        neq = createNotEqualInstance();

        // We want these assertions to yield errors, not failures.
        try {
            assertNotNull(eq1, "createInstance() returned null");
            assertNotNull(eq2, "2nd createInstance() returned null");
            assertNotNull(eq3, "3rd createInstance() returned null");
            assertNotNull(neq, "createNotEqualInstance() returned null");

            assertNotSame(eq1, eq2);
            assertNotSame(eq1, eq3);
            assertNotSame(eq1, neq);
            assertNotSame(eq2, eq3);
            assertNotSame(eq2, neq);
            assertNotSame(eq3, neq);

            assertEquals(eq1.getClass(), eq2.getClass(), "1st and 2nd equal instances of different classes");
            assertEquals(eq1.getClass(), eq3.getClass(), "1st and 3rd equal instances of different classes");
            assertEquals(eq1.getClass(), neq.getClass(), "1st equal instance and not-equal instance of different classes");
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

        assertNotEquals(eq1, o);
        assertNotEquals(eq2, o);
        assertNotEquals(eq3, o);
        assertNotEquals(neq, o);
    }

    /**
     * Tests whether <code>equals</code> holds up against <code>null</code>.
     */
    @Test
    public final void equalsAgainstNull() {
        assertNotEquals(eq1, null, "1st vs. null");
        assertNotEquals(eq2, null, "2nd vs. null");
        assertNotEquals(eq3, null, "3rd vs. null");
        assertNotEquals(neq, null, "not-equal vs. null");
    }

    /**
     * Tests whether <code>equals</code> holds up against objects that should
     * not compare equal.
     */
    @Test
    public final void equalsAgainstUnequalObjects() {
        assertNotEquals(eq1, neq, "1st vs. not-equal");
        assertNotEquals(eq2, neq, "2nd vs. not-equal");
        assertNotEquals(eq3, neq, "3rd vs. not-equal");

        assertNotEquals(neq, eq1, "not-equal vs. 1st");
        assertNotEquals(neq, eq2, "not-equal vs. 2nd");
        assertNotEquals(neq, eq3, "not-equal vs. 3rd");
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
        assertEquals(eq1, eq1, "1st equal instance");
        assertEquals(eq2, eq2, "2nd equal instance");
        assertEquals(eq3, eq3, "3rd equal instance");
        assertEquals(neq, neq, "not-equal instance");
    }

    /**
     * Tests whether <code>equals</code> is <em>symmetric</em> and
     * <em>transitive</em>.
     */
    @Test
    public final void equalsIsSymmetricAndTransitive() {
        assertEquals(eq1, eq2, "1st vs. 2nd");
        assertEquals(eq2, eq1, "2nd vs. 1st");

        assertEquals(eq1, eq3, "1st vs. 3rd");
        assertEquals(eq3, eq1, "3rd vs. 1st");

        assertEquals(eq2, eq3, "2nd vs. 3rd");
        assertEquals(eq3, eq2, "3rd vs. 2nd");
    }

    /**
     * Tests the <code>hashCode</code> contract.
     */
    @Test
    public final void hashCodeContract() {
        assertEquals(eq1.hashCode(), eq2.hashCode(), "1st vs. 2nd");
        assertEquals(eq1.hashCode(), eq3.hashCode(), "1st vs. 3rd");
        assertEquals(eq2.hashCode(), eq3.hashCode(), "2nd vs. 3rd");
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
            assertEquals(eq1Hash, eq1.hashCode(), "1st equal instance");
            assertEquals(eq2Hash, eq2.hashCode(), "2nd equal instance");
            assertEquals(eq3Hash, eq3.hashCode(), "3rd equal instance");
            assertEquals(neqHash, neq.hashCode(), "not-equal instance");
        }
    }

    /**
     * Tests the consistency of <code>hashCode</code>.
     */
    @Test
    public final void equalsWithIncompatibleObjectType() {
        boolean equals = eq1.equals(createIncompatibleObjectType());
        assertFalse(equals, "equal instance should not be equal to incompatible object type");
    }

    protected Object createIncompatibleObjectType() {
        return this;
    }

}
