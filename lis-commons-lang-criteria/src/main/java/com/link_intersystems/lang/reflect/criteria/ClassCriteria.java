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

import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.util.*;
import com.link_intersystems.util.graph.BreadthFirstNodeIterator;
import com.link_intersystems.util.graph.DepthFirstNodeIterator;
import com.link_intersystems.util.graph.GraphFacade;
import com.link_intersystems.util.graph.GraphFacade.NodeIterateStrategy;
import com.link_intersystems.util.graph.Node;

import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * A criteria for creating iterators that iterate over class hierarchies.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
public class ClassCriteria extends ElementCriteria<Class<?>> {

    /**
     *
     */
    private static final long serialVersionUID = -5205145459979909916L;
    private ClassType[] classTypes = new ClassType[]{ClassType.CLASSES, ClassType.INTERFACES};
    private boolean traverseClassesUniquely;
    private Class<?> stopClass;
    private Comparator<Class<?>> interfacesComparator = ReflectFacade.getCanonicalClassNameComparator();
    private TraverseStrategy traverseStrategy = TraverseStrategy.DEPTH_FIRST;
    private boolean separatedClassTypeTraversal = true;
    private Comparator<Class<?>> innerClassesComparator = ReflectFacade.getCanonicalClassNameComparator();

    public List<Class<?>> getResult(Class<?> startAt) {
        Iterable<Class<?>> iterable = getIterable(startAt);
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    /**
     * Set the class hierarchy traverse strategy (
     * {@link TraverseStrategy#BREADTH_FIRST} or
     * {@link TraverseStrategy#DEPTH_FIRST}).
     *
     * @param traverseStrategy
     * @since 1.2.0;
     */
    public void setTraverseStrategy(TraverseStrategy traverseStrategy) {
        this.traverseStrategy = requireNonNull(traverseStrategy);
    }

    /**
     * Sets the selected {@link ClassType}s of the class hierarchy in the order
     * they are selected. Default is {@link ClassType#CLASSES},
     * {@link ClassType#INTERFACES}.
     *
     * @param classTypes the {@link ClassType}s of the class hierarchy that should be
     *                   selected when iterating through the hierarchy. The classTypes
     *                   should be unique. Otherwise the first occurrence in the
     *                   classTypes array wins. All later occurrences will be ignored.
     * @since 1.2.0;
     */

    public void setSelection(ClassType... classTypes) {
        requireNonNull(classTypes);
        this.classTypes = Arrays.asList(classTypes).stream().distinct().toArray(ClassType[]::new);
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
     * @param separatedClassTypeTraversal
     * @since 1.2.0;
     */
    public void setSeparatedClassTypeTraversal(boolean separatedClassTypeTraversal) {
        this.separatedClassTypeTraversal = separatedClassTypeTraversal;
    }

    /**
     * A comparator that defines the order in which interfaces will be iterated.
     * When {@link Class#getInterfaces()} is invoked the comparator that has
     * been set is used to determine the order in which the interfaces are
     * iterated. Default is a canonical class name comparator. Pass <code>null</code> to disable interface iteration
     * order. In this case the interfaces will be iterated in the way the
     * {@link Class#getInterfaces()} method returns them.
     *
     * @param interfacesComparator
     * @since 1.0.0;
     */
    public void setInterfacesIterationOrder(Comparator<Class<?>> interfacesComparator) {
        this.interfacesComparator = requireNonNull(interfacesComparator);
    }

    /**
     * A comparator that defines the order in which inner classes will be
     * iterated.
     *
     * @param innerClassesIterationOrder
     * @since 1.2.0;
     */
    public void setInnerClassesIterationOrder(Comparator<Class<?>> innerClassesIterationOrder) {
        this.innerClassesComparator = requireNonNull(innerClassesIterationOrder);
    }

    /**
     * @return the interfaces iteration order comparator set by
     * {@link #setInterfacesIterationOrder(Comparator)} if any or null.
     * @since 1.2.0;
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
     * @since 1.0.0;
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
     * @since 1.0.0;
     */
    public void setTraverseClassesUniquely(boolean traverseClassesUniquely) {
        this.traverseClassesUniquely = traverseClassesUniquely;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@link Predicate} must handle {@link Class} objects in its
     * {@link Predicate#test(Object)} method.
     * </p>
     *
     * @since 1.0.0;
     */
    @Override
    public void add(Predicate<Class<?>> predicate) {
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
     * defined by this criteria starting at the given class. If the
     * criteria defines a stop class than the {@link Iterable} will stop
     * at that class.
     * @throws IllegalArgumentException if the this criteria specifies a stop class and this class is
     *                                  not a superclass of the start class.
     * @since 1.0.0;
     */
    public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(Class<?> startAt, Class<?> stopAt) {
        Iterable<Class<?>> iterable = getIterable(startAt, stopAt);
        return iterable;
    }

    /**
     * Same behavior as {@link #getIterable(Class, Class)} but returns an
     * {@link Iterable} whose iterator returns {@link AnnotatedElement}s.
     *
     * @param startAt
     * @return an {@link Iterable} that iterates through the class hierarchy as
     * defined by this criteria starting at the given class and stopping
     * at the given class.
     * @throws IllegalArgumentException if the stop class is not a superclass of the start class.
     * @since 1.0.0;
     */
    public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(Class<?> startAt) {
        return getAnnotatedElementIterable(startAt, stopClass);
    }

    /**
     * @param startAt
     * @return an {@link Iterable} that iterates through the class hierarchy as
     * defined by this criteria starting at the given class. If the
     * criteria defines a stop class than the {@link Iterable} will stop
     * at that class.
     * @throws IllegalArgumentException if the this criteria specifies a stop class and this class is
     *                                  not a superclass of the start class.
     * @since 1.0.0;
     */
    public Iterable<Class<?>> getIterable(Class<?> startAt) {
        return getIterable(startAt, stopClass);
    }

    /**
     * @param startAt
     * @param stopAt
     * @return an {@link Iterable} that iterates through the class hierarchy as
     * defined by this criteria starting at the given class and stopping
     * at the given class.
     * @throws IllegalArgumentException if the stop class is not a superclass of the start class.
     * @since 1.0.0;
     */
    public Iterable<Class<?>> getIterable(Class<?> startAt, Class<?> stopAt) {
        requireNonNull(startAt);
        if (stopAt != null && !stopAt.isAssignableFrom(startAt)) {
            throw new IllegalArgumentException("stopAt " + stopAt + " must be a superclass of " + startAt);
        }
        class StartAtIterable implements Iterable<Class<?>> {

            private final Class<?> startAt;
            private final ObjectFactory<ClassCriteria> templateObjectFactory;

            public StartAtIterable(Class<?> startAt, ObjectFactory<ClassCriteria> classCriteria) {
                this.templateObjectFactory = classCriteria;
                this.startAt = startAt;
            }

            @SuppressWarnings({"unchecked", "rawtypes"})
            public Iterator<Class<?>> iterator() {
                ClassCriteria classCriteriaCopy = templateObjectFactory.getObject();
                TraverseStrategy traverseStrategy = classCriteriaCopy.traverseStrategy;
                boolean separatedClassTypeTraversal = classCriteriaCopy.separatedClassTypeTraversal;
                Comparator<Class<?>> interfacesComparator = classCriteriaCopy.interfacesComparator;
                Comparator<Class<?>> innerClassesComparator = classCriteriaCopy.innerClassesComparator;
                ClassType[] classTypes = classCriteriaCopy.classTypes;
                ClassNode rootNode = new ClassNode(startAt, classTypes);

                rootNode.setInterfacesOrder(interfacesComparator);
                rootNode.setInnerClassesOrder(innerClassesComparator);

                class Node2ClassTransformer implements Transformer<Node, Class<?>> {

                    public Class<?> transform(Node input) {
                        Object userObject = input.getUserObject();
                        Class<?> clazz = Class.class.cast(userObject);
                        return clazz;
                    }

                }
                Iterator<Node> classNodeIterator = null;
                Iterator classesIterator;
                if (separatedClassTypeTraversal) {
                    NodeIterateStrategy nodeIterateStrategy = NodeIterateStrategy.valueOf(traverseStrategy.name());
                    Predicate[] nodeIteratePredicates = new Predicate[classTypes.length];
                    Node2ClassTransformer node2ClassTransformer = new Node2ClassTransformer();
                    for (int i = 0; i < classTypes.length; i++) {
                        ClassType classType = classTypes[i];
                        Predicate<Class<?>> classTypePredicate = new OrAbstractPredicate<>(classType);
                        Predicate transformedPredicate = new TransformedPredicate(node2ClassTransformer, classTypePredicate);
                        nodeIteratePredicates[i] = transformedPredicate;
                    }
                    classNodeIterator = GraphFacade.perPredicateNodeIterator(nodeIterateStrategy, rootNode, nodeIteratePredicates);
                    classesIterator = new TransformedIterator(classNodeIterator, new Node2ClassTransformer());
                } else {
                    switch (classCriteriaCopy.traverseStrategy) {
                        case BREADTH_FIRST:
                            classNodeIterator = new BreadthFirstNodeIterator(rootNode);
                            break;
                        case DEPTH_FIRST:
                            classNodeIterator = new DepthFirstNodeIterator(rootNode);
                            break;
                    }
                    classesIterator = new TransformedIterator(classNodeIterator, new Node2ClassTransformer());
                }

                Predicate<Class<?>> classTypePredicate = new OrAbstractPredicate<>(classTypes);
                classesIterator = new FilteredIterator<>(classesIterator, classTypePredicate);

                classesIterator = classCriteriaCopy.applyTraverseClassesUniquely(classesIterator);

                classesIterator = classCriteriaCopy.applyStopAtFilter(classesIterator);

                classesIterator = classCriteriaCopy.applyElementFilter(classesIterator);

                classesIterator = classCriteriaCopy.applySelectionFilter(classesIterator);

                return classesIterator;
            }

        }

        return new StartAtIterable(startAt, getObjectFactory());
    }

    protected Iterator<Class<?>> applyTraverseClassesUniquely(Iterator<Class<?>> iterator) {
        if (isTraverseClassesUniquelyEnabled()) {
            Set<Class<?>> uniqueClasses = new HashSet<>();
            iterator = new FilteredIterator<>(iterator, uniqueClasses::add);
        }
        return iterator;
    }

    protected boolean isTraverseClassesUniquelyEnabled() {
        return traverseClassesUniquely;
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Class<?>> applyStopAtFilter(Iterator<Class<?>> iterator) {
        if (stopClass != null) {
            Predicate stopPredicate = ReflectFacade.getIsAssignablePredicate(stopClass);
            iterator = new FilteredIterator<>(iterator, stopPredicate);
        }
        return iterator;
    }

    public ObjectFactory<ClassCriteria> getObjectFactory() {
        return new SerializableTemplateObjectFactory<>(this);
    }

    public enum ClassType implements Predicate<Class<?>> {
        CLASSES() {
            @Override
            Class<?>[] getClasses(Class<?> clazz) {
                Class<?> superclass = clazz.getSuperclass();
                if (superclass == null) {
                    return new Class<?>[0];
                }
                return new Class[]{superclass};
            }

            @Override
            public boolean test(Class<?> clazz) {
                return !clazz.isInterface() && clazz.getEnclosingClass() == null;
            }
        }, INTERFACES() {
            @Override
            Class<?>[] getClasses(Class<?> clazz) {
                return clazz.getInterfaces();
            }

            @Override
            public boolean test(Class<?> clazz) {
                return clazz.isInterface();
            }
        }, INNER_CLASSES() {
            @Override
            Class<?>[] getClasses(Class<?> clazz) {
                return clazz.getClasses();
            }

            @Override
            public boolean test(Class<?> clazz) {
                return clazz.getEnclosingClass() != null;
            }
        };


        abstract Class<?>[] getClasses(Class<?> clazz);
    }

    public enum TraverseStrategy {
        /**
         *
         */
        DEPTH_FIRST,
        /**
         *
         */
        BREADTH_FIRST
    }

}