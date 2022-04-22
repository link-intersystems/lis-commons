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

import com.link_intersystems.lang.ref.Reference;
import com.link_intersystems.lang.ref.SerializableReference;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public abstract class ReflectFacade {

    private static final class SerializableReferenceImplementation implements SerializableReference<Predicate<Class<?>>> {
        /**
         *
         */
        private static final long serialVersionUID = -625148437829360498L;
        private transient Predicate<Class<?>> predicate;

        public Predicate<Class<?>> get() {
            if (predicate == null) {
                Predicate<Class<?>> isInterfacePredicate = Class::isInterface;
                predicate = isInterfacePredicate;
            }
            return predicate;
        }
    }

    private static final Predicate<Class<?>> CLASS_IS_INTERFACE_PREDICATE;

    static {
        CLASS_IS_INTERFACE_PREDICATE = new SerializablePredicate<>(new SerializableReferenceImplementation());
    }

    /**
     * Adapts the {@link Class#isInterface()} method to an {@link Predicate}.
     * This Predicate that evaluates to true if the object it is evaluated
     * against is a {@link Class} and the class is an interface.
     *
     * @return a {@link Predicate} that evaluates to true if the object it is
     * evaluated against is a {@link Class} and that {@link Class}
     * represents an interface.
     * @since 1.0.0;
     */
    public static Predicate<Class<?>> getIsInterfacePredicate() {
        return CLASS_IS_INTERFACE_PREDICATE;
    }

    /**
     * A {@link Predicate} that evaluates to true if it is evaluated against
     * {@link Member} objects and the {@link Member#getName()} is equal to the
     * memberName this Predicate was constructed with.
     *
     * @return {@link Predicate} that evaluates to true if the {@link Member}'s
     * name it is evaluated against is equal to the name that this
     * member name predicate was constructed with.
     * @since 1.0.0;
     */
    public static Predicate<Member> getMemberNamePredicate(String memberName) {
        requireNonNull(memberName);

        class SerializableMemberNamePredicate implements SerializableReference<Predicate<Member>> {

            private static final long serialVersionUID = -625148437829360498L;

            private transient Predicate<Member> predicate;

            private final String memberName;

            public SerializableMemberNamePredicate(String memberName) {
                this.memberName = memberName;
            }

            public Predicate<Member> get() {
                if (predicate == null) {
                    Predicate<String> propertyValuePredicate = ((Predicate<String>) Objects::nonNull).and(name -> name.equals(memberName));
                    Predicate<Member> instance = m -> propertyValuePredicate.test(m.getName());
                    Predicate<Member> memberNamePredicate = ((Predicate<Member>) Objects::nonNull).and(instance);
                    this.predicate = memberNamePredicate;
                }
                return predicate;
            }
        }

        return new SerializablePredicate<>(new SerializableMemberNamePredicate(memberName));
    }

    /**
     * A {@link Predicate} that evaluates to true if the object it is evaluated
     * against is a {@link Member} and that {@link Member}'s declaring class is
     * equal to the {@link Class} that this
     * declaring class member predicate was constructed with.
     *
     * @author René Link <a
     * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     * intersystems.com]</a>
     * @since 1.0.0;
     */
    public static Predicate<Member> getDeclaringClassPredicate(Class<?> declaringClass) {
        requireNonNull(declaringClass);

        class SerializableDeclaringClassPredicate implements SerializableReference<Predicate<Member>> {

            private static final long serialVersionUID = -625148437829360498L;

            private transient Predicate<Member> predicate;

            private final Class<?> declaringClass;

            public SerializableDeclaringClassPredicate(Class<?> declaringClass) {
                this.declaringClass = declaringClass;
            }

            public Predicate<Member> get() {
                if (predicate == null) {
                    Predicate<Member> propertyValuePredicate = ((Predicate<Member>) Objects::nonNull).and(m -> m.getDeclaringClass().equals(declaringClass));
                    Predicate<Member> memberNamePredicate = ((Predicate<Member>) Objects::nonNull).and(propertyValuePredicate);
                    this.predicate = memberNamePredicate;
                }
                return predicate;
            }
        }

        return new SerializablePredicate<>(new SerializableDeclaringClassPredicate(declaringClass));
    }

    /**
     * A {@link Predicate} that evaluates to true if the {@link Class} that this
     * {@link AssignablePredicate} was constructed with is assignable from the
     * class that it is evaluated against.
     *
     * @return a {@link Predicate} that evaluates to true if the {@link Class}
     * it is evaluated against is assignable from the class it was
     * constructed with.
     * @since 1.0.0;
     */
    public static Predicate<Object> getIsAssignablePredicate(Class<?> clazz) {
        requireNonNull(clazz);

        return new AssignablePredicate(clazz);

    }

    /**
     * {@link Predicate} that evaluates to true if the {@link Member}'s name it
     * is evaluated against matches a given {@link Pattern}.
     *
     * @return a {@link Predicate} that evaluates to true if it is evaluated
     * against a {@link Member} object and the {@link Member}'s name
     * matches the namePattern.
     * @since 1.0.0;
     */
    public static Predicate<Member> getMemberNamePatternPredicate(Pattern namePattern) {
        return new MemberNamePatternPredicate(namePattern);
    }

    /**
     * Compares two {@link Class}es by their canonical name.
     *
     * @return a {@link Comparator} that compares {@link Class}es by their
     * canonical name.
     * @since 1.0.0;
     */
    public static Comparator<Class<?>> getCanonicalClassNameComparator() {
        return CanonicalClassNameComparator.INSTANCE;
    }

    /**
     * Compares {@link Member}s by their name ({@link Member#getName()}).
     *
     * @return a {@link Comparator} that compares {@link Member}s by their name
     * ({@link Member#getName()}).
     * @since 1.0.0;
     */
    public static Comparator<Member> getMemberNameComparator() {
        return MemberNameComparator.INSTANCE;
    }

    /**
     * A {@link Function} that transforms a {@link Field} into it's type.
     *
     * @return a {@link Function} that transforms a {@link Field} into it's
     * type.
     * @since 1.0.0;
     */
    public static Function<Field, Class<?>> getFieldTypeTransformer() {
        return FieldTypeTransformer.getInstance();
    }

    /**
     * A {@link Function} that transforms a {@link Method} into it's return
     * type.
     *
     * @return a {@link Function} that transforms a {@link Method} into it's
     * return type.
     * @since 1.0.0;
     */
    public static Function<Method, Class<?>> getMethodTypeTransformer() {
        return Method::getReturnType;
    }

    /**
     * A factory method for {@link SerializableReference} objects that hold
     * {@link Member}s.
     *
     * @param member
     * @return a {@link SerializableReference} that represents the member. This
     * factory method can produce {@link SerializableReference}s for
     * {@link Constructor}s, {@link Method}s and {@link Field}s.
     * @since 1.0.0;
     */
    public static SerializableReference<? extends Member> getSerializableReference(Member member) {
        if (member instanceof Method) {
            return getSerializableReference((Method) member);
        } else if (member instanceof Field) {
            return getSerializableReference((Field) member);
        } else if (member instanceof Constructor<?>) {
            return getSerializableReference((Constructor<?>) member);
        } else {
            throw new IllegalArgumentException("member must be one of the type " + Constructor.class + ", " + Method.class + " or " + Field.class);
        }
    }

    /**
     * A factory method for {@link SerializableReference} object that hold
     * {@link Constructor}s.
     *
     * @param constructor
     * @return a {@link SerializableReference} that represents the
     * {@link Constructor} and ensures that the constructor is available
     * each time the {@link Reference#get()} method is called. Even
     * after serialisation/deserialization of the
     * {@link SerializableReference}.
     * @since 1.0.0;
     */
    public static SerializableReference<Constructor<?>> getSerializableReference(Constructor<?> constructor) {
        return new SerializableConstructor(constructor);
    }

    /**
     * A factory method for {@link SerializableReference} object that hold
     * {@link Method}s.
     *
     * @param method
     * @return a {@link SerializableReference} that represents the
     * {@link Method} and ensures that the method is available each time
     * the {@link Reference#get()} method is called. Even after
     * serialisation/deserialization of the
     * {@link SerializableReference}.
     * @since 1.0.0;
     */
    public static SerializableReference<Method> getSerializableReference(Method method) {
        return new SerializableMethod(method);
    }

    /**
     * A factory method for {@link SerializableReference} object that hold
     * {@link Field}s.
     *
     * @param field
     * @return a {@link SerializableReference} that represents the {@link Field}
     * and ensures that the field is available each time the
     * {@link Reference#get()} method is called. Even after
     * serialisation/deserialization of the
     * {@link SerializableReference}.
     * @since 1.0.0;
     */
    public static SerializableReference<Field> getSerializableReference(Field field) {
        return new SerializableField(field);
    }

    /**
     * A factory method for {@link SerializableReference} object that hold
     * {@link Package}s.
     *
     * @param packageObj
     * @return a {@link SerializableReference} that represents the
     * {@link Package} and ensures that the package is available each
     * time the {@link Reference#get()} method is called. Even after
     * serialisation/deserialization of the
     * {@link SerializableReference}.
     * @since 1.0.0;
     */
    public static SerializableReference<Package> getSerializableReference(Package packageObj) {
        return new SerializablePackage(packageObj);
    }
}

