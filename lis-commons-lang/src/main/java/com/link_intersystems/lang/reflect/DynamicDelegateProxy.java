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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A {@link DynamicDelegateProxy} is a {@link Proxy} that delegates calls to
 * it's interface to a selected target object with the same interface. Target
 * objects are obtained using a {@link Reference} and therefore can change at
 * runtime. A {@link DynamicDelegateProxy} is useful when the target object must
 * be determined dynamically or rather changes at runtime.
 * <p>
 * <strong>warning:</strong>
 * <p>
 * It's your responsibility to ensure that the {@link DynamicDelegateProxy}'s
 * interface behaves in a consistent way. In some cases it might be hard to
 * ensure consistency between calls, e.g. you should not replace a {@link List}
 * proxy's target after a client called {@link List#size()} because the new
 * target's size might be different and {@link IndexOutOfBoundsException}s might
 * be the result.
 * </p>
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public class DynamicDelegateProxy<T> implements InvocationHandler {

    /**
     * Creates an {@link DynamicDelegateProxy} that delegates calls to it's
     * interface to a target object with the same interface.
     *
     * @param delegateClass proxy interface.
     * @param targetRef     the targetRef that is used to obtain a target object when
     *                      delegating a method invocation. The {@link Reference} must not
     *                      return <code>null</code>.
     * @return an {@link DynamicDelegateProxy} that delegates calls to it's
     * interface to a target object with the same interface.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> delegateClass,
                               Reference<? extends T> targetRef) {
        requireNonNull(delegateClass);
        requireNonNull(targetRef);

        Class<?>[] allInterfacesAsArray = new Class<?>[]{delegateClass};

        ClassLoader classLoader = delegateClass.getClassLoader();
        DynamicDelegateProxy<T> delegateProxy = new DynamicDelegateProxy<>(
                targetRef);

        T delegate = (T) Proxy.newProxyInstance(classLoader,
                allInterfacesAsArray, delegateProxy);
        return delegate;
    }

    private Reference<? extends T> targetRef;

    protected DynamicDelegateProxy(Reference<? extends T> targetRef) {
        requireNonNull(targetRef);
        this.targetRef = targetRef;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        T target = targetRef.get();
        Object result = method.invoke(target, args);
        return result;
    }

}
