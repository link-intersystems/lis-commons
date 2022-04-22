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

import com.link_intersystems.lang.ClassLoaderContextAware;
import com.link_intersystems.lang.Signature;
import com.link_intersystems.lang.reflect.PotentiallyApplicableMemberStrategy.PotentiallyApplicableCriteria;
import com.link_intersystems.lang.reflect.PotentiallyApplicableMemberStrategy.PotentionallyApplicableConstructorCriteria;
import com.link_intersystems.lang.reflect.PotentiallyApplicableMemberStrategy.PotentionallyApplicableMethodCriteria;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * An extension to the {@link Class} that encapsulates complex logic like
 * finding an applicable method or constructor and handling generic types.
 *
 *
 * <h2>Generic type handling</h2>
 * <p>
 * Suppose the following hierarchy
 * </p>
 *
 * <pre>
 *
 *                             +-----------------+                               +-------------------+
 *                             |  AInterface&lt;A&gt;  |                               | BInterface&lt;B, BB&gt; |
 *                             +-----------------+                               +-------------------+
 *                                      ^                                                  ^
 *                                      |                                                  |
 *                 +-----------------------------------------+       +-------------------------------------------+
 *                 |  AClass&lt;A&gt; implements AInterface&lt;Long&gt;  |       | CInterface&lt;C,CC&gt; extends BInterface&lt;CC,C&gt; |
 *                 +-----------------------------------------+       +-------------------------------------------+
 *                                      ^                                                  ^
 *                                      |                                                  |
 *                +---------------------+--------------------------------------------------+
 *                |                                                                        |
 * +------------------------------+          +-------------------------------------------------------------------+
 * |BClass extends AClass&lt;Integer&gt;|          | CClass extends AClass&lt;Float&gt; implements CInterface&lt;Byte, Boolean&gt; |
 * +------------------------------+          +-------------------------------------------------------------------+
 *
 * </pre>
 * <p>
 * In the previous described hierarchy the following code's assertions will be
 * true
 *
 * <pre>
 * Class2 aClass = Class2.forClass(AClass.class);
 * Class2 bClass = Class2.forClass(BClass.class);
 * Class2 cClass = Class2.forClass(CClass.class);
 *
 * TypeVariable&lt;?&gt; aClassTypeVariable = aClass.getTypeVariable(&quot;A&quot;);
 * Type bBoundType = bClass.getBoundType(aClassTypeVariable);
 * assertEqual(Integer.class, bBoundType);
 *
 * Type cBoundType = cClass.getBoundType(aClassTypeVariable);
 * assertEqual(Float.class, cBoundType);
 *
 * // bounded types are also resolved in a complex hierarchy,
 * // even if the bounded types got swapped in
 * // the hierarchy like in CInterface&lt;C,CC&gt; extends BInterface&lt;CC,C&gt;
 * Class2 bInterface = Class2.forClass(BInterface);
 * TypeVariable&lt;?&gt; bInterfaceTypeVariable = bInterface.getTypeVariable(&quot;BB&quot;);
 * cBoundType = cClass.getBoundType(bInterfaceTypeVariable);
 * assertEqual(Byte.class, cBoundType);
 * </pre>
 * <p>
 *
 * </p>
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
 */
