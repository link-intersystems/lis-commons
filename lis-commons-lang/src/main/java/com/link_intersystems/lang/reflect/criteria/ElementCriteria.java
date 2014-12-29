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

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AndPredicate;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.ref.SerializableReference;
import com.link_intersystems.util.SerializableTemplateObjectFactory;

/**
 * An {@link ElementCriteria} provides selection, and filtering capabilities for
 * concrete critirias. Every criteria must be {@link Serializable} to be able to
 * freeze it's state at any time (done via
 * {@link SerializableTemplateObjectFactory}). This is necessary to ensure that
 * criterias will not alter their behavior when they are in use. All
 * {@link ElementCriteria}s provide methods to obtain an {@link Iterable}. After
 * the {@link Iterable} is constructed it ensures that all {@link Iterator}s
 * that it constructs behave in the way the criteria was specified at
 * construction time of the {@link Iterable}.
 * <p>
 * {@link Serializable} is a way to freeze an object's state at any time.
 * Furthermore the use of the serializable strategy also empowers
 * {@link ElementCriteria}s to be transferred in a distributed environment, used
 * in permanent caches, web environment (session etc.) and so on. If you want to
 * add a Predicate that must hold a reference to an object that is not
 * serializable but can be reconstructed on other information (canoncial name,
 * data obtained from a database or web service) you can implement a
 * {@link SerializableReference}.
 * </p>
 *
 * <p>
 * <strong>ElementCriterias are Serializable to freeze their state</strong>
 *
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
 *
 * </pre>
 *
 * </p>
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public abstract class ElementCriteria<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4878247287543361989L;

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
	 * @param predicate
	 *            the predicate to use when filtering elements. The predicate
	 *            must implement {@link Serializable} to allow the
	 *            {@link ElementCriteria} to be serializable. For details take a
	 *            look at the {@link ElementCriteria} javadoc.
	 * @see AndPredicate
	 * @since 1.0.0.0
	 */
	public void add(Predicate<T> predicate) {
		Assert.notNull("predicate", predicate);
		if (!(predicate instanceof Serializable)) {
			/*
			 * We have to check if the predicate is a serializable. Of course
			 * you can enforce it via generics by declaring a type T as "T
			 * extends Predicate & Serializable". But this is not feasible
			 * because the commons-util apis (like AndPredicate.getInstance(..))
			 * only return a predicate and a Predicate is not Serializable. So
			 * if we would enforce it through generics all commons classes will
			 * not work.
			 */
			throw new IllegalArgumentException("predicate must be Serializable");
		}

		if (elementFilterPredicate == null) {
			elementFilterPredicate = predicate;
		} else {
			elementFilterPredicate = AndPredicate.andPredicate(
					elementFilterPredicate, predicate);
		}
	}

	/**
	 * Takes the iterator and wraps it into a filtering iterator that applies
	 * the {@link Predicate}s that were configured via {@link #add(Predicate)}.
	 *
	 * @param <T>
	 * @param iterator
	 * @return an {@link Iterator} that filters the elements of the given
	 *         {@link Iterator} according to the filter {@link Predicate}s
	 *         defined by {@link #add(Predicate)}.
	 * @since 1.0.0.0
	 */
	protected Iterator<T> applyElementFilter(Iterator<T> iterator) {
		if (elementFilterPredicate == null) {
			return iterator;
		} else {
			return IteratorUtils.filteredIterator(iterator,
					elementFilterPredicate);
		}
	}

	/**
	 * Takes the iterator and wraps it into a filtering iterator that applies
	 * the selection requirements as they are configured via
	 * {@link #setResult(Result)}.
	 *
	 * @param <T>
	 * @param iterator
	 * @return an {@link Iterator} that selects the elements of the given
	 *         {@link Iterator} according to selection as specified by
	 *         {@link #setResult(Result)}.
	 *
	 * @since 1.0.0.0
	 */
	protected Iterator<T> applySelectionFilter(final Iterator<T> iterator) {
		Iterator<T> result = null;
		switch (select) {
		case FIRST:
			Predicate<Object> firstPredicate = new Predicate<Object>() {

				private boolean first = true;

				public boolean evaluate(Object object) {
					if (first) {
						first = false;
						return true;
					}
					return false;
				}
			};
			result = IteratorUtils.filteredIterator(iterator, firstPredicate);
			break;
		case LAST:
			Predicate<Object> lastElementPredicate = new Predicate<Object>() {

				public boolean evaluate(Object object) {
					return !iterator.hasNext();
				}
			};
			result = IteratorUtils.filteredIterator(iterator,
					lastElementPredicate);
			break;
		case ALL:
			result = iterator;
			break;
		}
		return result;
	}

	/**
	 * Only select elements as described by the {@link Result} argument.
	 *
	 * @param select
	 *            specifies which matched elements will be returned. Defaults to
	 *            {@link Result#ALL}
	 * @since 1.0.0.0
	 */
	public void setResult(Result select) {
		Assert.notNull("select", select);
		this.select = select;
	}
}