/**
 * Compares two {@link Member}s by their name ({@link Member#getName()}).
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class MemberNameComparator implements Comparator<Member>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8826998287267302658L;

    /**
     * Singleton instance of the {@link MemberNameComparator}.
     *
     * @since 1.0.0;
     */
    public static final Comparator<Member> INSTANCE = new MemberNameComparator();

    /**
     * {@inheritDoc}
     *
     * @see MemberNameComparator class javadoc
     * @since 1.0.0;
     */
    public int compare(Member o1, Member o2) {
        int compareValue;
        if (o1 == null) {
            if (o2 == null) {
                compareValue = 0;
            } else {
                compareValue = -1;
            }
        } else {
            if (o2 == null) {
                compareValue = 1;
            } else {
                String name1 = o1.getName();
                String name2 = o2.getName();
                compareValue = name1.compareTo(name2);
            }
        }
        return compareValue;
    }

}

/**
 * A {@link Function} that transforms a {@link Method} into it's return type.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class MethodTypeTransformer implements Function<Method, Class<?>>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7766636718627804940L;

    /**
     * See class documentation.
     *
     * @return
     */
    public static Function<Method, Class<?>> getInstance() {
        return new MethodTypeTransformer();
    }

    /**
     * {@inheritDoc} Transforms a {@link Method} into it's return type.
     *
     * @since 1.0.0;
     */
    public Class<?> apply(Method input) {
        return input.getReturnType();
    }

}

