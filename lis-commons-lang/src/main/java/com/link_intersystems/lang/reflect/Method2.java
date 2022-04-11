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
package com.link_intersystems.lang.reflect;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.function.Predicate;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.Signature;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import com.link_intersystems.lang.reflect.criteria.MemberCriteria;
import com.link_intersystems.lang.reflect.criteria.Result;

/**
 * Extends a {@link Method} by wrapping it and provides sophisticated methods
 * for common {@link Method} object based.
 * <p>
 * Features are:
 * <ul>
 * <li>Find out if a method overrides another method.</li>
 * <li>Find out if a method overloads another method.</li>
 * <li>Abstracting a method ({@link Invokable}) so that constructors and methods
 * can be treated in the same way.</li>
 * </ul>
 * </p>
 *
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class Method2 extends Member2<Method> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3824221999295830037L;

	/**
	 *
	 * @param method
	 * @return a {@link Method2} object for the given {@link Method}.
	 * @since 1.0.0.0
	 */
	public static Method2 forMethod(Method method) {
		Assert.notNull("method", method);
		Class<?> declaringClass = method.getDeclaringClass();
		Class2<?> declaringClass2 = Class2.get(declaringClass);
		Method2 method2;
		try {
			method2 = declaringClass2.getMethod2(method);
			return method2;
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Implementation error.", e);
		}
	}

	private transient Signature signature;

	Method2(Method method) {
		super(method);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			Method2 otherMethod2 = (Method2) obj;
			Method member = getMember();
			return member.equals(otherMethod2.getMember());
		}
	}

	/**
	 * @return the definition method of this {@link Method2}. The definition is
	 *         the first occurrence of the method signature within the class
	 *         hierarchy. Interfaces method definitions take precedence over
	 *         class method definitions.
	 * @since 1.0.0.0
	 */
	public Method getDefinition() {
		Class<?> declaringClass = getDeclaringClass();
		ClassCriteria classCriteria = new ClassCriteria();
		classCriteria.setSelection(ClassType.INTERFACES, ClassType.CLASSES);
		classCriteria.setTraverseClassesUniquely(true);
		classCriteria.setSeparatedClassTypeTraversal(true);
		Iterable<Class<?>> classIterable = classCriteria
				.getIterable(declaringClass);

		MemberCriteria methodCriteria = MemberCriteria
				.forMemberTypes(Method.class);
		methodCriteria.named(getName());
		Predicate<Object> predicate = new SignaturePredicate(this);
		methodCriteria.add(predicate);
		methodCriteria.setResult(Result.FIRST);

		Iterator<Member> iterator = methodCriteria.getIterable(classIterable)
				.iterator();

		Method declaringMethod = (Method) iterator.next();
		return declaringMethod;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	public Type[] getGenericParameterTypes() {
		return getMember().getGenericParameterTypes();
	}

	/**
	 *
	 * @param target
	 * @return
	 * @since 1.2.0.0
	 */
	public Invokable getInvokable(Object target) {
		return new Method2Invokable(target, this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	public Class<?>[] getParameterTypes() {
		return getMember().getParameterTypes();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	protected Class<?> getReturnType() {
		return getMember().getReturnType();
	}

	/**
	 * @return a {@link Signature} that represents this method. Clients might
	 *         use the {@link Signature} to compare it to other signatures. The
	 *         returned {@link Signature}'s equality attributes are
	 *         <ul>
	 *         <li>name</li>
	 *         <li>parameter types</li>
	 *         </ul>
	 *         Two {@link Signature}s are equal if these attributes are equal.
	 *         Exactly as defined by the java language specificatio, eexcept the
	 *         generic part which is not implemented ye.:
	 *
	 *         <pre>
	 *         <h2>8.4.2 Method Signature</h2>
	 * It is a compile-time error to declare two methods with override-equivalent signatures (defined below) in a class.
	 * Two methods have the same signature if they have the same name and argument types.
	 *
	 * Two method or constructor declarations M and N have the same argument types if all of the following conditions hold:
	 *
	 * They have the same number of formal parameters (possibly zero)
	 * They have the same number of type parameters (possibly zero)
	 *
	 * Let <A1,...,An> be the formal type parameters of M and let <B1,...,Bn> be the formal type parameters of N. After renaming
	 * each occurrence of a Bi in N's type to Ai the bounds of corresponding type variables and the argument types of M and N
	 * are the same.
	 *
	 * The signature of a method m1 is a subsignature of the signature of a method m2 if either
	 *
	 * m2 has the same signature as m1, or
	 * the signature of m1 is the same as the erasure of the signature of m2.
	 * </pre>
	 * @since 1.0.0.0
	 */
	public Signature getSignature() {
		if (signature == null) {
			signature = new Method2Signature(this);
		}
		return signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getMember().hashCode();
		return result;
	}

	public boolean isOverriddenBy(Method method) {
		Method2 method2 = Method2.forMethod(method);
		return isOverriddenBy(method2);
	}

	public boolean isOverriddenBy(Method2 method) {
		boolean overrides = method.overrides(getMember());
		return overrides;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.2.0.0
	 */
	@Override
	public Class<?>[] getDeclaredExceptionTypes() {
		return getMember().getExceptionTypes();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	public boolean isVarArgs() {
		return getMember().isVarArgs();
	}

	/**
	 * @param method
	 * @return true, if the {@link Method} of this {@link Method2} overloads the
	 *         given {@link Method2}. Overloading is determined as specified by
	 *         the java specification section 8.4.9.
	 *
	 *         <pre>
	 * <h2>8.4.9 Overloading</h2>
	 * If two methods of a class (whether both declared in the same class, or both inherited by a class, or one declared and one
	 * inherited) have the same name but signatures that are not override-equivalent, then the method name is said to be
	 * overloaded. This fact causes no difficulty and never of itself results in a compile-time error. There is no required
	 * relationship between the return types or between the throws clauses of two methods with the same name, unless their
	 * signatures are override-equivalent.
	 *
	 * Methods are overridden on a signature-by-signature basis.
	 *
	 * If, for example, a class declares two public methods with the same name, and a subclass overrides one of them, the subclass
	 * still inherits the other method.
	 *
	 * When a method is invoked (§15.12), the number of actual arguments (and any explicit type arguments) and the compile-time
	 * types of the arguments are used, at compile time, to determine the signature of the method that will be invoked (§15.12.2).
	 * If the method that is to be invoked is an instance method, the actual method to be invoked will be determined at run time,
	 * using dynamic method lookup (§15.12.4).
	 * <hr/>
	 * <h2>8.4.10.2 Example: Overloading, Overriding, and Hiding</h2>
	 * In the example:
	 *
	 *     class Point {
	 *     	int x = 0, y = 0;
	 *     	void move(int dx, int dy) { x += dx; y += dy; }
	 *     	int color;
	 *     }
	 *     class RealPoint extends Point {
	 *     	float x = 0.0f, y = 0.0f;
	 *     	void move(int dx, int dy) { move((float)dx, (float)dy); }
	 *     	void move(float dx, float dy) { x += dx; y += dy; }
	 *     }
	 *
	 * the class RealPoint hides the declarations of the int instance variables x and y of class Point with its own float instance
	 * variables x and y, and overrides the method move of class Point with its own move method. It also overloads the name move
	 * with another method with a different signature (§8.4.2).
	 *
	 * In this example, the members of the class RealPoint include the instance variable color inherited from the class Point, the
	 * float instance variables x and y declared in RealPoint, and the two move methods declared in RealPoint.
	 *
	 * Which of these overloaded move methods of class RealPoint will be chosen for any particular method invocation will be
	 * determined at compile time by the overloading resolution procedure described in §15.12.
	 * </pre>
	 * @since 1.0.0.0
	 */
	public boolean overloads(Method2 method) {
		Method2 higher = method;
		return isNameEqual(higher) && isReturnTypeAssignableFrom(higher)
				&& !isInterfaceImplementation(higher)
				&& !method.overrides(this);
	}

	/**
	 *
	 * @param method
	 * @return true, if the {@link Method} of this {@link Method2} overrides the
	 *         given method. The override logic is based upon the java
	 *         specification section 8.4.2 Method signature.
	 *
	 *         <pre>
	 *         <h2>8.4.2 Method Signature</h2>
	 * It is a compile-time error to declare two methods with override-equivalent signatures (defined below) in a class.
	 *
	 * Two methods have the same signature if they have the same name and argument types.
	 *
	 * Two method or constructor declarations M and N have the same argument types if all of the following conditions hold:
	 *
	 * They have the same number of formal parameters (possibly zero)
	 * They have the same number of type parameters (possibly zero)
	 *
	 * Let <A1,...,An> be the formal type parameters of M and let <B1,...,Bn> be the formal type parameters of N. After renaming
	 * each occurrence of a Bi in N's type to Ai the bounds of corresponding type variables and the argument types of M and N
	 * are the same.
	 *
	 * The signature of a method m1 is a subsignature of the signature of a method m2 if either
	 *
	 * m2 has the same signature as m1, or
	 * the signature of m1 is the same as the erasure of the signature of m2.
	 *
	 * <hr/>
	 * <h3>Discussion</h3>
	 * The notion of subsignature defined here is designed to express a relationship between two methods whose signatures are
	 * not identical, but in which one may override the other.
	 *
	 * Specifically, it allows a method whose signature does not use generic types to override any generified version of that method.
	 * This is important so that library designers may freely generify methods independently of clients that define subclasses or
	 * subinterfaces of the library.
	 *
	 * Consider the example:
	 *
	 *    	class CollectionConverter {
	 *        	List toList(Collection c) {...}
	 *    	}
	 *    	class Overrider extends CollectionConverter{
	 * List toList(Collection c) {...}
	 *   	}
	 *
	 * Now, assume this code was written before the introduction of genericity, and now the author of class CollectionConverter decides
	 * to generify the code, thus:
	 *
	 *    	class CollectionConverter {
	 *    		<T> List<T> toList(Collection<T> c) {...}
	 *    	}
	 *
	 * Without special dispensation, Overrider.toList() would no longer override CollectionConverter.toList(). Instead, the code
	 * would be illegal. This would significantly inhibit the use of genericity, since library writers would hesitate to migrate existing code.
	 *
	 * Two method signatures m1 and m2 are override-equivalent iff either m1 is a subsignature of m2 or m2 is a subsignature of m1.
	 *
	 * The example:
	 *
	 *    	class Point implements Move {
	 *    		int x, y;
	 *    		abstract void move(int dx, int dy);
	 *    		void move(int dx, int dy) { x += dx; y += dy; }
	 *    	}
	 *
	 * causes a compile-time error because it declares two move methods with the same (and hence, override-equivalent) signature.
	 * This is an error even though one of the declarations is abstract.
	 * <hr/>
	 * <h2>8.4.10.1 Example: Overriding</h2>
	 * In the example:
	 *
	 *   	class Point {
	 *   		int x = 0, y = 0;
	 *   		void move(int dx, int dy) { x += dx; y += dy; }
	 *   	}
	 *   	class SlowPoint extends Point {
	 *   		int xLimit, yLimit;
	 *   		void move(int dx, int dy) {
	 *   			super.move(limit(dx, xLimit), limit(dy, yLimit));
	 *   		}
	 *   		static int limit(int d, int limit) {
	 *   			return d > limit ? limit : d < -limit ? -limit : d;
	 *   		}
	 *   	}
	 *
	 * the class SlowPoint overrides the declarations of method move of class Point with its own move method, which limits the
	 * distance that the point can move on each invocation of the method. When the move method is invoked for an instance of
	 * class SlowPoint, the overriding definition in class SlowPoint will always be called, even if the reference to the
	 * SlowPoint object is taken from a variable whose type is Point.
	 * </pre>
	 * @since 1.0.0.0
	 */
	public boolean overrides(Method method) {
		Method2 referenceMethod = new Method2(method);

		if (referenceMethod.isDeclaringClassIsAnInterface()) {
			return false;
		}

		if (!isNameEqual(referenceMethod)) {
			return false;
		}

		if (!isAccessModifierOverriddingCompatible(referenceMethod)) {
			return false;
		}

		if (!isDeclaringClassAssignableFrom(referenceMethod)) {
			return false;
		}

		if (!areParametersEqual(referenceMethod)) {
			return false;
		}

		if (!isReturnTypeAssignableFrom(referenceMethod)) {
			return false;
		}

		return true;
	}

	/**
	 * Same semantics as {@link #overrides(Method)}.
	 *
	 * @param methodInfo
	 * @return
	 */
	public boolean overrides(Method2 methodInvokable) {
		return overrides(methodInvokable.getMember());
	}

	@Override
	public String toString() {
		String methodAsString = "Method2: ";
		methodAsString += getMember().toGenericString();
		return methodAsString;
	}

}
