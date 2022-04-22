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

import com.link_intersystems.lang.Signature;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * An {@link AdaptableProxy} is used to create adapters that route method calls
 * on the adapted interfaces to equivalent methods on the adapted object.
 * Equivalent methods are determined by their {@link Method2#getSignature()}
 * their return type and modifiers.
 *
 * <p>
 * Imaging you have to deal with two kind of objects like:
 *
 * <pre>
 * public interface InterfaceA {
 * 	public String greeting(String name);
 * }
 *
 * public interface InterfaceB {
 * 	public String greeting(String name);
 * }
 *
 * </pre>
 * <p>
 * Both interfaces define exactly the same methods, but because they are of a
 * different type can not handle them equal. You (the client) have to know which
 * concrete type you deals with. So normally such a condition ends up in
 * instanceof code like:
 *
 * <pre>
 * ...
 * 	public void handle(Object o, String arg){
 * 		if(o instanceof InterfaceA){
 * 			InterfaceA a = (InterfaceA) o;
 * 			a.greeting(arg);
 *        } else if(o instanceof InterfaceB){
 * 			InterfaceB b = (InterfaceB) o;
 * 			b.greeting(arg);
 *        } else {
 * 			throw new IllegalStateException();
 *        }
 *    }
 *
 * ...
 * </pre>
 * <p>
 * You can omit this instanceof code either by
 *
 * <ol>
 * <li>adapt InterfaceB to InterfaceA and only make calls on InterfaceA.</li>
 * <li>create an own interface InterfaceC and create adapters from InterfaceC to
 * InterfaceA and InterfaceB.</li>
 * </ol>
 * For both solutions you have to write code. But the {@link AdaptableProxy}
 * makes it easy for you to create such an adapter and write less code. <br/>
 * Example for case 1:
 *
 * <pre>
 * ...
 *  ClassLoader classLoader = ...;
 *  InterfaceB b = ...;
 * 	InvocationHandler h = new AdaptableInvocationHanlder(b);
 * 	InterfaceA a = (InterfaceA) Proxy.newProxyInstance(classLoader,
 * 				new Class[] { InterfaceA.class }, h);
 *  a.greeting(""); // will call greeting on b
 *
 * ...
 * </pre>
 * <p>
 * Example for case 2:
 *
 * <pre>
 * public interface InterfaceC {
 * 	public String greeting(String arg);
 * }
 * ...
 *  ClassLoader classLoader = ...;
 *  InterfaceA a = ...;
 * 	InvocationHandler h = new AdaptableInvocationHanlder(a);
 * 	InterfaceC c = (InterfaceC) Proxy.newProxyInstance(classLoader,
 * 				new Class[] { InterfaceA.class }, h);
 *  c.greeting(""); // will call greeting on a
 *
 *  InterfaceB b = ...;
 * 	InvocationHandler h = new AdaptableInvocationHanlder(b);
 * 	InterfaceC c = (InterfaceC) Proxy.newProxyInstance(classLoader,
 * 				new Class[] { InterfaceA.class }, h);
 *  c.greeting(""); // will call greeting on b
 *
 * ...
 * </pre>
 *
 * <strong>note:</strong> </br> If you call a method on the adapter that can not
 * be routed to the adapted object an {@link UnsupportedOperationException}s
 * will be thrown. An {@link IllegalStateException} will be thrown if the return
 * types do not match.
 * </p>
 *
 * @author René Link [<a
 * href="mailto:rene.link@link-intersystems.com">rene.link@link-
 * intersystems.com</a>]
 * @since 1.0.0;
 */
public class AdaptableProxy implements InvocationHandler {

    private final Object adaptable;

    private final Class2<?> adaptableClass;

    /**
     * Creates an adapter that implements the <code>adapterClasses</code> and
     * delegates invocations to those interface methods to the
     * <code>adaptables<code> method with the same signature.
     *
     * @param adaptable      the adaptable that receives the adapter invocations.
     * @param adapterClasses the classes that the adapter should represent. All classes
     *                       must belong to the same {@link ClassLoader}.
     * @return an adapter that implements the <code>adapterClasses</code> and
     * delegates invocations to those interface methods to the
     * <code>adaptables<code> method with the same signature.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Object adaptable, Class<?>... adapterClasses) {
        requireNonNull(adaptable);
        requireNonNull(adapterClasses);

        ClassLoader cl = ClassLoader.getSystemClassLoader();

        for (Class<?> adapterClass : adapterClasses) {
            ClassLoader classLoader = adapterClass.getClassLoader();
            if (classLoader == null) {
                /*
                 * java runtime class loader is always in the hierarchy of the
                 * application class loader (system class loader).
                 */
                continue;
            }

            boolean classLoaderIsParentOfCL = inHierarchy(classLoader, cl);
            if (!classLoaderIsParentOfCL) {
                boolean classLoaderIsChildOfCL = inHierarchy(cl, classLoader);
                if (classLoaderIsChildOfCL) {
                    cl = classLoader;
                } else {
                    throw new IllegalArgumentException(
                            "All adapterClasses must belong to the same class loader hierarchy");
                }

            }
        }
        AdaptableProxy adaptableInvocationHandler = new AdaptableProxy(
                adaptable);

        T adapter = (T) Proxy.newProxyInstance(cl, adapterClasses,
                adaptableInvocationHandler);
        return adapter;
    }

    private static boolean inHierarchy(ClassLoader cl, ClassLoader inHierarchyCL) {
        boolean inHierarchy = false;

        ClassLoader classLoaderToFind = inHierarchyCL;

        while (classLoaderToFind != null) {
            if (classLoaderToFind.equals(cl)) {
                inHierarchy = true;
                break;
            }
            classLoaderToFind = classLoaderToFind.getParent();
        }

        return inHierarchy;
    }

    /**
     * Creates an {@link AdaptableProxy} for the given object.
     *
     * @param adaptable
     * @since 1.0.0;
     */
    public AdaptableProxy(Object adaptable) {
        this.adaptable = Objects.requireNonNull(adaptable);
        adaptableClass = Class2.get(adaptable.getClass());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0;
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Invokable invokableAdapter = getInvokableAdapter(method);
        return invokableAdapter.invoke(args);
    }

    private Invokable getInvokableAdapter(Method method) {
        Method2 adaptableMethod2 = getAdaptableMethod(method);
        checkReturnTypeCompatibility(adaptableMethod2, method);
        Invokable invokable = adaptableMethod2.getInvokable(adaptable);
        return invokable;
    }

    private void checkReturnTypeCompatibility(Method2 adaptableMethod,
                                              Method original) {
        Class<?> originalReturnType = original.getReturnType();
        Class<?> adaptableReturnType = adaptableMethod.getReturnType();
        if (!originalReturnType.equals(adaptableReturnType)) {
            throw new IllegalStateException("Can not adapt method " + original
                    + ", because the adaptable's corresponding method's '"
                    + adaptableMethod.getMember()
                    + "' return type doesn't match.");
        }
    }

    private Method2 getAdaptableMethod(Method method) {
        Method2 method2 = Method2.forMethod(method);
        Signature invokedSignature = method2.getSignature();
        Method2 adaptableMethod2 = adaptableClass.getMethod2(invokedSignature);
        if (adaptableMethod2 == null) {
            throw new UnsupportedOperationException("Can not adapt method "
                    + method
                    + ", because no corresponding method found on adaptable "
                    + adaptableClass);
        }
        return adaptableMethod2;
    }

}
