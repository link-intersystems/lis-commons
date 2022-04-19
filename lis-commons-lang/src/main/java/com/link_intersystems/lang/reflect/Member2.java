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

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.Conversions;
import com.link_intersystems.lang.PrimitiveArrayCallback;
import com.link_intersystems.lang.Signature;
import com.link_intersystems.lang.ref.SerializableObjectReference;
import com.link_intersystems.lang.ref.SerializableReference;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Member2<M extends Member> implements Member,
        Serializable {

    private static final long serialVersionUID = 1L;

    private List<Parameter> parameters;

    private Boolean generic;

    private final SerializableReference<M> memberRef;

    private Member2Applicability member2Applicability;

    protected Member2(M member) {
        Assert.notNull("member", member);
        this.memberRef = getSerializableReference(member);
        member2Applicability = new Member2Applicability(this);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Member> SerializableReference<T> getSerializableReference(
            T member) {
        if (member instanceof Method) {
            return (SerializableReference<T>) new SerializableMethod(
                    (Method) member);
        } else if (member instanceof Field) {
            return (SerializableReference<T>) new SerializableField(
                    (Field) member);
        } else if (member instanceof Constructor<?>) {
            return (SerializableReference<T>) new SerializableConstructor(
                    (Constructor<?>) member);
        } else if (member instanceof Member2<?>) {
            Member2<T> member2 = (Member2<T>) member;
            return new SerializableObjectReference<T>(member2);
        } else {
            throw new IllegalArgumentException("member must be either a "
                    + Constructor.class.getCanonicalName() + ", a "
                    + Method.class.getCanonicalName() + " or a "
                    + Field.class.getCanonicalName());
        }
    }

    public abstract Signature getSignature();

    /**
     * Returns the member that this {@link Member2} wraps.
     *
     * @return the member that this {@link Member2} wraps.
     * @since 1.0.0;
     */
    public M getMember() {
        return memberRef.get();
    }

    /**
     * The class that declares this {@link Member2}.
     *
     * @return the declaring class of this {@link Member2}.
     * @since 1.0.0;
     */
    public Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    /**
     * The generic type parameters of this {@link Invokable}.
     *
     * @return the generic parameter types of this {@link Invokable}.
     * @since 1.0.0;
     */
    public abstract Type[] getGenericParameterTypes();

    /**
     * Returns true if this {@link Member2} has variable arguments.
     *
     * @return true if this {@link Member2} has variable arguments.
     * @since 1.0.0;
     */
    public abstract boolean isVarArgs();

    /**
     * The type that invocations of this {@link Member2} will return.
     *
     * @return the return type of this {@link Member2}.
     * @since 1.0.0;
     */
    public Class2<?> getReturnClass2() {
        Class<?> returnType = getReturnType();
        return Class2.get(returnType);
    }

    /**
     * @return the {@link Class} object that represents this member's return
     *         type.
     * @since 1.0.0;
     */
    protected abstract Class<?> getReturnType();

    public boolean isSynthetic() {
        return getMember().isSynthetic();
    }

    /**
     * The modifiers of this {@link Member2}.
     *
     * @return the modifiers of this {@link Member2}.
     * @since 1.0.0;
     */
    public int getModifiers() {
        return getMember().getModifiers();
    }

    /**
     * The name of this {@link Member2}.
     *
     * @return the name of this {@link Member2}.
     * @since 1.0.0;
     */
    public String getName() {
        return getMember().getName();
    }

    /**
     * The parameter type array of this {@link Member2}.
     *
     * @return the parameter types of this {@link Member2}.
     * @since 1.0.0;
     */
    public abstract Class<?>[] getParameterTypes();

    /**
     * @param accessTypes
     * @return true if this {@link Member2} is accessible by one of the given
     *         {@link AccessType}s.
     * @since 1.0.0;
     */
    public boolean isAccessible(AccessType... accessTypes) {
        Assert.notNull("accessTypes", accessTypes);
        int modifiers = getModifiers();
        boolean accessible = false;
        for (int i = 0; i < accessTypes.length; i++) {
            if (accessTypes[i].isMatching(modifiers)) {
                accessible = true;
                break;
            }
        }
        return accessible;
    }

    protected boolean isDeclaringClassIsAnInterface() {
        return getDeclaringClass().isInterface();
    }

    protected boolean isDeclaringClassEqual(Member2<?> otherMember2) {
        return getDeclaringClass().equals(otherMember2.getDeclaringClass());
    }

    /**
     * @param otherMember2
     * @return true if the names of this {@link Member2} is equal to the name of
     *         the other {@link Member2}.
     * @since 1.0.0;
     */
    protected boolean isNameEqual(Member2<?> otherMember2) {
        return getName().equals(otherMember2.getName());
    }

    /**
     * We have to make sure we don't mistake standard interface implementation
     * (where first method is on an interface and the params are equal) for
     * overloading.
     *
     * @param member2
     * @return
     * @since 1.0.0;
     */
    protected boolean isInterfaceImplementation(Member2<?> member2) {
        return member2.isDeclaringClassIsAnInterface()
                && areParametersEqual(member2);
    }

    /**
     * @param referenceMember
     * @return true if the access modifier of this {@link Member2} are less
     *         restrictive than the access modifier of the referenceInvokable.
     *         <p>
     *         Logic according to the java language specification:
     *
     *         <pre>
     * The access modifier (§6.6) of an overriding or hiding method must provide at least as much access as the overridden or hidden method, or a compile-time error occurs. In more detail:
     *         <ul>
     *         <li>If the overridden or hidden method is <code>public</code>,
     *         then the overriding or hiding method must be <code>public</code>;
     *         otherwise, a compile-time error occurs. <a name="39550"></a></li>
     *         <li>If the overridden or hidden method is <code>protected</code>,
     *         then the overriding or hiding method must be
     *         <code>protected</code> or <code>public</code>; otherwise, a
     *         compile-time error occurs. <a name="39551"></a></li>
     *         <li>If the overridden or hidden method has default (package)
     *         access, then the overriding or hiding method must not be
     *         <code>private</code>; otherwise, a compile-time error occurs.
     *
     *         </li>
     *         </ul>
     * </pre>
     *
     *         <pre>
     *                                 +---------------------------------------------------------------------+
     *                                 |                               this                                  |
     *                                 +----------------+----------------+-------------------+---------------+
     *                                 |     public     |    protected   | package (default) |    private    |
     * +-----------+-------------------+----------------+----------------+-------------------+---------------+
     * |           | public            |      true      |     false      |       false       |     false     |
     * |           +-------------------+----------------+----------------+-------------------+---------------+
     * | reference | protected         |      true      |     true       |       false       |     false     |
     * |           +-------------------+----------------+----------------+-------------------+---------------+
     * | invokable | package (default) |      true      |     true       |       true        |     false     |
     * |           +-------------------+----------------+----------------+-------------------+---------------+
     * |           | private           |      false     |     false      |       false       |     false     |
     * +-----------+-------------------+----------------+----------------+-------------------+---------------+
     * </pre>
     *
     *         </p>
     * @since 1.0.0;
     */
    protected boolean isAccessModifierOverriddingCompatible(
            Member2<?> referenceMember) {
        int referenceModifiers = referenceMember.getModifiers();
        int invokableModifiers = getModifiers();
        if (Modifier.isPrivate(referenceModifiers)
                || Modifier.isPrivate(invokableModifiers)) {
            return false;
        }
        if (Modifier.isPublic(referenceModifiers)) {
            return Modifier.isPublic(invokableModifiers);
        }
        if (Modifier.isProtected(referenceModifiers)) {
            return Modifier.isProtected(invokableModifiers)
                    || Modifier.isPublic(invokableModifiers);
        }
        return true;
    }

    /**
     * @param member2
     * @return true if the this {@link Member2}'s declaring class is assignable
     *         from the invokable.
     * @since 1.0.0;
     */
    protected boolean isDeclaringClassAssignableFrom(Member2<?> member2) {
        return member2.getDeclaringClass()
                .isAssignableFrom(getDeclaringClass());
    }

    /**
     * Check deep equality on parameters of this {@link Member2} with another
     * {@link Member2}.
     *
     * @param compareWithMember2
     * @return
     * @since 1.0.0;
     */
    protected boolean areParametersEqual(Member2<?> compareWithMember2) {
        return Arrays.deepEquals(getParameterTypes(),
                compareWithMember2.getParameterTypes());
    }

    protected boolean isReturnTypeAssignableFrom(Member2<?> member2) {
        return member2.getReturnType().isAssignableFrom(getReturnType());
    }

    /**
     * Converts method call arguments to method parameter types.
     *
     * @param args
     * @return
     * @since 1.0.0;
     */
    protected Class<?>[] argObjectsToParamClasses(Object[] args) {
        Class<?>[] paramClasses = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            Object param = args[i];
            if (param == null) {
                Parameter parameterInfo = getParameter(i);
                Class2<?> parameterClass2 = parameterInfo.getParameterClass2();
                paramClasses[i] = parameterClass2.getType();
            } else {
                paramClasses[i] = param.getClass();
            }
        }
        return paramClasses;
    }

    /**
     * @param index
     * @return
     * @since 1.0.0;
     */
    protected Parameter getParameter(int index) {
        List<Parameter> parametersInternal = getParameters();
        if (index < parametersInternal.size()) {
            return parametersInternal.get(index);
        } else if (isVarArgs() && parametersInternal.size() > 0) {
            Parameter parameterInfoImpl = parametersInternal
                    .get(parametersInternal.size() - 1);
            return parameterInfoImpl;
        } else {
            throw new IndexOutOfBoundsException(
                    "unable to get parameter info for index " + index
                            + ". Paramter length is "
                            + parametersInternal.size());
        }
    }

    /**
     * Returns true if this member can be called with the given arguments.
     *
     * @param arguments
     * @return true if this {@link Invokable} is applicable for the arguments.
     * @since 1.0.0;
     */
    public boolean isApplicable(Object[] arguments) {
        Class<?>[] paramClasses = argObjectsToParamClasses(arguments);
        return isApplicable(paramClasses);
    }

    /**
     * Returns true if this member is applicable for the given parameter types.
     * types?
     *
     * @param paramTypes
     * @return true if this {@link Invokable} is applicable for the parameter
     *         types.
     */
    public boolean isApplicable(Class<?>[] paramTypes) {
        boolean applicable = member2Applicability.isApplicable(paramTypes);
        return applicable;
    }

    /**
     * Converts an array of objects used as parameters to the 'real' invocation
     * parameters by resolving null to default values if necessary (e.g.
     * param[3] == null && parameterTypes[3] == int.class -> 0).
     *
     * @param callParameters
     * @return
     * @since 1.0.0;
     */
    protected Object[] toMethodInvokeParams(Object[] callParameters) {
        if (isVarArgs()) {
            List<Parameter> parameters = getParameters();

            if (parameters.isEmpty()) {
                return new Object[0];
            }

            Object[] invokeParams = new Object[parameters.size()];

            copyNonVarargs(parameters, invokeParams, callParameters);

            boolean atLeastOneVarargCallParameter = callParameters.length >= parameters
                    .size();
            if (atLeastOneVarargCallParameter) {
                int varargsParamIndex = parameters.size() - 1;
                Parameter varargParameter = parameters.get(varargsParamIndex);
                Object varargsArray = createVarargsArray(varargParameter,
                        varargsParamIndex, callParameters);
                invokeParams[varargsParamIndex] = varargsArray;
            }
            return invokeParams;
        } else {
            return callParameters;
        }
    }

    private void copyNonVarargs(List<Parameter> parameters,
                                Object[] invokeParams, Object[] callParameters) {
        System.arraycopy(callParameters, 0, invokeParams, 0,
                parameters.size() - 1);
    }

    private Object createVarargsArray(Parameter varargsParameter,
                                      int varargsParamIndex, Object[] callParameters) {
        Class2<?> parameterClass2 = varargsParameter.getParameterClass2();
        Class<?> type = parameterClass2.getType();
        Class<?> componentType = type.getComponentType();
        Object varargsArray = Array.newInstance(type.getComponentType(),
                callParameters.length - varargsParamIndex);
        if (componentType.isPrimitive()) {
            PrimitiveArrayCallback primitiveCallback = new PrimitiveArrayCallback(
                    varargsArray);
            for (int varargsIndex = varargsParamIndex; varargsIndex < callParameters.length; varargsIndex++) {
                Object object = callParameters[varargsIndex];
                Conversions.unbox(object, primitiveCallback);
            }
        } else {
            System.arraycopy(callParameters, varargsParamIndex, varargsArray,
                    0, Array.getLength(varargsArray));
        }
        return varargsArray;
    }

    /**
     * Is this {@link Member2} a generic {@link Member2}?
     *
     * @return true if this Member2 is a generic member in terms of the java
     *         language specification. <h3>For {@link Member2}s that represent
     *         {@link Constructor2}s</h3>
     *
     *         <pre>
     * <h2>8.8.4 Generic Constructors</h2>
     * It is possible for a constructor to be declared generic, independently of whether the class the
     * constructor is declared in is itself generic. A constructor is generic if it declares one or more
     * type variables (§4.4). These type variables are known as the formal type parameters of the constructor.
     * The form of the formal type parameter list is identical to a type parameter list of a generic class or
     * interface, as described in §8.1.2.
     *
     * The scope of a constructor's type parameter is the entire declaration of the constructor, including the
     * type parameter section itself. Therefore, type parameters can appear as parts of their own bounds, or
     * as bounds of other type parameters declared in the same section.
     *
     * Type parameters of generic constructor need not be provided explicitly when a generic constructor is
     * invoked. When they are not provided, they are inferred as specified in §15.12.2.7.
     * </pre>
     *
     *         <h3>For {@link Member2}s that represent {@link Method2}s</h3>
     *
     *         <pre>
     * <h2>8.4.4 Generic Methods</h2>
     * A method is generic if it declares one or more type variables (§4.4). These type variables are
     * known as the formal type parameters of the method. The form of the formal type parameter list
     * is identical to a type parameter list of a class or interface, as described in §8.1.2.
     *
     * The scope of a method's type parameter is the entire declaration of the method, including the
     * type parameter section itself. Therefore, type parameters can appear as parts of their own bounds,
     * or as bounds of other type parameters declared in the same section.
     *
     * Type parameters of generic methods need not be provided explicitly when a generic method is
     * invoked. Instead, they are almost always inferred as specified in §15.12.2.7
     * </pre>
     * @since 1.0.0;
     */
    public boolean isGeneric() {
        if (generic == null) {
            Boolean generic = Boolean.FALSE;
            Type[] genericParameterTypes = getGenericParameterTypes();

            for (int i = 0; i < genericParameterTypes.length; i++) {
                if (genericParameterTypes[i] instanceof ParameterizedType) {
                    generic = Boolean.TRUE;
                }
            }

            this.generic = generic;
        }
        return generic.booleanValue();
    }

    /**
     * Returns the {@link Parameter}s of this {@link Invokable}.
     *
     * @return the {@link Parameter}s of this {@link Invokable}.
     * @since 1.2.0;
     */
    public List<Parameter> getParameters() {
        if (parameters == null) {
            List<Parameter> parameters = new ArrayList<Parameter>();

            Class<?>[] parameterTypes = getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters.add(new Parameter(this, i));
            }

            this.parameters = Collections.unmodifiableList(parameters);
        }
        return parameters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((memberRef == null) ? 0 : memberRef.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        Member2<?> other = Member2.class.cast(obj);

        if (memberRef == null && other.memberRef != null) {
            return false;
        }
        return memberRef.equals(other.memberRef);
    }

    /**
     * @return the declared exception types of this method.
     * @since 1.2.0;
     */
    public abstract Class<?>[] getDeclaredExceptionTypes();

    /**
     * @param exceptionClass
     * @return true if the exceptionClass is a exception type declared by this
     *         method.
     * @since 1.2.0;
     */
    public boolean isDeclaredException(Class<? extends Exception> exceptionClass) {
        Assert.notNull("exceptionClass", exceptionClass);
        boolean isDeclaredException = false;
        Class<?>[] declaredExceptionTypes = getDeclaredExceptionTypes();
        for (int i = 0; i < declaredExceptionTypes.length; i++) {
            Class<?> declaredExceptionType = declaredExceptionTypes[i];
            isDeclaredException = declaredExceptionType
                    .isAssignableFrom(exceptionClass);
            if (isDeclaredException) {
                break;
            }
        }
        return isDeclaredException;
    }

    /**
     * @param exception
     * @return true if the exception is declared by this method.
     * @since 1.2.0;
     */
    public boolean isDeclaredException(Exception exception) {
        Assert.notNull("exception", exception);
        Class<? extends Exception> exceptionClass = exception.getClass();
        return isDeclaredException(exceptionClass);
    }

}
