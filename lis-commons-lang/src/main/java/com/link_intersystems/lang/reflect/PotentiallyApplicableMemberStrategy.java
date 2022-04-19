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
package com.link_intersystems.lang.reflect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements the algorithm to identify if a {@link Member2} is applicable for
 * some {@link AccessType}s and invocation parameter objects or invocation
 * parameter classes as defined by the java language specification, chapter
 * 15.12.2.1.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0;
 */
class PotentiallyApplicableMemberStrategy {

    public <C extends Member2<?>> List<C> getPotentialApplicable(
            List<C> candidates,
            PotentiallyApplicableCriteria<C> potentiallyApplicableCriteria) {
        List<C> potentiallyApplicable = new ArrayList<C>(
                candidates);
        Iterator<C> potentiallyApplicableIterator = potentiallyApplicable
                .iterator();
        while (potentiallyApplicableIterator.hasNext()) {
            C candidate = potentiallyApplicableIterator.next();
            if (!potentiallyApplicableCriteria
                    .isPotentiallyApplicable(candidate)) {
                potentiallyApplicableIterator.remove();
            }
        }

        return potentiallyApplicable;
    }

    static interface PotentiallyApplicableCriteria<RT extends Member2<?>> {

        public boolean isPotentiallyApplicable(RT member2);
    }

    /**
     * Abstract base class that checks if a {@link Member2} is potentially
     * applicable for the {@link AccessType}s and invocation parameters objects
     * or classes it was constructed with by using the
     * {@link Member2#isAccessible(AccessType...)} and
     * {@link Member2#isApplicable(Class[])} or
     * {@link Member2#isApplicable(Object[])} methods.
     *
     * @author René Link <a
     *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     *         intersystems.com]</a>
     *
     * @param <C>
     * @since 1.2.0;
     */
    private abstract static class AbstractPotentionallyApplicableCriteria<C extends Member2<?>>
            implements PotentiallyApplicableCriteria<C> {

        protected AccessType[] accessTypes;
        protected Object[] invocationParameters;
        protected Class<?>[] invocationParameterClasses;

        protected AbstractPotentionallyApplicableCriteria(
                AccessType[] accessTypes, Object... invocationParameters) {
            this.accessTypes = accessTypes.clone();
            this.invocationParameters = invocationParameters.clone();
        }

        protected AbstractPotentionallyApplicableCriteria(
                AccessType[] accessTypes,
                Class<?>... invocationParameterClasses) {
            this.accessTypes = accessTypes.clone();
            this.invocationParameterClasses = invocationParameterClasses
                    .clone();
        }

        public boolean isPotentiallyApplicable(C candidate) {
            boolean accessible = candidate.isAccessible(accessTypes);
            if (!accessible) {
                return false;
            }
            boolean isApplicable = false;

            if (invocationParameters == null) {
                isApplicable = candidate
                        .isApplicable(invocationParameterClasses);
            } else {
                isApplicable = candidate.isApplicable(invocationParameters);
            }

            return isApplicable;
        }

    }

    /**
     * Implements the criteria that evaluates to true if the
     * {@link Constructor2} it is evaluated against is applicable for the
     * {@link AccessType}s and invocation parameters objects or classes it was
     * constructed with. This criteria implements the java language
     * specification chapter 15.9.3.
     *
     * @author René Link <a
     *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     *         intersystems.com]</a>
     * @since 1.2.0;
     */
    static class PotentionallyApplicableConstructorCriteria<T> extends
            AbstractPotentionallyApplicableCriteria<Constructor2<T>> {

        public PotentionallyApplicableConstructorCriteria(
                AccessType[] accessTypes,
                Class<?>... invocationParameterClasses) {
            super(accessTypes, invocationParameterClasses);
        }

        public PotentionallyApplicableConstructorCriteria(
                AccessType[] accessTypes, Object... invocationParameters) {
            super(accessTypes, invocationParameters);
        }

    }

    /**
     * Implements the criteria that evaluates to true if the {@link Method2} it
     * is evaluated against is applicable for the {@link AccessType}s and
     * invocation parameters objects or classes it was constructed with. This
     * criteria implements the java language specification chapter 15.12.2.1.
     *
     * @author René Link <a
     *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     *         intersystems.com]</a>
     * @since 1.2.0;
     */
    static class PotentionallyApplicableMethodCriteria extends
            AbstractPotentionallyApplicableCriteria<Method2> {

        private final String name;

        public PotentionallyApplicableMethodCriteria(String name,
                                                     AccessType[] accessTypes,
                                                     Class<?>... invocationParameterClasses) {
            super(accessTypes, invocationParameterClasses);
            this.name = name;
        }

        public PotentionallyApplicableMethodCriteria(String name,
                                                     AccessType[] accessTypes, Object... invocationParameters) {
            super(accessTypes, invocationParameters);
            this.name = name;
        }

        public boolean isPotentiallyApplicable(Method2 method2) {
            boolean nameEqual = method2.getName().equals(name);
            if (!nameEqual) {
                return false;
            }

            return super.isPotentiallyApplicable(method2);
        }
    }
}
