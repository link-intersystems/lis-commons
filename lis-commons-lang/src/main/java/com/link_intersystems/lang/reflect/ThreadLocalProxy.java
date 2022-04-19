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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ThreadLocalProxy implements InvocationHandler {

    private ThreadLocal<?> threadLocal;
    private Object nullInstance;

    @SuppressWarnings("unchecked")
    public static <T, TC> T createProxy(ThreadLocal<T> threadLocal, T nullInstance, Class<TC> targetClass) {
        Set<Class<?>> allInterfaces = getAllInterfaces(targetClass);

        if (targetClass.isInterface()) {
            allInterfaces.add(targetClass);
        }
        Class<?>[] interfaces = allInterfaces.toArray(new Class<?>[allInterfaces.size()]);
        T proxy = (T) Proxy.newProxyInstance(targetClass.getClassLoader(), interfaces, new ThreadLocalProxy(threadLocal, nullInstance));
        return proxy;
    }

    private static Set<Class<?>> getAllInterfaces(Class<?> type) {
        Set<Class<?>> allInterfaces = new LinkedHashSet<>();

        if (type.isInterface()) {
            allInterfaces.add(type);
        }

        Class<?>[] interfaces = type.getInterfaces();
        allInterfaces.addAll(stream(interfaces)
                .map(ThreadLocalProxy::getAllInterfaces)
                .flatMap(Set::stream).collect(toList()));

        return allInterfaces;
    }

    protected ThreadLocalProxy(ThreadLocal<?> threadLocal, Object nullInstance) {
        Assert.notNull("threadLocal", threadLocal);
        Assert.notNull("nullInstance", nullInstance);

        this.threadLocal = threadLocal;
        this.nullInstance = nullInstance;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = threadLocal.get();
        if (target == null) {
            target = nullInstance;
        }
        Object result = method.invoke(target, args);
        return result;
    }
}
