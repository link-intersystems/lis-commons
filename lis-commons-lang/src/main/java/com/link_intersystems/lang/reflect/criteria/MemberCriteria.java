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

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.AccessType;
import com.link_intersystems.lang.reflect.MemberModifierPredicate;
import com.link_intersystems.lang.reflect.MemberModifierPredicate.Match;
import com.link_intersystems.lang.reflect.ReflectFacade;
import com.link_intersystems.util.*;
import com.link_intersystems.util.graph.tree.DepthFirstBottomUpTreeModelIterable;
import com.link_intersystems.util.graph.tree.TransformedIterableTreeModel;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Iterate over {@link Member}s or {@link AnnotatedElement}s that match the
 * criteria specified by a {@link MemberCriteria}.
 * <p>
 * <h2>Examples</h2>
 * <h3>Iterate over all public abstract methods, in method names order, whose
 * name's match the pattern &quot;add.*&quot; starting at class ArrayList.</h3>
 *
 * <pre>
 * MemberCriteria memberCriteria = new MemberCriteria();
 * memberCriteria.withAccess(AccessType.PUBLIC);
 * memberCriteria.withModifiers(Modifier.ABSTRACT);
 * memberCriteria.membersOfType(Method.class);
 * memberCriteria.setMemberIterateOrder(MemberNameComparator.INSTANCE);
 * memberCriteria.named(Pattern.compile(&quot;add.*&quot;));
 *
 * ClassCriteria classCriteria = new ClassCriteria();
 * Iterable&lt;Class&lt;?&gt;&gt; classIterable = classCriteria.getIterable(ArrayList.class);
 * Iterable&lt;Member&gt; memberIterable = memberCriteria.getIterable(classIterable);
 *
 * Iterator&lt;Member&gt; criteriaIterator = memberIterable.iterator();
 * </pre>
 *
 * <h3>Get the first public method that has the same signature as
 * List.subList(int, int) starting at ArrayList traversing only the class
 * hierarchy (omit interfaces).</h3>
 *
 * <pre>
 * MemberCriteria memberCriteria = new MemberCriteria();
 * memberCriteria.setResult(Selection.FIRST);
 * memberCriteria.membersOfType(Method.class);
 *
 * Method method = List.class.getDeclaredMethod(&quot;subList&quot;, int.class, int.class);
 *
 * memberCriteria.add(new SignaturePredicate(method));
 *
 * ClassCriteria classCriteria = new ClassCriteria();
 * classCriteria.setTraverseOrder(TraverseOrder.SUPERCLASSES_ONLY);
 * Iterable&lt;Class&lt;?&gt;&gt; classIterable = classCriteria.getIterable(ArrayList.class);
 * Iterable&lt;Member&gt; memberIterable = memberCriteria.getIterable(classIterable);
 *
 * Iterator&lt;Member&gt; criteriaIterator = memberIterable.iterator();
 * Member member = criteriaIterator.next();
 * Method firstMatch = (Method) member;
 * Method declaredMethod = AbstractList.class.getDeclaredMethod(&quot;subList&quot;, int.class, int.class);
 * assertEquals(declaredMethod, firstMatch);
 * assertFalse(criteriaIterator.hasNext());
 * </pre>
 *
 * </p>
 *
 * @author René Link
 * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0.0
 */
public class MemberCriteria<T extends Member> extends ElementCriteria<T> {

    /**
     *
     */
    private static final long serialVersionUID = 4870605392298539751L;

    public static final List<Class<?>> DEFAULT_MEMBER_TYPES;

    static {
        List<Class<?>> defaultMemberTypes = new ArrayList<>();
        defaultMemberTypes.add(Constructor.class);
        defaultMemberTypes.add(Method.class);
        defaultMemberTypes.add(Field.class);
        DEFAULT_MEMBER_TYPES = Collections.unmodifiableList(defaultMemberTypes);
    }

    private List<Class<?>> memberTypes = DEFAULT_MEMBER_TYPES;

    private int modifiers = 0;

    private Pattern pattern;

    public MemberCriteria() {

    }

    public static MemberCriteria<? extends Member> forMemberTypes(Class<?>... memberTypes) {
        MemberCriteria<Member> memberCriteria = new MemberCriteria<>();
        memberCriteria.membersOfType(memberTypes);
        return memberCriteria;
    }

    /**
     * A non empty collection of {@link AccessType}s that this criteria should
     * match with.
     */
    private Collection<AccessType> accesses = Arrays.asList(AccessType.values());

