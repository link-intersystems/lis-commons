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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

class Constructor2Test {

    @SuppressWarnings("rawtypes")
    @Test
    void bestFitConstructor() throws SecurityException,
            NoSuchMethodException {
        Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
        Constructor2<TreeMap> constructorInfo = classInfoImpl
                .getApplicableConstructor(TreeMap.class);
        Assertions.assertNotNull(constructorInfo);
        Constructor<?> selectedConstructor = constructorInfo.getMember();
        Constructor<TreeMap> constructor = TreeMap.class
                .getConstructor(SortedMap.class);
        Assertions.assertEquals(constructor, selectedConstructor);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void bestFitConstructorByInvocationParamObjects()
            throws SecurityException, NoSuchMethodException {
        Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        Constructor2<TreeMap> constructorInfo = classInfoImpl
                .getApplicableConstructor(treeMap);
        Assertions.assertNotNull(constructorInfo);
        Constructor<?> selectedConstructor = constructorInfo.getMember();
        Constructor<TreeMap> constructor = TreeMap.class
                .getConstructor(SortedMap.class);
        Assertions.assertEquals(constructor, selectedConstructor);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void constructorSelected() throws SecurityException,
            NoSuchMethodException {
        Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
        Constructor2<TreeMap> constructorInfo = classInfoImpl
                .getApplicableConstructor(HashMap.class);
        Assertions.assertNotNull(constructorInfo);
        Constructor<?> selectedConstructor = constructorInfo.getMember();
        Assertions.assertNotNull(selectedConstructor);
        Constructor<TreeMap> constructor = TreeMap.class
                .getConstructor(Map.class);
        Assertions.assertEquals(constructor, selectedConstructor);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void invoke() throws Exception {
        Class2<TreeMap> classInfoImpl = Class2.get(TreeMap.class);
        Constructor2<TreeMap> constructor2 = classInfoImpl
                .getApplicableConstructor();
        Assertions.assertNotNull(constructor2);
        TreeMap invoke = constructor2.getInvokable().invoke();
        Assertions.assertNotNull(invoke);

    }

    @Test
    void declaredExceptions() throws Exception {
        Class2<URL> classInfoImpl = Class2.get(URL.class);
        Constructor2<URL> constructor2 = classInfoImpl
                .getApplicableConstructor(String.class);
        Assertions.assertNotNull(constructor2);
        Class<?>[] declaredExceptionTypes = constructor2
                .getDeclaredExceptionTypes();
        Assertions.assertEquals(1, declaredExceptionTypes.length);
        Assertions.assertEquals(MalformedURLException.class, declaredExceptionTypes[0]);
    }

}
