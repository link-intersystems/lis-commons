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
package com.link_intersystems.lang.ref;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Reference} that implements the null object pattern.
 *
 * @param <T>
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @see <a href="http://www.refactoring.com/catalog/introduceNullObject.html">...</a>
 */
public class NullSafeReference<T> implements Reference<T> {

    private T nullInstance;

    private Reference<T> referentRef = NullReference.getInstance();

    /**
     * The null object instance in terms of the null object pattern as described
     * by http://www.refactoring.com/catalog/introduceNullObject.html.
     *
     * @param nullInstance the null object instance in terms of the null object pattern.
     *                     Must never be <code>null</code>
     * @since 1.2.0;
     */
    public NullSafeReference(T nullInstance) {
        this.nullInstance = requireNonNull(nullInstance);
    }

    /**
     * Sets the referent of this {@link NullSafeReference}. If the referent is
     * <code>null</code> the null object, that this {@link NullSafeReference}
     * was constructed with, will be returned by {@link #get()}. Otherwise the
     * referent.
     *
     * @param referent the referent to set. Can be <code>null</code>
     * @since 1.2.0;
     */
    public void setReferent(T referent) {
        if (referent == null) {
            referentRef = NullReference.getInstance();
        } else {
            referentRef = new HardReference<>(referent);
        }
    }

    /**
     * Sets the referent as another {@link Reference} . If the referent returned
     * by the {@link Reference} is <code>null</code> the null object, that this
     * {@link NullSafeReference} was constructed with, will be returned by
     * {@link #get()}. Otherwise the referent.
     *
     * @param referentRef the referent to set. Can be <code>null</code>
     * @since 1.2.0;
     */
    public void setReferent(Reference<T> referentRef) {
        this.referentRef = referentRef;
    }

    /**
     * @inherited
     * @since 1.2.0;
     */
    public T get() {
        T referent = referentRef.get();
        if (referent == null) {
            referent = nullInstance;
        }
        return referent;
    }

}