    private String name;
    private Comparator<Member> iterateOrderComparator = ReflectFacade.getMemberNameComparator();

    /**
     * The {@link IterateStrategy} determines in which order
     * {@link AnnotatedElement}s are iterated when using an {@link Iterable} of
     * {@link AnnotatedElement}s.
     *
     * @author René Link
     * <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     * intersystems.com]</a>
     * @since 1.0.0.0
     */
    public enum IterateStrategy {
        /**
         * Iterate only elements that are {@link Member}s.
         *
         * @since 1.0.0.0
         */
        MEMBERS_ONLY(JavaElementTraverseStrategies.MEMBER_TRAVERSE_STRATEGY),

        /**
         * Iterate only elements that are {@link Class}es.
         *
         * @since 1.0.0.0
         */
        CLASSES_ONLY(JavaElementTraverseStrategies.CURRENT_CLASS_TRAVERSE_STRATEGY),
        /**
         * Iterate only elements that are {@link Package}s.
         *
         * @since 1.0.0.0
         */
        PACKAGES_ONLY(JavaElementTraverseStrategies.PACKAGE_TRAVERSE_STRATEGY),

        /**
         * Iterate elements that are {@link Class}es or {@link Member}s in that
         * order.
         *
         * @since 1.0.0.0
         */
        CLASS_MEMBERS(JavaElementTraverseStrategies.CURRENT_CLASS_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.MEMBER_TRAVERSE_STRATEGY),
        /**
         * Iterate elements that are {@link Member}s or {@link Class}es in that
         * order.
         *
         * @since 1.0.0.0
         */
        MEMBERS_CLASS(JavaElementTraverseStrategies.MEMBER_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.CURRENT_CLASS_TRAVERSE_STRATEGY),
        /**
         * Iterate elements that are {@link Package}s, {@link Class}es or
         * {@link Member}s in that order.
         *
         * @since 1.0.0.0
         */
        PACKAGE_CLASS_MEMBERS(JavaElementTraverseStrategies.PACKAGE_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.CURRENT_CLASS_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.MEMBER_TRAVERSE_STRATEGY),
        /**
         * Iterate elements that are {@link Member}s, {@link Class}es or
         * {@link Package}s in that order.
         *
         * @since 1.0.0.0
         */
        MEMBERS_CLASS_PACKAGE(JavaElementTraverseStrategies.MEMBER_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.CURRENT_CLASS_TRAVERSE_STRATEGY, JavaElementTraverseStrategies.PACKAGE_TRAVERSE_STRATEGY);

        private final JavaElementTraverseStrategy javaElementTraverseStrategy;

        IterateStrategy(JavaElementTraverseStrategy... javaElementTraverseStrategies) {
            if (javaElementTraverseStrategies.length == 1) {
                this.javaElementTraverseStrategy = javaElementTraverseStrategies[0];
            } else {
                this.javaElementTraverseStrategy = new CompositeJavaElementTraverseStrategy(javaElementTraverseStrategies);
            }
        }

        private JavaElementTraverseStrategy getJavaElementTraverseStrategy() {
            return javaElementTraverseStrategy;
        }

    }

