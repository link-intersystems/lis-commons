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
package com.link_intersystems.lang.reflect.criteria;

import java.util.Iterator;
import java.util.function.Predicate;

import static com.link_intersystems.util.Iterators.filtered;
import static java.util.Objects.requireNonNull;

/**
 * An {@link ElementCriteria} provides selection, and filtering capabilities for
 * member based criteria. All{@link ElementCriteria}s provide methods to obtain an {@link Iterable}.
 * After the {@link Iterable} is constructed it ensures that all {@link Iterator}s
 * it constructs apply the same iteration rules. These are the rules of the critera at
 * construction time of the {@link Iterable}. This rule is ensured for properties of the criteria.
 * It can not be ensured for properties of dependent objects, e.g. for {@link Predicate}s that have state
 * like a unique predicate and are added using {@link #add(Predicate)}.
 *
 * <p>
 * <pre>
 * ClassCriteria criteria = new ClassCriteria();
 * criteria.setTraverseOrder(TraverseOrder.INTERFACES_FIRST);
 * Iterator&lt;Class&lt;?&gt;&gt; classIterator = criteria.getIterable(ArrayList.class)
 * 		.iterator();
 * /*
 *  * Should not have an effect on the iterator or iterable created before
 *  &#42;/
 * criteria.setTraverseOrder(TraverseOrder.SUPERCLASS_ONLY);
 * criteria.add(new AssignablePredicate(AbstractList.class));
 * criteria.setResultSelection(Selection.LAST);
 *
 * Iterator&lt;Class&lt;?&gt;&gt; secondIterator = criteria.getIterable(ArrayList.class)
 * 		.iterator();
 * </pre>
 *
 * </p>
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
public abstract class ElementCriteria<T> implements Cloneable {

    private Predicate<T> elementFilterPredicate = null;

    private Result select = Result.ALL;

    public ElementCriteria() {
    }

    /**
     * Use the defined predicate as a filter when searching for elements.
     * Multiple calls to this method will combine the predicates with a logical
     * 'and' operator. The type of objects that the {@link Predicate} must be
     * able to handle is defined by subclasses.
     *
     * @param predicate the predicate to use when filtering elements.
     * @since 1.0.0;
     */
    public void add(Predicate<T> predicate) {
        if (elementFilterPredicate == null) {
            elementFilterPredicate = requireNonNull(predicate);
        } else {
            elementFilterPredicate = elementFilterPredicate.and(requireNonNull(predicate));
        }
    }

    /**
     * Takes the iterator and wraps it into a filtering iterator that applies
     * the {@link Predicate}s that were configured via {@link #add(Predicate)}.
     *
     * @param iterator the iterator that the filter should be applied on.
     * @return an {@link Iterator} that filters the elements of the given
     * {@link Iterator} according to the filter {@link Predicate}s
     * defined by {@link #add(Predicate)}.
     * @since 1.0.0;
     */
    protected Iterator<T> applyElementFilter(Iterator<T> iterator) {
        if (elementFilterPredicate == null) {
            return iterator;
        } else {
            return filtered(iterator, elementFilterPredicate);
        }
    }

    /**
     * Takes the iterator and wraps it into a filtering iterator that applies
     * the selection requirements as they are configured via
     * {@link #setResult(Result)}.
     *
     * @param iterator the iterator that the filter should be applied on.
     * @return an {@link Iterator} that selects the elements of the given
     * {@link Iterator} according to selection as specified by
     * {@link #setResult(Result)}.
     * @since 1.0.0;
     */
    protected Iterator<T> applySelectionFilter(final Iterator<T> iterator) {
        return select.apply(iterator);
    }

    /**
     * Only select elements as described by the {@link Result} argument.
     *
     * @param select specifies which matched elements will be returned. Defaults to
     *               {@link Result#ALL}
     * @since 1.0.0;
     */
    public void setResult(Result select) {
        this.select = requireNonNull(select);
    }

    @Override
    protected ElementCriteria<T> clone() {
        try {
            return (ElementCriteria<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