/**
 * A {@link Function} that transforms a {@link Field} into it's type.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class FieldTypeTransformer implements Function<Field, Class<?>>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5506590991172237537L;

    /**
     * See class documentation.
     *
     * @return
     * @since 1.0.0;
     */
    public static Function<Field, Class<?>> getInstance() {
        return new FieldTypeTransformer();
    }

    /**
     * {@inheritDoc}
     *
     * @param input must be a {@link Field}.
     * @since 1.0.0;
     */
    public Class<?> apply(Field input) {
        Class<?> type = input.getType();
        return type;
    }

}

/**
 * {@link Predicate} that evaluates to true if the {@link Member}'s name it is
 * evaluated against matches a given {@link Pattern}.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class MemberNamePatternPredicate implements Predicate<Member>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7433776975628507987L;

    private transient Matcher matcher;

    private final Pattern namePattern;

    /**
     * Constructs a {@link MemberNamePatternPredicate} with the given
     * {@link Pattern}.
     *
     * @param namePattern the {@link Pattern} to use when evaluating.
     * @since 1.0.0;
     */
    public MemberNamePatternPredicate(Pattern namePattern) {
        requireNonNull(namePattern);
        this.namePattern = namePattern;
        matcher = namePattern.matcher("");
    }

    private Matcher getMatcher(String string) {
        if (matcher == null) {
            matcher = namePattern.matcher(string);
        } else {
            matcher.reset(string);
        }
        return matcher;
    }

    /**
     * {@inheritDoc}
     *
     * @see MemberNamePatternPredicate class javadoc
     * @since 1.0.0;
     */
    public boolean test(Member member) {
        String memberName = member.getName();
        Matcher matcher = getMatcher(memberName);
        return matcher.matches();
    }

}

/**
 * An {@link Predicate} that evaluates to true if the {@link Class} that this
 * {@link AssignablePredicate} was constructed with is assignable from the class
 * that it is evaluated against.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class AssignablePredicate implements Predicate<Object>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8619979013477832617L;

    private final Class<?> clazz;

    public static Predicate<Object> getInstance(Class<?> clazz) {
        return new AssignablePredicate(clazz);
    }

    /**
     * Constructs a new {@link AssignablePredicate} that evaluates to true if
     * the given class is assignable from the class it is evaluated against.
     *
     * @param clazz
     * @since 1.0.0;
     */
    public AssignablePredicate(Class<?> clazz) {
        this.clazz = requireNonNull(clazz);
    }

    /**
     * {@inheritDoc}
     *
     * @param object must be a {@link Class}. Other objects always evaluate to
     *               false.
     * @since 1.0.0;
     */
    public boolean test(Object object) {
        boolean isAssignable = false;
        Class<?> class2Compare = null;

        if (object instanceof Class<?>) {
            class2Compare = Class.class.cast(object);
        }

        if (class2Compare == null && object != null) {
            class2Compare = object.getClass();
        }

        if (class2Compare != null) {
            isAssignable = clazz.isAssignableFrom(class2Compare);
        }

        return isAssignable;
    }

}

/**
 * Compares two {@link Class}es by their canonical name.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
class CanonicalClassNameComparator implements Comparator<Class<?>>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1112739163768529278L;

    /**
     * A singleton of the {@link CanonicalClassNameComparator}.
     *
     * @since 1.0.0;
     */
    public static final Comparator<Class<?>> INSTANCE = new CanonicalClassNameComparator();

    /**
     * {@inheritDoc}
     * <p>
     * Compares two {@link Class}es by their canonical name.
     * </p>
     *
     * @since 1.0.0;
     */
    public int compare(Class<?> o1, Class<?> o2) {
        int compareValue;
        if (o1 == null) {
            if (o2 == null) {
                compareValue = 0;
            } else {
                compareValue = -1;
            }
        } else {
            if (o2 == null) {
                compareValue = 1;
            } else {
                String canonicalName1 = o1.getCanonicalName();
                String canonicalName2 = o2.getCanonicalName();
                compareValue = canonicalName1.compareTo(canonicalName2);
            }
        }
        return compareValue;
    }

}

class SerializablePredicate<T> implements Predicate<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6679937464429440905L;

    private SerializableReference<Predicate<T>> serializablePredicate;

    public SerializablePredicate(SerializableReference<Predicate<T>> serializablePredicate) {
        this.serializablePredicate = requireNonNull(serializablePredicate);
    }

    public boolean test(T object) {
        Predicate<T> predicate = serializablePredicate.get();
        return predicate.test(object);
    }
}