public class Class2<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1718130720537907850L;

    private final Class<T> clazz;

    private Class2<T[]> arrayType2;

    private List<Method2> declaredMethods;

    private List<Constructor2<T>> declaredConstructors;

    /**
     * internal cache for type variables. I expect that a "normal" generic type
     * will not have more than 3 type variables. Therefore I use the Flat3Map
     * that can grow but is very fast for up to 3 entries.
     */
    private transient Map<String, TypeVariable<?>> typeVariableCache;

    private static final Object TYPE_VARIABLE_CACHE_SYNCHRONIZATION = new Object();

    private static final Map<Class<?>, Class2<?>> CLASS_TO_CLASS2 = new HashMap<>();

    private static final PotentiallyApplicableMemberStrategy POTENTIALLY_APPLICABLE_STRATEGY = new PotentiallyApplicableMemberStrategy();

    private static final ChooseMostSpecificMemberStrategy<Member2<?>> CHOOSE_POTENTIAL_APPLICABLE = new ChooseMostSpecificMemberStrategy<>();

    private ArrayType<T> arrayType;

    @SuppressWarnings("unchecked")
    private static <RT extends Member2<?>> ChooseMostSpecificMemberStrategy<RT> getChooseMostSpecificStrategy() {
        return (ChooseMostSpecificMemberStrategy<RT>) CHOOSE_POTENTIAL_APPLICABLE;
    }

    /**
     * @return a {@link Class2} object that represents the {@link Class} defined
     * by the full qualified class name.
     * @since 1.0.0;
     * @deprecated use {@link #get(String)} instead.
     */
    public static <T> Class2<T> forName(String className)
            throws ClassNotFoundException {
        return get(className);
    }

    /**
     * @return a {@link Class2} object that represents the {@link Class} defined
     * by the full qualified class name.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class2<T> get(String className)
            throws ClassNotFoundException {
        Class<T> classForName = (Class<T>) Class.forName(className);
        return get(classForName);
    }

    /**
     * @return a {@link Class2} for the given {@link Class}.
     * @since 1.0.0;
     * @deprecated use {@link #get(Class)} instead.
     */
    public static <T> Class2<T> forClass(Class<T> clazz) {
        return get(clazz);
    }

    /**
     * @return a {@link Class2} for the given {@link Class}.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> Class2<T> get(Class<T> clazz) {
        requireNonNull(clazz);
        Class2<T> class2 = (Class2<T>) CLASS_TO_CLASS2.get(clazz);
        if (class2 == null) {
            class2 = new Class2<>(clazz);
            CLASS_TO_CLASS2.put(clazz, class2);
        }
        return class2;
    }

    /**
     * Constructs a new {@link Class2} for class clazz.
     *
     * @param clazz the class to get a {@link Class2} for.
     */
    protected Class2(Class<T> clazz) {
        this.clazz = requireNonNull(clazz);
    }

    /**
     * @return the {@link ClassLoaderContextAware} that uses this class's class
     * loader. If this class has no class loader (java system classes)
     * it returns a {@link ClassLoaderContextAware} that uses the
     * {@link ClassLoader#getSystemClassLoader()}.
     * @since 1.2.0;
     */
    public ClassLoaderContextAware getClassLoaderContextAware() {
        ClassLoader classLoader = clazz.getClassLoader();
        ClassLoaderContextAware classLoaderContext = ClassLoaderContextAware
                .forClassLoader(classLoader);
        return classLoaderContext;
    }

    /**
     * @return the {@link Package2} that this {@link Class2} belongs to.
     * @since 1.2.0;
     */
    public Package2 getPackage() {
        return Package2.get(getType().getPackage());
    }

    /**
     * Constructs a new instance of the type described by this {@link Class2}
     * with the given arguments.
     *
     * @param constructorArgs the arguments for creating an instance of this {@link Class2}
     *                        's type.
     * @return a new instance of the type represented by this {@link Class2}.
     * @throws Exception if one of the declared exceptions (if any) of the constructor
     *                   is thrown.
     * @since 1.2.0;
     */
    public T newInstance(Object... constructorArgs) throws Exception {
        Constructor2<T> applicableConstructor = getApplicableConstructor(constructorArgs);
        T newInstance = applicableConstructor.newInstance(constructorArgs);
        return newInstance;
    }

    /**
     * @return the {@link Class} object that this {@link Class2} is based on.
     * @since 1.0.0;
     */
    public Class<T> getType() {
        return clazz;
    }

    /**
     * Returns the array type of this {@link Class2}'s type, e.g. if this
     * <code>Class2</code>'s type is <code>Object</code> the returned type is
     * <code>Object[]</code>. This method is a way to dynamically create an
     * array type.
     *
     * @return the array type of this {@link Class2}'s type, e.g. if this
     * <code>Class2</code>'s type is <code>Object</code> the returned
     * type is <code>Object[]</code>. This method is a way to
     * dynamically create an array type.
     * @since 1.2.0;
     */
    public ArrayType<T> getArrayType() {
        if (arrayType == null) {
            arrayType = new ArrayType<>(getType());
        }
        return arrayType;
    }


    /**
     * Identifies the {@link Constructor2} of this {@link Class2} that is
     * applicable for the invocation parameters. Uses the same search algorithm
     * as {@link #getApplicableMethod(String, AccessType[], Class...)} . If the
     * {@link Class} represented by this {@link Class2} is an interface this
     * method does never return a {@link Constructor2} always null.
     *
     * @return the applicable constructor or null if no applicable constructor
     * could be found.
     * @since 1.0.0;
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Constructor2<T> getApplicableConstructor(AccessType[] accessTypes,
                                                    Class<?>... invocationParameters) {
        PotentionallyApplicableConstructorCriteria potentionallyApplicableConstructorCriteria = new PotentionallyApplicableConstructorCriteria(
                accessTypes, invocationParameters);
        return getApplicableConstructor(potentionallyApplicableConstructorCriteria);
    }

    private Constructor2<T> getApplicableConstructor(PotentionallyApplicableConstructorCriteria potentionallyApplicableConstructorCriteria) {
        List<Constructor2<T>> constructorsInternal = getConstructors();
        List<Constructor2<T>> potentiallyApplicable = getPotentiallyApplicable(constructorsInternal, potentionallyApplicableConstructorCriteria);
        return chooseApplicableMember(potentiallyApplicable);
    }

    /**
     * Identifies the {@link Constructor2} of this {@link Class2} that is
     * applicable for the invocation parameters and {@link AccessType}s. If this
     * class represents an interface null is returned.
     *
     * @param accessTypes the {@link AccessType} that the selected constructor must
     *                    have.
     * @since 1.0.0;
     */
    public Constructor2<T> getApplicableConstructor(AccessType[] accessTypes,
                                                    Object... invocationArguments) {
        PotentionallyApplicableConstructorCriteria potentionallyApplicableConstructorCriteria = new PotentionallyApplicableConstructorCriteria(
                accessTypes, invocationArguments);
        return getApplicableConstructor(potentionallyApplicableConstructorCriteria);
    }

    private <C extends Member2<?>> List<C> getPotentiallyApplicable(
            List<C> candidates,
            PotentiallyApplicableCriteria<C> potentiallyApplicableCriteria) {
        return POTENTIALLY_APPLICABLE_STRATEGY
                .getPotentialApplicable(candidates,
                        potentiallyApplicableCriteria);
    }

    private <RT extends Member2<?>> RT chooseApplicableMember(
            List<RT> potentiallyApplicable) {
        ChooseMostSpecificMemberStrategy<RT> chooseMostSpecificStrategy = getChooseMostSpecificStrategy();
        RT mostSpecific = chooseMostSpecificStrategy
                .chooseMostSpecific(potentiallyApplicable);
        return mostSpecific;
    }

    /**
     * Returns the declaring method2 object using the same logic as
     * {@link Class#getDeclaredMethod(String, Class...)}.
     *
     * @param name           the name of the method.
     * @param parameterTypes the exact parameter types that the declaring method must have.
     * @return the {@link Method2} that represents the declaring method with the
     * name and Assert.
     * @throws NoSuchMethodException if no method matches the parameters.
     * @since 1.0.0;
     */
    public Method2 getDeclaringMethod2(String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        requireNonNull(parameterTypes);
        List<Method2> declaredMethods = getDeclaredMethods();
        for (Method2 method2 : declaredMethods) {
            Class<?>[] parameterTypes2 = method2.getParameterTypes();
            if (Arrays.equals(parameterTypes, parameterTypes2)) {
                return method2;
            }
        }

        throw new NoSuchMethodException("No method named " + name + "( )");
    }

    /**
     * Returns the {@link Method2} of this {@link Class2} that matches the
     * signature or null if no method matches.
     *
     * @param signature the {@link Signature} of the requested method
     * @return the {@link Method2} of this {@link Class2} that matches the
     * signature or null if no method matches.
     * @since 1.0.0;
     */
    public Method2 getMethod2(Signature signature) {
        List<Method2> declaredMethods = getDeclaredMethods();
        for (Method2 method2 : declaredMethods) {
            if (method2.getSignature().equals(signature)) {
                return method2;
            }
        }
        return null;
    }

    /**
     * @return the {@link Method2} for the given {@link Method}. The
     * {@link Method} must be declared on this {@link Class2}'s
     * {@link Class}.
     * @throws NoSuchMethodException if the method is not declared on this {@link Class2}'s
     *                               {@link Class}.
     * @since 1.0.0;
     */
    Method2 getMethod2(Method method) throws NoSuchMethodException {
        requireNonNull(method);
        List<Method2> declaredMethods = getDeclaredMethods();
        for (Method2 method2 : declaredMethods) {
            if (method.equals(method2.getMember())) {
                return method2;
            }
        }
        throw new NoSuchMethodException(method + " must be a method of this "
                + clazz);
    }

    /**
     * Identifies the {@link Constructor2} of this {@link Class2} that is
     * applicable for the invocation parameters regardless to the access type.
     * If this class represents an interface null is returned.
     *
     * @return the applicable constructor or null if no applicable constructor
     * could be found.
     * @see #getApplicableConstructor(AccessType[], Class...)
     * @since 1.0.0;
     */
    public Constructor2<T> getApplicableConstructor(Class<?>... paramTypes) {
        return getApplicableConstructor(AccessType.values(), paramTypes);
    }

    /**
     * {@inheritDoc}
     *
     * @param args
     * @return
     */
    public Constructor2<T> getApplicableConstructor(Object... args) {
        return getApplicableConstructor(AccessType.values(), args);
    }

    /**
     * Identifies the {@link Method2} of this {@link Class2} that is applicable
     * for the name and invocation parameters regardless to the access type.
     *
     * @return the applicable method or null if no applicable method could be
     * found.
     * @see #getApplicableMethod(String, AccessType[], Class...)
     * @since 1.0.0;
     */
    public Method2 getApplicableMethod(String name, Class<?>... paramTypes) {
        return getApplicableMethod(name, AccessType.values(), paramTypes);
    }

    public Method2 getApplicableMethod(String name, Object... args) {
        return getApplicableMethod(name, AccessType.values(), args);

    }

    /**
     * Identifies the {@link Method2} of this {@link Class2} that is applicable
     * for the name and invocation parameters, including those methods declared
     * by this class or interface and those inherited from superclasses and
     * superinterfaces. Uses the search algorithm defined by the java language
     * specification - 15.12.2.1 Identify Potentially Applicable Methods.
     *
     * <pre>
     *  <h2>15.12.2.1 Identify Potentially Applicable Methods.</h2>
     *  A member method is <i>potentially applicable</i> to a method invocation if and only if all of
     *  the following are true:
     *  <ul>
     *  <li>The name of the member is identical to the name of the method in the method invocation.</li>
     *  <li>The member is accessible (§6.6) to the class or interface in which the method
     * invocation appears.</li>
     *  <li>The arity of the member is lesser or equal to the arity of the method invocation.</li>
     *  <li>If the member is a variable arity method with arity <i>n</i>, the arity of the method
     *  invocation is greater or equal to <i>n</i>-1.</li>
     *  <li>If the member is a fixed arity method with arity <i>n</i>, the arity of the method
     *  invocation is equal to <i>n</i></li>
     *  <li>If the method invocation includes explicit type parameters, and the member is a generic
     *  method, then the number of actual type parameters is equal to the number of formal type
     *  parameters.</li>
     *  </ul>
     * </pre>
     *
     * @param name       the method's name.
     * @param paramTypes the parameter classes.
     * @return the applicable method or null if no applicable method exists in
     * this class nor in it's superclasses.
     * @since 1.0.0;
     */
    public Method2 getApplicableMethod(String name, AccessType[] accessTypes,
                                       Class<?>... paramTypes) {
        Method2 applicableMethod = getDeclaredApplicableMethod(name,
                accessTypes, paramTypes);
        if (applicableMethod == null) {
            Class2<? super T> superclass2 = getSuperclass2();
            if (superclass2 != null) {
                applicableMethod = superclass2.getApplicableMethod(name,
                        accessTypes, paramTypes);
            }
        }
        return applicableMethod;
    }

    /**
     * Identifies the {@link Method2} of this {@link Class2} that is applicable
     * for the name and invocation parameters, but excludes inherited methods.
     * Uses the search algorithm defined by the java language specification -
     * 15.12.2.1 Identify Potentially Applicable Methods.
     *
     * <pre>
     *  <h2>15.12.2.1 Identify Potentially Applicable Methods.</h2>
     *  A member method is <i>potentially applicable</i> to a method invocation if and only if all of
     *  the following are true:
     *  <ul>
     *  <li>The name of the member is identical to the name of the method in the method invocation.</li>
     *  <li>The member is accessible (§6.6) to the class or interface in which the method
     * invocation appears.</li>
     *  <li>The arity of the member is lesser or equal to the arity of the method invocation.</li>
     *  <li>If the member is a variable arity method with arity <i>n</i>, the arity of the method
     *  invocation is greater or equal to <i>n</i>-1.</li>
     *  <li>If the member is a fixed arity method with arity <i>n</i>, the arity of the method
     *  invocation is equal to <i>n</i></li>
     *  <li>If the method invocation includes explicit type parameters, and the member is a generic
     *  method, then the number of actual type parameters is equal to the number of formal type
     *  parameters.</li>
     *  </ul>
     * </pre>
     *
     * @param name       the method's name.
     * @param paramTypes the parameter classes.
     * @return the applicable method or null if no applicable method exists in
     * this class.
     * @since 1.2.0;
     */
    public Method2 getDeclaredApplicableMethod(String name,
                                               AccessType[] accessTypes, Class<?>... paramTypes) {
        List<Method2> declaredMethods = getDeclaredMethods();
        PotentionallyApplicableMethodCriteria potentionallyApplicableMethodCriteria = new PotentionallyApplicableMethodCriteria(
                name, accessTypes, paramTypes);

        List<Method2> potentiallyApplicable = getPotentiallyApplicable(
                declaredMethods, potentionallyApplicableMethodCriteria);
        return chooseApplicableMember(potentiallyApplicable);
    }

    /**
     * Same behavior as
     * {@link #getApplicableMethod(String, AccessType[], Class...)}, but uses
     * invocation parameter objects. E.g. finds a methods that is applicable to
     * be invoked with the invocation parameter objects.
     *
     * @param name the name of the method.
     * @param args the invocation parameters.
     * @return the applicable method or null if no applicable method exists in
     * this class nor in it's superclasses.
     * @since 1.2.0;
     */
    public Method2 getApplicableMethod(String name, AccessType[] accessTypes,
                                       Object... args) {
        Method2 applicableMethod = getDeclaredApplicableMethod(name,
                accessTypes, args);
        if (applicableMethod == null) {
            Class2<? super T> superclass2 = getSuperclass2();
            if (superclass2 != null) {
                applicableMethod = superclass2.getApplicableMethod(name,
                        accessTypes, args);
            }
        }
        return applicableMethod;
    }

    /**
     * Same behavior as
     * {@link #getDeclaredApplicableMethod(String, AccessType[], Class...)}, but
     * uses invocation parameter objects. Finds a methods that is applicable to
     * be invoked with the invocation parameter objects.
     *
     * @param name the name of the method.
     * @param args the invocation parameters.
     * @return the applicable method or null if no applicable method exists in
     * this class.
     * @since 1.2.0;
     */
    public Method2 getDeclaredApplicableMethod(String name,
                                               AccessType[] accessTypes, Object... args) {
        List<Method2> declaredMethods = getDeclaredMethods();
        PotentionallyApplicableMethodCriteria potentionallyApplicableMethodCriteria = new PotentionallyApplicableMethodCriteria(
                name, accessTypes, args);

        List<Method2> potentiallyApplicable = getPotentiallyApplicable(
                declaredMethods, potentionallyApplicableMethodCriteria);
        return chooseApplicableMember(potentiallyApplicable);
    }

    /**
     * Convenience method to get a {@link Class2} instance of this class's
     * superclass.
     *
     * @return the {@link Class2} instance that represents this class's
     * superclass if any. If this class represents the java
     * {@link Object} class null is returned.
     * @since 1.2.0;
     */
    public Class2<? super T> getSuperclass2() {
        Class<? super T> superclass = clazz.getSuperclass();
        if (superclass == null) {
            return null;
        }
        Class2<? super T> superclass2 = Class2.get(superclass);
        return superclass2;
    }

    /**
     * @return the {@link Method2}s for this {@link Class2}. Supposed to be used
     * by sub classes. Subclasses must not modify the returned array.
     * @since 1.0.0;
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Constructor2<T>> getConstructors() {
        if (declaredConstructors == null) {
            if (clazz.isInterface()) {
                declaredConstructors = Collections.emptyList();
            } else {
                Constructor<?>[] declaredConstructors = clazz
                        .getDeclaredConstructors();
                List<Constructor2<T>> constructors = new ArrayList<>();
                for (Constructor<?> declaredConstructor : declaredConstructors) {
                    constructors.add(new Constructor2(declaredConstructor));
                }
                this.declaredConstructors = Collections
                        .unmodifiableList(constructors);
            }
        }
        return declaredConstructors;
    }

    /**
     * @return the {@link Method2}s for this {@link Class2}. Supposed to be used
     * by sub classes.
     * @since 1.0.0;
     */
    private List<Method2> getDeclaredMethods() {
        if (declaredMethods == null) {
            List<Method2> declaredMethods = new ArrayList<>();
            Method[] declaredMethodArray = clazz.getDeclaredMethods();
            for (Method method : declaredMethodArray) {
                declaredMethods.add(new Method2(method));
            }
            this.declaredMethods = Collections
                    .unmodifiableList(declaredMethods);
        }
        return declaredMethods;
    }

    /**
     * The generic type variables defined at this class.
     *
     * @since 1.2.0;
     */
    public TypeVariable<?>[] getTypeVariables() {
        return clazz.getTypeParameters();
    }

    /**
     * @return the {@link TypeVariable} for the given name that is defined on
     * this generic type or null if no {@link TypeVariable} is
     * defined with that name. This method only looks at the current
     * type represented by this generic object and not on super
     * types.
     * @since 1.2.0;
     */
    public TypeVariable<?> getTypeVariable(String typeVarName) {
        requireNonNull(typeVarName);
        TypeVariable<?> typeVariable = getTypeVariableCache().get(typeVarName);
        if (typeVariable == null) {
            typeVarName = typeVarName.trim();
            TypeVariable<?>[] typeParams = clazz.getTypeParameters();
            for (TypeVariable<?> typeParameter : typeParams) {
                if (typeVarName.equals(typeParameter.getName())) {
                    typeVariable = typeParameter;
                    getTypeVariableCache().put(typeVarName, typeVariable);
                    break;
                }
            }
        }
        return typeVariable;
    }

    private Map<String, TypeVariable<?>> getTypeVariableCache() {
        synchronized (TYPE_VARIABLE_CACHE_SYNCHRONIZATION) {
            if (typeVariableCache == null) {
                typeVariableCache = new HashMap<>();
            }
            return typeVariableCache;
        }
    }

    /**
     * @param typeVariable the {@link TypeVariable} to get the bounded type for.
     * @return the type that is bound on this generic type for the given
     * {@link TypeVariable} or null if the type is not bound on this
     * generic's type. If the bound type is a generic type the
     * raw type will be returned.
     * @since 1.2.0;
     */
    public Type getBoundType(TypeVariable<?> typeVariable) {
        requireNonNull(typeVariable);
        Type type = doGetBoundType(typeVariable, clazz);
        if (type instanceof TypeVariable<?>) {
            typeVariable = (TypeVariable<?>) type;
            type = getBoundType(typeVariable);
        }
        return type;
    }

    /**
     * Convenience method that returns the bound class of the first type
     * variable occurrence in the class's hierarchy. Every class in the
     * hierarchy will be traversed. If a class doesn't define a type variable
     * with the required name that class's interfaces gets traversed too. If
     * also no interface has a type variable with the required name the next
     * class in the hierarchy will be traversed.
     *
     * @return the class bound to this class's type variable with the
     * typeVarName. Searches the class hierarchy for the first
     * occurrence of a type variable with the given name.
     * @throws IllegalArgumentException if no type variable definition could be found in this class's
     *                                  hierarchy.
     * @since 1.2.0;
     */
    public <C> Class<C> getBoundClass(String typeVarName) {
        class BoundClassIterator implements Iterator<Class<?>> {

            private Queue<Class<?>> types = new LinkedList<>();

            public BoundClassIterator(Class<T> type) {
                types.add(type);
            }

            @Override
            public boolean hasNext() {
                return !types.isEmpty();
            }

            @Override
            public Class<?> next() {
                Class<?> nextType = types.poll();

                Class<?> superclass = nextType.getSuperclass();
                if (superclass != null) {
                    types.add(superclass);
                }
                Class<?>[] interfaces = nextType.getInterfaces();

                types.addAll(Arrays.asList(interfaces));

                return nextType;
            }
        }

        BoundClassIterator boundClassIterator = new BoundClassIterator(getType());

        TypeVariable<?> typeVariable = null;
        while (boundClassIterator.hasNext()) {
            Class<?> clazzInHierarchy = boundClassIterator.next();
            Class2<?> class2InHierarchy = Class2.get(clazzInHierarchy);
            typeVariable = class2InHierarchy.getTypeVariable(typeVarName);
            if (typeVariable != null) {
                break;
            }
        }

        if (typeVariable == null) {
            throw new IllegalArgumentException("No type variable named "
                    + typeVarName + " was found in the hierarchy of "
                    + getType());
        }
        return getBoundClass(typeVariable);
    }

    /**
     * @return the class that is bound on this generic type for the
     * given {@link TypeVariable} or null if the type bound is not a
     * Class<?>. If the bound type resolved for the {@link TypeVariable}
     * is itself a ( {@link ParameterizedType} ) the raw type will be
     * returned.
     */
    @SuppressWarnings("unchecked")
    public <C> Class<C> getBoundClass(TypeVariable<?> typeVariable) {
        Type boundType = getBoundType(typeVariable);
        if (boundType == null) {
            return null;
        }
        if (boundType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) boundType;
            boundType = parameterizedType.getRawType();
        }

        if (boundType instanceof GenericArrayType) {
            GenericArrayType genericArrayType = GenericArrayType.class.cast(boundType);
            Type genericComponentType = genericArrayType
                    .getGenericComponentType();
            Class<?> componentType = Class.class.cast(genericComponentType);
            Object array = Array.newInstance(componentType, 0);
            boundType = array.getClass();
        }

        return (Class<C>) boundType;
    }

    /**
     * @param <TI>            the expected type so that clients must not cast.
     * @param typeVariable    the type variable that defines the class to be instantiated.
     * @param constructorArgs the arguments to use when constructing the bound type.
     * @return an instance of the bound type defined by the typeVariable -
     * constructing it with the constructorArgs. The right constructor
     * is determined by the constructorArgs object's types.
     * @throws IllegalStateException    in case of an {@link InstantiationException},
     *                                  {@link IllegalAccessException} or
     *                                  {@link InvocationTargetException} that arises when trying to
     *                                  instantiate the class bound to the type variable.
     * @throws IllegalArgumentException if the type bound to the type variable is an interface or the
     *                                  constructor that is called to instantiate the class bound to
     *                                  the type variable itself throws an
     *                                  {@link IllegalArgumentException} or. The original exception
     *                                  is wrapped to provide more information.
     */
    public <TI> TI getBoundInstance(TypeVariable<?> typeVariable,
                                    Object... constructorArgs) {
        Class<TI> typeClass = getBoundClass(typeVariable);
        if (typeClass.isInterface()) {
            throw new IllegalArgumentException("The type variable's ("
                    + typeVariable + ") bound type (" + typeClass
                    + ") is an interface.");
        }
        Class2<TI> class2 = Class2.get(typeClass);
        Class<?>[] constructorArgClasses = new Class<?>[constructorArgs.length];
        for (int i = 0; i < constructorArgs.length; i++) {
            Object arg = constructorArgs[i];
            if (arg != null) {
                constructorArgClasses[i] = arg.getClass();
            }
        }

        Constructor2<?> constructor2 = class2
                .getApplicableConstructor(constructorArgClasses);
        if (constructor2 == null) {
            throw new IllegalArgumentException(
                    "Type variable's ("
                            + typeVariable
                            + ") bound type "
                            + typeClass
                            + " doesn't have a constructor applicable for the argument types "
                            + Arrays.asList(constructorArgClasses));
        }
        try {
            TI t = constructor2.getInvokable().invoke(constructorArgs);
            return t;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to instantiate an object of " + typeClass
                            + " using constructor " + constructor2.getMember()
                            + " with arguments "
                            + Arrays.asList(constructorArgs), e);
        }
    }

    /**
     * Same as {@link #toString(String...)} with parameter "java.lang".
     *
     * @return a string representation of this {@link Class2};
     * @since 1.0.0;
     */
    public String toString() {
        return toString("java.lang");
    }

    /**
     * The string representation of a {@link Class2}.
     *
     * <ul>
     * <li>
     * Types are represented by their canonical name. If a type is a
     * &quot;well-known&quot; type (all types in java.lang) the type's simple
     * name is used. E.g. String - java.util.List.</li>
     * <ul>
     * <li>
     * Arrays are represented by their type and appended by []. E.g. int[]
     * String[] java.beans.PropertyDescriptor[].</li>
     *
     * @param wellKnownPackages packages that are &quot;well known&quot; will not be printed
     *                          in the string representation. E.g. if java.lang is defined as
     *                          well known the Class2 that represents a String class will be
     *                          printed just as &quot;String&quot; and not java.lang.String.
     * @return a string representation of this {@link Class2};
     * @since 1.0.0;
     */
    public String toString(String... wellKnownPackages) {
        requireNonNull(wellKnownPackages);
        StringBuilder toStringBuilder = new StringBuilder();
        Class<?> clazz = getType();
        boolean isArray = clazz.isArray();
        if (isArray) {
            clazz = clazz.getComponentType();
        }
        Package clazzPackage = clazz.getPackage();
        String packageName = "";
        if (clazzPackage != null) {
            packageName = clazzPackage.getName();
        }

        boolean isWellKnownPackage = Arrays.binarySearch(wellKnownPackages,
                packageName) > -1;

        if (isWellKnownPackage) {
            toStringBuilder.append(clazz.getSimpleName());
        } else {
            toStringBuilder.append(clazz.getCanonicalName());
        }

        TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
        String typeParametersToString = typeParametersToString(typeParameters);
        toStringBuilder.append(typeParametersToString);

        if (isArray) {
            toStringBuilder.append("[]");
        }
        return toStringBuilder.toString();
    }

    private String typeParametersToString(TypeVariable<?>[] typeParameters) {
        StringBuilder toStringBuilder = new StringBuilder();
        if (typeParameters.length > 0) {
            toStringBuilder.append("<");

            String[] typeParametersAsStrings = transform(typeParameters,
                    TypeVariableToStringTransformer.INSTANCE);

            String typeParametersToString = String.join(",", typeParametersAsStrings);
            toStringBuilder.append(typeParametersToString);

            toStringBuilder.append(">");
        }
        return toStringBuilder.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private String[] transform(TypeVariable<?>[] typeVariables, Function<TypeVariable<?>, String> objectTransformer) {
        String[] toStringList = new String[typeVariables.length];
        for (int i = 0; i < typeVariables.length; i++) {
            TypeVariable<?> typeVariable = typeVariables[i];
            Object transformedObject = objectTransformer.apply(typeVariable);
            toStringList[i] = transformedObject.toString();
        }
        return toStringList;
    }

    private Type doGetBoundType(TypeVariable<?> typeVariable, Type... types) {
        Type boundType = null;
        for (Type type : types) {
            if (!(type instanceof Class<?>)) {
                continue;
            }

            Class<?> typeClass = Class.class.cast(type);
            boundType = findBoundTypeInGenericSuperclasses(typeClass,
                    typeVariable);
            if (boundType != null) {
                break;
            }

            boundType = findBoundTypeInGenericInterfaces(typeClass,
                    typeVariable);
            if (boundType != null) {
                break;
            }

            boundType = findBoundTypeInSuperclasses(typeClass, typeVariable);
            if (boundType != null) {
                break;
            }

            boundType = findBoundTypeInInterfaces(typeClass, typeVariable);
            if (boundType != null) {
                break;
            }
        }
        return boundType;
    }

    private Type findBoundTypeInGenericSuperclasses(Class<?> typeClass,
                                                    TypeVariable<?> typeVariable) {
        Type boundType = null;

        Type genericSuperclass = typeClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(genericSuperclass);
            boundType = doGetBoundType(parameterizedType, typeVariable);
        }

        return boundType;
    }

    private Type findBoundTypeInGenericInterfaces(Class<?> typeClass,
                                                  TypeVariable<?> typeVariable) {
        Type boundType = null;

        Type[] genericInterfaces = typeClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                boundType = doGetBoundType(parameterizedType, typeVariable);
                if (boundType != null) {
                    break;
                }
            }
        }

        return boundType;
    }

    private Type findBoundTypeInSuperclasses(Class<?> typeClass,
                                             TypeVariable<?> typeVariable) {
        Class<?> superclass = typeClass.getSuperclass();
        Type boundType = doGetBoundType(typeVariable, superclass);
        return boundType;
    }

    private Type findBoundTypeInInterfaces(Class<?> typeClass,
                                           TypeVariable<?> typeVariable) {
        Class<?>[] interfaces = typeClass.getInterfaces();
        Type boundType = doGetBoundType(typeVariable, interfaces);
        return boundType;
    }

    private Type doGetBoundType(ParameterizedType parameterizedType,
                                TypeVariable<?> typeVariable) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class<?>) {
            Class<?> rawTypeClass = (Class<?>) rawType;
            TypeVariable<?>[] typeParameters = rawTypeClass.getTypeParameters();
            for (int i = 0; i < typeParameters.length; i++) {
                TypeVariable<?> typeParameter = typeParameters[i];
                if (typeParameter.equals(typeVariable)) {
                    return actualTypeArguments[i];
                }
            }
        }
        return null;
    }

    /**
     * Nullsafe {@link ClassLoader} resolution. If this {@link Class2}
     * represents a system class the {@link ClassLoader#getSystemClassLoader()}
     * will be returned.
     *
     * @return the class loader of this {@link Class2}.
     * @since 1.2.0;
     */
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = getType().getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + clazz.hashCode();
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Class2 other = (Class2) obj;
        return clazz.equals(other.clazz);
    }

}