    /**
     * Set the order in which {@link Member} are iterated. The order is defined
     * by the {@link Comparator}. The {@link Member} order is determined per
     * declaring class. Therefore the member iterate order only determines how
     * {@link Member}s of the same declaring class are iterated. So it does NOT
     * affect the order of {@link Member}s of different classes (global order).
     *
     * @param iterateOrderComparator the member comparator that orders the members.
     * @since 1.0.0.0
     */
    public void setMemberIterateOrder(Comparator<Member> iterateOrderComparator) {
        Assert.notNull("iterateOrderComparator", iterateOrderComparator);
        this.iterateOrderComparator = iterateOrderComparator;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@link Predicate} must handle {@link Method} objects in it's
     * {@link Predicate#test(Object)} method.
     * </p>
     *
     * @since 1.0.0.0
     */
    @Override
    public void add(Predicate<T> predicate) {
        /*
         * Just override the method to provide more specific javadoc
         */
        super.add(predicate);
    }

    /**
     * Add a criterion to only select {@link Member}s of the specified types.
     * Allowed types are
     * <ul>
     * <li>{@link Constructor}</li>
     * <li>{@link Method}</li>
     * <li>{@link Field}</li>
     * </ul>
     *
     * @param memberTypes the member types that this {@link MemberCriteria} should
     *                    select.
     * @since 1.0.0.0
     */
    public final void membersOfType(Class<?>... memberTypes) {
        /*
         * we do not enforce member types by the generic definition
         * "Class<? extends Member> ... memberTypes" because this will cause
         * type unsafe warnings at the client side. For example if a client
         * calls this method with the parameters
         * "membersOfType(Field.class, Method.class)".
         */
        for (Class<?> memberType : memberTypes) {
            if (!(Constructor.class.equals(memberType)) && !(Method.class.equals(memberType)) && !(Field.class.equals(memberType))) {
                throw new IllegalArgumentException("Unsupported member type " + memberType + ". Supported types are " + DEFAULT_MEMBER_TYPES + ".");
            }
        }
        this.memberTypes = Arrays.asList(memberTypes);
    }

    protected List<Member> getSortedMembers(Class<?> currentClass) {
        List<Member> memberList = new ArrayList<>();
        for (Class<?> memberType : memberTypes) {
            Member[] members = null;
            if (Constructor.class.equals(memberType)) {
                members = currentClass.getDeclaredConstructors();
            } else if (Method.class.equals(memberType)) {
                members = currentClass.getDeclaredMethods();
            } else if (Field.class.equals(memberType)) {
                members = currentClass.getDeclaredFields();
            }
            if (members != null) {
                List<Member> asList = Arrays.asList(members);
                memberList.addAll(asList);
            }
        }

        memberList.sort(iterateOrderComparator);
        return memberList;
    }

    /**
     * Set the criterion that selects only {@link Member}s that have one of the
     * defined {@link AccessType}s.
     *
     * @param accesses the {@link AccessType}s that this criteria should match with.
     *                 Must contain at least one element.
     * @since 1.0.0.0
     */
    public void withAccess(AccessType... accesses) {
        Assert.notNull("accesses", accesses);
        if (accesses.length == 0) {
            throw new IllegalArgumentException("accesses must contain at least 1 access type");
        }
        this.accesses = Arrays.asList(accesses);
    }

    /**
     * Set the criterion that selects only {@link Member}s that exactly have the
     * specified modifiers. The access modifiers are set separately. Use
     * {@link #withAccess(AccessType...)} to set access modifiers.
     *
     * @param modifiers the modifiers that the {@link Member}s, that are selected by this {@link MemberCriteria}, must have.
     * @since 1.0.0.0
     */
    public void withModifiers(int modifiers) {
        if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)) {
            throw new IllegalArgumentException("access modifiers are not allowed as argument. Use withAccess() instead.");
        }
        int allowedModifiers = Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT | Modifier.VOLATILE | Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.STRICT | Modifier.INTERFACE;

        if ((modifiers & allowedModifiers) == 0) {
            throw new IllegalArgumentException("modifiers must be one of [" + Modifier.toString(allowedModifiers) + "]");
        }

