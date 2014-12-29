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

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.util.ObjectFactory;
import com.link_intersystems.util.SerializableTemplateObjectFactory;
import com.link_intersystems.util.graph.BreadthFirstNodeIterator;
import com.link_intersystems.util.graph.DepthFirstNodeIterator;
import com.link_intersystems.util.graph.GraphFacade;
import com.link_intersystems.util.graph.GraphFacade.NodeIterateStrategy;
import com.link_intersystems.util.graph.Node;

/**
 * A criteria for creating iterators that iterate over class hierarchies.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class ClassCriteria extends ElementCriteria<Class<?>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5205145459979909916L;

	public enum ClassType {
		CLASSES, INTERFACES, INNER_CLASSES
	}

	private ClassType[] classTypes = new ClassType[] { ClassType.CLASSES,
			ClassType.INTERFACES };

	private boolean traverseClassesUniquely;

	private Class<?> stopClass;

	private Comparator<Class<?>> interfacesComparator = ReflectFacade
			.getCanonicalClassNameComparator();

	private TraverseStrategy traverseStrategy = TraverseStrategy.DEPTH_FIRST;

	private boolean separatedClassTypeTraversal = true;

	private Comparator<Class<?>> innerClassesComparator = ReflectFacade
			.getCanonicalClassNameComparator();

	/**
	 * Set the class hierarchy traverse strategy (
	 * {@link TraverseStrategy#BREADTH_FIRST} or
	 * {@link TraverseStrategy#DEPTH_FIRST}).
	 *
	 * @param traverseStrategy
	 * @since 1.2.0.0
	 */
	public void setTraverseStrategy(TraverseStrategy traverseStrategy) {
		Assert.notNull("traverseStrategy", traverseStrategy);
		this.traverseStrategy = traverseStrategy;
	}

	/**
	 * Sets the selected {@link ClassType}s of the class hierarchy in the order
	 * they are selected. Default is {@link ClassType#CLASSES},
	 * {@link ClassType#INTERFACES}.
	 *
	 * @param classTypes
	 *            the {@link ClassType}s of the class hierarchy that should be
	 *            selected when iterating through the hierarchy. The classTypes
	 *            should be unique. Otherwise the first occurrence in the
	 *            classTypes array wins. All later occurrences will be ignored.
	 * @since 1.2.0.0
	 */

	public void setSelection(ClassType... classTypes) {
		Assert.notNull("classTypes", classTypes);
		List<ClassType> uniqueClassTypes = new ArrayList<ClassType>(
				Arrays.asList(classTypes));
		CollectionUtils.filter(uniqueClassTypes, new UniquePredicate());
		this.classTypes = (ClassType[]) uniqueClassTypes
				.toArray(new ClassType[uniqueClassTypes.size()]);
	}

	/**
	 * If set to true the selected {@link ClassType}s (
	 * {@link #setSelection(ClassType...)}) will be traversed in separation.
	 * Separation means that if the {@link ClassType}s:
	 * {@link ClassType#INTERFACES}, {@link ClassType#CLASSES} is specified the
	 * interfaces will get traversed first using the set
	 * {@link #setTraverseStrategy(TraverseStrategy)} and than the classes are
	 * traversed using the set {@link TraverseStrategy}. If
	 * separatedClassTypeTraversal is set to false the interfaces and classes
	 * tree will be traversed together in the set {@link TraverseStrategy}.
	 *
	 *
	 * @param separatedClassTypeTraversal
	 * @since 1.2.0.0
	 */
	public void setSeparatedClassTypeTraversal(
			boolean separatedClassTypeTraversal) {
		this.separatedClassTypeTraversal = separatedClassTypeTraversal;
	}

	/**
	 * A comparator that defines the order in which interfaces will be iterated.
	 * When {@link Class#getInterfaces()} is invoked the comparator that has
	 * been set is used to determine the order in which the interfaces are
	 * iterated. Default is the {@link CanonicalClassNameComparator}. Use
	 * {@link #setInterfacesIterationOrder(null)} to disable interface iteration
	 * order. In this case the interfaces will be iterated in the way the
	 * {@link Class#getInterfaces()} method returns them.
	 *
	 * @param interfacesComparator
	 * @see CanonicalClassNameComparator
	 * @since 1.0.0.0
	 */
	public void setInterfacesIterationOrder(
			Comparator<Class<?>> interfacesComparator) {
		Assert.notNull("interfacesComparator", interfacesComparator);
		this.interfacesComparator = interfacesComparator;
	}

	/**
	 * A comparator that defines the order in which inner classes will be
	 * iterated.
	 *
	 * @param innerClassesIterationOrder
	 * @since 1.2.0.0
	 */
	public void setInnerClassesIterationOrder(
			Comparator<Class<?>> innerClassesIterationOrder) {
		Assert.notNull("innerClassesIterationOrder", interfacesComparator);
		this.innerClassesComparator = innerClassesIterationOrder;
	}

	/**
	 *
	 * @return the interfaces iteration order comparator set by
	 *         {@link #setInterfacesIterationOrder(Comparator)} if any or null.
	 * @since 1.2.0.0
	 */
	Comparator<Class<?>> getInterfacesComparator() {
		return interfacesComparator;
	}

	/**
	 * The stopClass is the class or interface in the hierarchy where the
	 * iterator will stop iterating. If no stopClass is set (or null) the
	 * iterator will iterate through the hierarchy until no super class can be
	 * found (until Object - the Object class will be included). Otherwise the
	 * the iterator will stop at that class and not include it.
	 * <p>
	 *
	 * <pre>
	 * ClassCriteria criteria = new ClassCriteria();
	 * criteria.setTraverseOrder(TraverseOrder.SUPERCLASS_ONLY);
	 *
	 * criteria.stopAt(AbstractList.class);
	 * criteria.startAt(ArrayList.class);
	 *
	 * Iterator&lt;Class&lt;?&gt;&gt; classIterator = criteria.iterator();
	 * Class&lt;?&gt; currentClass = null;
	 *
	 * currentClass = classIterator.next();
	 * assertEquals(ArrayList.class, currentClass);
	 *
	 * currentClass = classIterator.next();
	 * assertEquals(AbstractList.class, currentClass);
	 *
	 * assertFalse(classIterator.hasNext());
	 * </pre>
	 *
	 * </p>
	 *
	 * @param stopClass
	 * @since 1.0.0.0
	 */
	public void stopAt(Class<?> stopClass) {
		this.stopClass = stopClass;
	}

	/**
	 * If set to true the iterator traverses each class only one time.
	 * Interfaces as well as inner classes super types can occur multiple times
	 * in the class hierarchy. Therefore you might only want to iterate through
	 * a specific class one time instead of all occurrences. Default is false.
	 *
	 * @param traverseClassesUniquely
	 * @since 1.0.0.0
	 */
	public void setTraverseClassesUniquely(boolean traverseClassesUniquely) {
		this.traverseClassesUniquely = traverseClassesUniquely;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The {@link Predicate} must handle {@link Class} objects in its
	 * {@link Predicate#evaluate(Object)} method.
	 * </p>
	 *
	 * @since 1.0.0.0
	 */
	@Override
	public void add(Predicate predicate) {
		/*
		 * Just override the method to provide more specific javadoc
		 */
		super.add(predicate);
	}

	/**
	 * Same behavior as {@link #getIterable(Class)} but returns an
	 * {@link Iterable} whose iterator returns {@link AnnotatedElement}s.
	 *
	 * @param startAt
	 * @return an {@link Iterable} that iterates through the class hierarchy as
	 *         defined by this criteria starting at the given class. If the
	 *         criteria defines a stop class than the {@link Iterable} will stop
	 *         at that class.
	 * @throws IllegalArgumentException
	 *             if the this criteria specifies a stop class and this class is
	 *             not a superclass of the start class.
	 * @since 1.0.0.0
	 */
	public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(
			Class<?> startAt, Class<?> stopAt) {
		Iterable<Class<?>> iterable = getIterable(startAt, stopAt);
		return iterable;
	}

	/**
	 * Same behavior as {@link #getIterable(Class, Class)} but returns an
	 * {@link Iterable} whose iterator returns {@link AnnotatedElement}s.
	 *
	 * @param startAt
	 * @param stopAt
	 * @return an {@link Iterable} that iterates through the class hierarchy as
	 *         defined by this criteria starting at the given class and stopping
	 *         at the given class.
	 * @throws NullArgumentException
	 *             if the stop class is not a superclass of the start class.
	 * @since 1.0.0.0
	 */
	public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(
			Class<?> startAt) {
		return getAnnotatedElementIterable(startAt, stopClass);
	}

	/**
	 *
	 * @param startAt
	 * @return an {@link Iterable} that iterates through the class hierarchy as
	 *         defined by this criteria starting at the given class. If the
	 *         criteria defines a stop class than the {@link Iterable} will stop
	 *         at that class.
	 * @throws NullArgumentException
	 *             if the this criteria specifies a stop class and this class is
	 *             not a superclass of the start class.
	 * @since 1.0.0.0
	 */
	public Iterable<Class<?>> getIterable(Class<?> startAt) {
		return getIterable(startAt, stopClass);
	}

	public enum TraverseStrategy {
		/**
		 *
		 */
		DEPTH_FIRST,
		/**
		 *
		 */
		BREADTH_FIRST;
	}

	/**
	 *
	 * @param startAt
	 * @param stopAt
	 * @return an {@link Iterable} that iterates through the class hierarchy as
	 *         defined by this criteria starting at the given class and stopping
	 *         at the given class.
	 * @throws NullArgumentException
	 *             if the stop class is not a superclass of the start class.
	 * @since 1.0.0.0
	 */
	public Iterable<Class<?>> getIterable(Class<?> startAt, Class<?> stopAt) {
		Assert.notNull("startAt", startAt);
		if (stopAt != null && !stopAt.isAssignableFrom(startAt)) {
			throw new IllegalArgumentException("stopAt " + stopAt
					+ " must be a superclass of " + startAt);
		}
		class StartAtIterable implements Iterable<Class<?>> {

			private final Class<?> startAt;
			private final ObjectFactory<ClassCriteria> templateObjectFactory;

			public StartAtIterable(Class<?> startAt,
					ObjectFactory<ClassCriteria> classCriteria) {
				this.templateObjectFactory = classCriteria;
				this.startAt = startAt;
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Iterator<Class<?>> iterator() {
				ClassCriteria classCriteriaCopy = templateObjectFactory
						.getObject();
				TraverseStrategy traverseStrategy = classCriteriaCopy.traverseStrategy;
				boolean separatedClassTypeTraversal = classCriteriaCopy.separatedClassTypeTraversal;
				Comparator<Class<?>> interfacesComparator = classCriteriaCopy.interfacesComparator;
				Comparator<Class<?>> innerClassesComparator = classCriteriaCopy.innerClassesComparator;
				ClassType[] classTypes = classCriteriaCopy.classTypes;
				ClassNode rootNode = new ClassNode(startAt, classTypes);

				rootNode.setInterfacesOrder(interfacesComparator);
				rootNode.setInnerClassesOrder(innerClassesComparator);

				class Node2ClassTransformer implements Transformer {

					public Object transform(Object input) {
						Node node = Node.class.cast(input);
						Object userObject = node.getUserObject();
						Class<?> clazz = Class.class.cast(userObject);
						return clazz;
					}

				}
				Iterator<Node> classNodeIterator = null;
				Iterator classesIterator = null;
				if (separatedClassTypeTraversal) {
					NodeIterateStrategy nodeIterateStrategy = NodeIterateStrategy
							.valueOf(traverseStrategy.name());
					Predicate[] nodeIteratePredicates = new Predicate[classTypes.length];
					Node2ClassTransformer node2ClassTransformer = new Node2ClassTransformer();
					for (int i = 0; i < classTypes.length; i++) {
						ClassType classType = classTypes[i];
						ClassTypePredicate classTypePredicate = new ClassTypePredicate(
								classType);
						TransformedPredicate transformedPredicate = new TransformedPredicate(
								node2ClassTransformer, classTypePredicate);
						nodeIteratePredicates[i] = transformedPredicate;
					}
					classNodeIterator = GraphFacade.perPredicateNodeIterator(
							nodeIterateStrategy, rootNode,
							nodeIteratePredicates);
					classesIterator = IteratorUtils.transformedIterator(
							classNodeIterator, new Node2ClassTransformer());
				} else {
					switch (classCriteriaCopy.traverseStrategy) {
					case BREADTH_FIRST:
						classNodeIterator = new BreadthFirstNodeIterator(
								rootNode);
						break;
					case DEPTH_FIRST:
						classNodeIterator = new DepthFirstNodeIterator(rootNode);
						break;
					}
					classesIterator = IteratorUtils.transformedIterator(
							classNodeIterator, new Node2ClassTransformer());
				}

				ClassTypePredicate classTypePredicate = new ClassTypePredicate(
						classTypes);
				classesIterator = IteratorUtils.filteredIterator(
						classesIterator, classTypePredicate);

				classesIterator = classCriteriaCopy
						.applyTraverseClassesUniquely(classesIterator);

				classesIterator = classCriteriaCopy
						.applyStopAtFilter(classesIterator);

				classesIterator = classCriteriaCopy
						.applyElementFilter(classesIterator);

				classesIterator = classCriteriaCopy
						.applySelectionFilter(classesIterator);

				return classesIterator;
			}

		}

		return new StartAtIterable(startAt, getObjectFactory());
	}

	@SuppressWarnings("unchecked")
	protected Iterator<Class<?>> applyTraverseClassesUniquely(
			Iterator<Class<?>> iterator) {
		if (isTraverseClassesUniquelyEnabled()) {
			iterator = IteratorUtils.filteredIterator(iterator,
					UniquePredicate.uniquePredicate());
		}
		return iterator;
	}

	protected boolean isTraverseClassesUniquelyEnabled() {
		return traverseClassesUniquely;
	}

	@SuppressWarnings("unchecked")
	protected Iterator<Class<?>> applyStopAtFilter(Iterator<Class<?>> iterator) {
		if (stopClass != null) {
			Predicate stopPredicate = ReflectFacade
					.getIsAssignablePredicate(stopClass);
			iterator = IteratorUtils.filteredIterator(iterator, stopPredicate);
		}
		return iterator;
	}

	public ObjectFactory<ClassCriteria> getObjectFactory() {
		return new SerializableTemplateObjectFactory<ClassCriteria>(this);
	}

	private static class ClassTypePredicate implements Predicate {

		private ClassType[] classTypes;

		public ClassTypePredicate(ClassType... classTypes) {
			this.classTypes = classTypes;
		}

		public boolean evaluate(Object object) {
			Class<?> clazz = Class.class.cast(object);
			boolean filterMatch = false;

			for (int i = 0; i < classTypes.length; i++) {
				ClassType classType = classTypes[i];
				switch (classType) {
				case INNER_CLASSES:
					filterMatch = clazz.getEnclosingClass() != null;
					break;
				case CLASSES:
					filterMatch = !clazz.isInterface()
							&& clazz.getEnclosingClass() == null;
					break;
				case INTERFACES:
					filterMatch = clazz.isInterface();
				}
				if (filterMatch) {
					break;
				}
			}
			return filterMatch;
		}
	}

}