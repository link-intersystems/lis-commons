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
package com.link_intersystems.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;

/**
 * The {@link UtilFacade} class provides high level access to the collection
 * types in the com.link_intersystems.util package.
 *
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @since 1.0.0;
 */
public abstract class UtilFacade {

	private static final double HASH_MAP_LOAD_FACTOR = 1 / 0.75;
	private static final IdentityComparator<?> IDENTITY_COMPARATOR = new IdentityComparator<Object>();

	/**
	 * @param <T>
	 * @return an instance of the {@link IdentityComparator}. This method is
	 *         type-safe and memory friendly, because one instance of an
	 *         {@link IdentityComparator} can be shared by all components of
	 *         your application.
	 * @since 1.0.0;
	 */
	@SuppressWarnings("unchecked")
	public static <T> Comparator<T> identityComparator() {
		return (Comparator<T>) IDENTITY_COMPARATOR;
	}

	/**
	 * Takes a {@link Stack} object and returns an unmodifiable {@link Stack}
	 * instance. Unmodifiable means that every call on methods that will change
	 * the state of the {@link Stack} will throw an
	 * {@link UnsupportedOperationException}.
	 * <p>
	 * <strong>Methods of Stack that throw an
	 * {@link UnsupportedOperationException} s</strong><br>
	 * {@link Stack#pop()}<br/>
	 * {@link Stack#push(Object)}
	 * </p>
	 * <p>
	 * <strong>Methods of the Vector superclass that will throw an
	 * {@link UnsupportedOperationException}s</strong><br>
	 * {@link Vector#addElement(Object)}<br/>
	 * {@link Vector#removeAllElements()}<br/>
	 * {@link Vector#removeElement(Object)}<br/>
	 * {@link Vector#removeElementAt(int)}<br/>
	 * {@link Vector#setElementAt(Object, int)}<br/>
	 * {@link Vector#trimToSize()}<br/>
	 * {@link Vector#setSize(int)} <br/>
	 * </p>
	 * <p>
	 * <strong>Methods of the List interface that throw will an
	 * {@link UnsupportedOperationException}s</strong><br>
	 * {@link List#add(int, Object)}<br/>
	 * {@link List#addAll(Collection)}<br/>
	 * {@link List#remove(int)}<br/>
	 * {@link List#set(int, Object)}<br/>
	 * </p>
	 * <p>
	 * <strong>Methods of the Collection interface that will throw an
	 * {@link UnsupportedOperationException}s</strong><br>
	 * {@link Collection#add(Object)}<br/>
	 * {@link Collection#addAll(Collection)}<br/>
	 * {@link Collection#clear()}<br/>
	 * {@link Collection#remove(Object)}<br/>
	 * {@link Collection#removeAll(Collection)}<br/>
	 * {@link Collection#retainAll(Collection)}<br/>
	 * </p>
	 *
	 * @param <T>
	 *            the component type of the {@link Stack}
	 * @param stack
	 *            the stack object for which an unmodifiable instance should be
	 *            returned
	 * @return an unmodifiable instance of the given stack.
	 * @since 1.0.0;
	 */
	public static <T> Stack<T> unmodifiableStack(Stack<T> stack) {
		return new UnmodifiableStack<T>(stack);
	}

	/**
	 * Takes a collection of objects and builds a map that maps object's keys to
	 * objects. The object keys are calculated via a
	 * {@link ParameterizedObjectFactory}. The
	 * {@link ParameterizedObjectFactory} must ensure that a distinct key for
	 * every object instance in the collection is generated. If there are two
	 * instances that have the same key (in order to the key calculation
	 * algorithm) a {@link KeyCollisionException} is thrown.
	 *
	 * @param <KEY>
	 *            the key's type
	 * @param <VALUE>
	 *            the value's type
	 * @param objects
	 *            the {@link Collection} of objects the key map should be
	 *            generated of.
	 * @param keyObjectFactory
	 *            the {@link ParameterizedObjectFactory} to use for creating the
	 *            keys.
	 * @return a map that maps the keys generated by the
	 *         {@link ParameterizedObjectFactory} to the objects of the original
	 *         collection.
	 * @throws KeyCollisionException
	 *             if the collection contains two objects that are equal in
	 *             their key which is generated by the
	 *             {@link ParameterizedObjectFactory}.
	 * @since 1.0.0;
	 */
	public static <KEY, VALUE> Map<KEY, VALUE> keyMap(
			Collection<VALUE> objects,
			ParameterizedObjectFactory<KEY, VALUE> keyObjectFactory)
			throws KeyCollisionException {
		Map<KEY, VALUE> map = new HashMap<KEY, VALUE>(
				(int) (objects.size() * HASH_MAP_LOAD_FACTOR));
		for (VALUE v : objects) {
			KEY k = keyObjectFactory.getObject(v);
			if (map.containsKey(k)) {
				throw new KeyCollisionException(k);
			} else {
				map.put(k, v);
			}
		}
		return map;
	}

	/**
	 * A {@link Predicate} that only evaluates to true for the first time the
	 * {@link Predicate#test(Object)} method is called.
	 *
	 * @return a {@link Predicate} that only evaluates to true for the first
	 *         time the {@link Predicate#test(Object)} method is called.
	 *
	 * @since 1.0.0;
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Predicate<T> getFirstPredicate() {
		return new FirstPredicate<>();
	}

}

/**
 * {@link Predicate} that returns true only for the first evaluation.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 */
@SuppressWarnings("rawtypes")
class FirstPredicate<T> implements Predicate<T> {

	private boolean first = true;

	public boolean test(T object) {
		if (first) {
			first = false;
			return true;
		}
		return false;
	}
}