        this.modifiers = modifiers;
    }

    /**
     * Set the criterion that selects only {@link Member}s whose name matches
     * the specified pattern. If the pattern is null the criterion will be
     * removed.
     *
     * @param pattern a regular expression pattern to filter members by name.
     * @since 1.0.0.0
     */
    public void named(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Set the criterion that selects only {@link Member}s whose name matches
     * the specified pattern. If the pattern is null the criterion will be
     * removed.
     *
     * @param name a name to filter the members.
     * @since 1.0.0.0
     */
    public void named(String name) {
        this.name = name;
    }

    /**
     * @param classIterable the class {@link Iterable} that defines the classes that are
     *                      iterated to look for members that match this
     *                      {@link MemberCriteria}.
     * @return an {@link Iterable} that creates {@link Iterator}s that iterate
     * through {@link AnnotatedElement}s as defined by this
     * {@link MemberCriteria} using the default {@link IterateStrategy}
     * ( {@link IterateStrategy#MEMBERS_CLASS_PACKAGE} ).
     * @since 1.0.0.0
     */
    public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(Iterable<Class<?>> classIterable) {
        return getAnnotatedElementIterable(classIterable, IterateStrategy.MEMBERS_CLASS_PACKAGE);
    }

    /**
     * @param classIterable   the class {@link Iterable} that defines the classes that are
     *                        iterated to look for members that match this
     *                        {@link MemberCriteria}.
     * @param iterateStrategy the order in which the {@link AnnotatedElement} types selected
     *                        by this {@link MemberCriteria} are iterated through. In
     *                        contrast to the {@link #getIterable(Iterable)} method the
     *                        {@link IterateStrategy} allows you to iterate also through
     *                        elements that the selected {@link Member}s are declared on.
     * @return an {@link Iterable} that creates {@link Iterator}s that iterate
     * through {@link AnnotatedElement}s as defined by this
     * {@link MemberCriteria} using the given iterate strategy.
     * @since 1.0.0.0
     */
    public Iterable<? extends AnnotatedElement> getAnnotatedElementIterable(Iterable<Class<?>> classIterable, IterateStrategy iterateStrategy) {
        return new AnnotatedElementIterable(classIterable, iterateStrategy, getObjectFactory());
    }

    /**
     * @param classIterable the class {@link Iterable} that defines the classes that are
     *                      iterated to look for members that match this
     *                      {@link MemberCriteria}
     * @return an {@link Iterable} that creates {@link Iterator}s that iterate
     * through {@link Member}s as defined by this {@link MemberCriteria}
     * .
     * @since 1.0.0.0
     */
    public Iterable<Member> getIterable(Iterable<Class<?>> classIterable) {
        return new MemberIterableWithClassIterable(classIterable, getObjectFactory());
    }

    @Override
    protected Iterator<T> applyElementFilter(Iterator<T> iterator) {
        iterator = super.applyElementFilter(iterator);
        iterator = applyAccessAndNamePredicates(iterator);
        return iterator;
    }

    @SuppressWarnings("unchecked")
    protected Iterator<T> applyAccessAndNamePredicates(Iterator<T> iterator) {
        Collection<Predicate<T>> predicates = new ArrayList<>();

        int accessModifiers = 0;
        Collection<AccessType> accesses = getAccesses();
        for (AccessType access : accesses) {
            switch (access) {
                case PUBLIC:
                    accessModifiers |= Modifier.PUBLIC;
                    break;
                case PRIVATE:
                    accessModifiers |= Modifier.PRIVATE;
                    break;
                case PROTECTED:
                    accessModifiers |= Modifier.PROTECTED;
                    break;
                default:
                    break;
            }
        }
        Predicate<Member> accessModifierPredicate = new MemberModifierPredicate(accessModifiers, Match.AT_LEAST_ONE);

        if (accesses.contains(AccessType.DEFAULT)) {
            MemberModifierPredicate memberModifierPredicate = new MemberModifierPredicate(Modifier.PRIVATE | Modifier.PUBLIC | Modifier.PROTECTED, Match.AT_LEAST_ONE);
            accessModifierPredicate = accessModifierPredicate.or(o -> !memberModifierPredicate.test(o));
        }

        predicates.add((Predicate<T>) accessModifierPredicate);

        int modifiers = getModifiers();
        if (modifiers != 0) {
            predicates.add((Predicate<T>) new MemberModifierPredicate(modifiers, Match.AT_LEAST_ALL));
        }

        String name = getName();
        if (name != null) {
            predicates.add(ReflectFacade.getMemberNamePredicate(name));
        }

        Pattern pattern = getPattern();
        if (pattern != null) {
            predicates.add(ReflectFacade.getMemberNamePatternPredicate(pattern));
        }
        Predicate<T> allPredicate = new AndPredicate<>(predicates);
        iterator = new FilteredIterator<>(iterator, allPredicate);
        return iterator;
    }

    /**
     * @return a non empty list of {@link AccessType}s.
     */
    protected Collection<AccessType> getAccesses() {
        return accesses;
    }

    protected int getModifiers() {
        return modifiers;
    }

    protected String getName() {
        return name;
    }

    protected Pattern getPattern() {
        return pattern;
    }

    protected List<Class<?>> getMemberTypes() {
        return memberTypes;
    }

    private abstract static class AbstractCriteriaTransformer<I, O> implements Function<I, O> {

        private final MemberCriteria<Member> memberCriteria;

        public AbstractCriteriaTransformer(MemberCriteria<Member> memberCriteria) {
            this.memberCriteria = memberCriteria;
        }

        protected MemberCriteria<Member> getMemberCriteria() {
            return memberCriteria;
        }

    }

    private static class MemberIteratorTransformer extends AbstractCriteriaTransformer<Object, Object> {

        public MemberIteratorTransformer(MemberCriteria<Member> memberCriteria) {
            super(memberCriteria);
        }

        public Object apply(Object input) {
            if (input instanceof Class<?>) {
                final Class<?> currentClass = (Class<?>) input;
                List<Member> memberList = getMemberCriteria().getSortedMembers(currentClass);
                return memberList.iterator();
            }
            return input;
        }

    }

    private static class MemberIterableWithClassIterable implements Iterable<Member> {

        private final Iterable<Class<?>> classIterable;
        private final ObjectFactory<MemberCriteria<Member>> templateObjectFactory;

        public MemberIterableWithClassIterable(Iterable<Class<?>> classIterable, ObjectFactory<MemberCriteria<Member>> templateObjectFactory) {
            this.classIterable = classIterable;
            this.templateObjectFactory = templateObjectFactory;
        }

        @SuppressWarnings("unchecked")
        public Iterator<Member> iterator() {
            Iterator<Class<?>> classIterator = classIterable.iterator();
            MemberCriteria<Member> memberCriteria = templateObjectFactory.getObject();
            Function<Object, Object> memberIteratorTransformer = new MemberIteratorTransformer(memberCriteria);

            TransformedIterableTreeModel<Object> transformedIterableTreeModel = new TransformedIterableTreeModel<>(memberIteratorTransformer::apply);
            DepthFirstBottomUpTreeModelIterable<Object> objects = new DepthFirstBottomUpTreeModelIterable<>(transformedIterableTreeModel, classIterator);
            objects.setLeavesOnly(true);

            Iterator<Member> memberIterator = Iterators.toStream(objects.iterator()).filter(Member.class::isInstance).map(Member.class::cast).iterator();
            memberIterator = memberCriteria.applyElementFilter(memberIterator);
            memberIterator = memberCriteria.applySelectionFilter(memberIterator);
            return memberIterator;
        }

    }

    private static class AnnotatedElementIterable implements Iterable<AnnotatedElement> {

        private final Iterable<Class<?>> classIterable;
        private final ObjectFactory<MemberCriteria<Member>> templateObjectFactory;
        private final IterateStrategy traverseStrategy;

        public AnnotatedElementIterable(Iterable<Class<?>> classIterable, IterateStrategy traverseStrategy, ObjectFactory<MemberCriteria<Member>> templateObjectFactory) {
            this.classIterable = classIterable;
            this.traverseStrategy = traverseStrategy;
            this.templateObjectFactory = templateObjectFactory;
        }

        @SuppressWarnings("unchecked")
        public Iterator<AnnotatedElement> iterator() {
            Iterator<Class<?>> classIterator = classIterable.iterator();
            MemberCriteria<Member> memberCriteria = templateObjectFactory.getObject();

            Function<Object, Object> memberIteratorTransformer = new AnnotatedElementIteratorTransformer(traverseStrategy, memberCriteria);

            TransformedIterableTreeModel<Object> transformedIterableTreeModel = new TransformedIterableTreeModel<>(memberIteratorTransformer::apply);
            DepthFirstBottomUpTreeModelIterable<Object> objects = new DepthFirstBottomUpTreeModelIterable<>(transformedIterableTreeModel, classIterator);
            objects.setLeavesOnly(true);
            return Iterators.toStream(objects.iterator()).filter(AnnotatedElement.class::isInstance).map(AnnotatedElement.class::cast).iterator();
        }

    }

    private static class AnnotatedElementIteratorTransformer extends AbstractCriteriaTransformer<Object, Object> {

        private final IterateStrategy traverseStrategy;

        private final Collection<Class<?>> transformed = new ArrayList<>();

        public AnnotatedElementIteratorTransformer(IterateStrategy traverseStrategy, MemberCriteria memberCriteria) {
            super(memberCriteria);
            this.traverseStrategy = traverseStrategy;
        }

        public Object apply(Object input) {
            if (input instanceof Class<?>) {
                final Class<?> currentClass = (Class<?>) input;
                boolean hasBeenTransformed = transformed.remove(currentClass);
                if (hasBeenTransformed) {
                    switch (traverseStrategy) {
                        case MEMBERS_ONLY:
                        case PACKAGES_ONLY:
                            return Collections.emptyList().iterator();
                        default:
                            return currentClass;
                    }
                }

                transformed.add(currentClass);

                MemberCriteria memberCriteria = getMemberCriteria();

                JavaElementTraverseStrategy javaElementTraverseStrategy = traverseStrategy.getJavaElementTraverseStrategy();

                Iterator<?> iterator = javaElementTraverseStrategy.getIterator(currentClass, memberCriteria);

                return iterator;
            } else {
                return input;
            }
        }
    }

    /**
     * @return an object factory that uses this {@link MemberCriteria} as a
     * template to create a new {@link MemberCriteria}.
     */
    public ObjectFactory<MemberCriteria<Member>> getObjectFactory() {
        return new SerializableTemplateObjectFactory(this);
    }
}
