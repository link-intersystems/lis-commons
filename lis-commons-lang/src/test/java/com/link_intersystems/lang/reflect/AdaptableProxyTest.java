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

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URLClassLoader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdaptableProxyTest  {

    @Test
    void create() {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };
        InterfaceA adapter = AdaptableProxy.create(interfaceB, InterfaceA.class);

        String concat = adapter.concat("Hello");
        assertEquals("HelloHello", concat);
    }

    @Test
    void createMultipleInterfacesAdapter() {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };
        Object adapter = AdaptableProxy.create(interfaceB, InterfaceA.class, InterfaceE.class);
        InterfaceE interfaceE = (InterfaceE) adapter;
        String concat = interfaceE.concat("Hello");
        assertEquals("HelloHello", concat);

        InterfaceA interfaceA = (InterfaceA) adapter;
        concat = interfaceA.concat("Hello");
        assertEquals("HelloHello", concat);
    }

    @Test
    void createMultipleInterfacesSameClassLoaderHierarchy() throws ClassNotFoundException {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };

        AdaptableProxy.create(interfaceB, InterfaceA.class, CharSequence.class, InterfaceE.class);
    }

    @Test
    void createMultipleInterfacesNotTheSameClassLoaderHierarchy() throws ClassNotFoundException {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };

        URLClassLoader classLoader = (URLClassLoader) InterfaceE.class.getClassLoader();
        URLClassLoader otherURLClassLoader = new URLClassLoader(classLoader.getURLs(), null);
        Class<?> loadClass = otherURLClassLoader.loadClass("com.link_intersystems.lang.reflect.AdaptableProxyTest$InterfaceE");

        assertThrows(IllegalArgumentException.class, () -> AdaptableProxy.create(interfaceB, InterfaceA.class, loadClass));
    }

    @Test
    void createMultipleInterfacesSameClassLoaderHierarchy2() throws ClassNotFoundException {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };

        URLClassLoader classLoader = (URLClassLoader) InterfaceE.class.getClassLoader();

        Class<?> loadClass = classLoader.loadClass("com.link_intersystems.lang.reflect.AdaptableProxyTest$InterfaceE");

        AdaptableProxy.create(interfaceB, List.class, loadClass);
    }

    @Test
    void compatibleMethods() {
        InterfaceB interfaceB = new AdaptableProxyTest.InterfaceB() {

            public String concat(String string) {
                return string += string;
            }
        };
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        InvocationHandler h = new AdaptableProxy(interfaceB);
        InterfaceA a = (InterfaceA) Proxy.newProxyInstance(contextClassLoader, new Class[]{InterfaceA.class}, h);

        String concat = a.concat("Hello");
        assertEquals("HelloHello", concat);
    }

    @Test
    void incombatibleMethods() {
        InterfaceD adaptable = new AdaptableProxyTest.InterfaceD() {

            public String concat() {
                return "";
            }
        };
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        InvocationHandler h = new AdaptableProxy(adaptable);
        InterfaceA a = (InterfaceA) Proxy.newProxyInstance(contextClassLoader, new Class[]{InterfaceA.class}, h);

        assertThrows(UnsupportedOperationException.class, () -> a.concat("Hello"));
    }

    @Test
    void incombatibleReturnTypes() {
        InterfaceC adaptable = new AdaptableProxyTest.InterfaceC() {

            public StringBuilder concat(String string) {
                return new StringBuilder(string);
            }
        };
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        InvocationHandler h = new AdaptableProxy(adaptable);
        InterfaceA a = (InterfaceA) Proxy.newProxyInstance(contextClassLoader, new Class[]{InterfaceA.class}, h);

        assertThrows(IllegalStateException.class, () -> a.concat("Hello"));
    }

    @Test
    void nullAdaptable() {
        assertThrows(IllegalArgumentException.class, () -> new AdaptableProxy(null));
    }

    public static interface InterfaceA {

        public String concat(String string);
    }

    public static interface InterfaceB {

        public String concat(String string);
    }

    public static interface InterfaceC {

        public StringBuilder concat(String string);
    }

    public static interface InterfaceD {

        public String concat();
    }

    public static interface InterfaceE {

        public String concat(String string);
    }

}
