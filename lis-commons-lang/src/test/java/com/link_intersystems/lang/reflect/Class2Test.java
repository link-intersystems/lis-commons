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
import com.link_intersystems.util.Serialization;
import com.link_intersystems.lang.reflect.testclasses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Class2Test  {

    @BeforeEach
    public void instantiate() {
        List<String> allContainerProperties = new ArrayList<>(Arrays.asList("alignmentY", "component", "focusTraversalPolicyProvider", "layout", "containerListeners", "insets", "preferredSize", "minimumSize", "focusCycleRoot", "maximumSize", "foreground", "focusTraversalPolicy", "componentCount", "font", "components", "focusable", "enabled", "focusTraversalPolicySet", "visible", "background", "alignmentX", "name", "focusTraversalKeys"));
        ArrayList<String> allPanelProperties = new ArrayList<>(Arrays.asList("accessibleContext"));
        allPanelProperties.addAll(allContainerProperties);
    }

    @Test
    void getClassLoaderForSystemClass() throws ClassNotFoundException {
        Class2<String> class2 = Class2.get(String.class);
        ClassLoader classLoader = class2.getType().getClassLoader();
        assertNull(classLoader);

        ClassLoader classLoader2 = class2.getClassLoader();
        assertEquals(ClassLoader.getSystemClassLoader(), classLoader2);

        Class2<Object> class2ByName = Class2.get(String.class.getCanonicalName());
        assertSame(class2, class2ByName);
    }

    @Test
    void nullTypeVariableName() {
        Class2<?> genericSubClass = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        assertThrows(IllegalArgumentException.class, () -> genericSubClass.getTypeVariable(null));
    }

    @Test
    void getNonExistentTypeVariable() {
        Class2<?> genericSubClass = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        TypeVariable<?> typeVariable = genericSubClass.getTypeVariable("ABC");
        assertNull(typeVariable, "Type variable should not exist");
    }

    @Test
    void nullTypeVariable() {
        Class2<?> genericSubWithInterface = Class2.get(GenericSubWithInterface.class);
        assertThrows(IllegalArgumentException.class, () -> genericSubWithInterface.getBoundType(null));
    }

    @Test
    void simpleGenerics() {
        Class2<?> genericSubClass = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        Class2<?> genericSubWithInterface = Class2.get(GenericSubWithInterface.class);
        TypeVariable<?> typeVariable = genericSubClass.getTypeVariable("C");
        Type type = genericSubWithInterface.getBoundType(typeVariable);
        assertEquals(Float.class, type);
    }

    @Test
    void genericInterfaceSimpleHierarchy() {
        Class2<?> genericSubWithInterface = Class2.get(GenericSubWithInterface.class);
        Class2<?> genericInterface = Class2.get(GenericSubInterface.class);
        TypeVariable<?> typeVariable = genericInterface.getTypeVariable("B");
        Type boundType = genericSubWithInterface.getBoundType(typeVariable);
        assertEquals(Float.class, boundType);
    }

    @Test
    void genericInterfaceKomplexHierarchy() {
        Class2<?> genericSubWithInterface = Class2.get(GenericSubWithInterface.class);
        Class2<?> genericInterface = Class2.get(GenericInterface_Types_A_B_C.class);
        TypeVariable<?> typeVariable = genericInterface.getTypeVariable("B");
        Type boundType = genericSubWithInterface.getBoundType(typeVariable);
        assertEquals(Double.class, boundType);
    }

    @Test
    void convenienceMethodForBoundClass() {
        Class2<?> genericSubWithInterface = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        Type boundType = genericSubWithInterface.getBoundClass("B");
        assertEquals(Integer.class, boundType);
    }

    @Test
    void convenienceMethodForBoundInterfaceTypeVar() {
        Class2<?> genericSubWithInterface = Class2.get(OnlyGenericInterface.class);
        Type boundType = genericSubWithInterface.getBoundClass("C");
        assertEquals(Integer.class, boundType);
    }

    @Test
    void convenienceMethodForBoundClassNoSuchTypeVariable() {
        Class2<?> genericSubWithInterface = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        assertThrows(IllegalArgumentException.class, () -> genericSubWithInterface.getBoundClass("X"));
    }

    @Test
    void genericClassKomplexHierarchy() {
        Class2<?> genericClass = Class2.get(GenericClass_Types_A_B_C_D.class);
        Class2<?> genericSubWithInterface = Class2.get(GenericSubWithInterface.class);
        TypeVariable<?> typeVariable = genericClass.getTypeVariable("C");
        Type boundType = genericSubWithInterface.getBoundType(typeVariable);
        assertEquals(Float.class, boundType);
    }

    @Test
    void unboundTypeVariable() {
        Class2<?> genericClass = Class2.get(GenericClass_Types_A_B_C_D.class);
        Class2<?> genericSubClass = Class2.get(GenericClass_Types_D_C_Extends_GenericClass.class);
        TypeVariable<?> typeVariable = genericClass.getTypeVariable("C");
        Type boundType = genericSubClass.getBoundType(typeVariable);
        assertNull(boundType, "type variable should not be bound");
    }

    @Test
    void boundTypeClass() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concrteGeneric = Class2.get(ConreteGenericClassWithBeanType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        Class<ConreteGenericClassWithBeanType.SomeBean> boundClass = concrteGeneric.getBoundClass(typeVariable);
        assertNotNull(boundClass);
    }

    @Test
    void boundTypeClassForNonExistingTypeVariable() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        Class<Object> boundClass = genericDefinition.getBoundClass(typeVariable);
        assertNull(boundClass);
    }

    @Test
    void instantiateTypeVariable() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concrteGeneric = Class2.get(ConreteGenericClassWithBeanType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        Object boundTypeInstance = concrteGeneric.getBoundInstance(typeVariable);
        assertNotNull(boundTypeInstance);
        assertTrue(boundTypeInstance instanceof ConreteGenericClassWithBeanType.SomeBean);
    }

    @Test
    void instantiateTypeVariableWrongConstructorArgs() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concreteGeneric = Class2.get(ConreteGenericClassWithBeanType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        assertThrows(IllegalArgumentException.class, () -> concreteGeneric.getBoundInstance(typeVariable, new byte[0]));
    }

    @Test
    void instantiateTypeVariableWithGenericBoundType() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concrteGeneric = Class2.get(GenericClassWithGenericBeanType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        Object boundTypeInstance = concrteGeneric.getBoundInstance(typeVariable);
        assertNotNull(boundTypeInstance);
        assertTrue(boundTypeInstance instanceof ArrayList);
    }

    @Test
    void instantiateTypeVariableisAnInterface() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concrteGeneric = Class2.get(CrazyParameterizedBoundType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        assertThrows(IllegalArgumentException.class, () -> concrteGeneric.getBoundInstance(typeVariable));
    }

    @Test
    void parameterizedBoundType() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> concrteGeneric = Class2.get(CrazyParameterizedBoundType.class);
        TypeVariable<?> typeVariable = genericDefinition.getTypeVariable("BEAN_TYPE");
        Class<?> boundTypeClass = concrteGeneric.getBoundClass(typeVariable);
        assertNotNull(boundTypeClass);
        assertTrue(boundTypeClass.equals(List.class));
    }

    @Test
    void arrayBoundType() {
        Class2<?> arrayBoundType = Class2.get(ArrayBoundType.class);
        Class<?> boundClass = arrayBoundType.getBoundClass("BEAN_TYPE");
        assertEquals(String[].class, boundClass);
    }

    @Test
    void genericBoundType() {
        Class2<?> arrayBoundType = Class2.get(GenericBoundType.class);
        Class<?> boundClass = arrayBoundType.getBoundClass("BEAN_TYPE");
        assertEquals(List.class, boundClass);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForMultipleParameterizedType() {
        Class2<Map> forClass = Class2.get(Map.class);
        String string = forClass.toString();
        assertEquals("java.util.Map<K,V>", string);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForSimpleParameterizedType() {
        Class2<Class2> forClass = Class2.get(Class2.class);
        String string = forClass.toString();
        assertEquals("com.link_intersystems.lang.reflect.Class2<T>", string);
    }

    @Test
    void applicableVarargsMethod() throws Exception {
        Class2<String> forClass = Class2.get(String.class);
        Method2 applicableMethod = forClass.getApplicableMethod("format", Locale.GERMAN, "Hello %s", "World");
        Invokable invokable = applicableMethod.getInvokable(String.class);
        String formatted = invokable.invoke(Locale.GERMAN, "Hello %s", "World");
        assertEquals("Hello World", formatted);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForBoundTypes() {
        Class2<Class2ToStringGeneric> forClass = Class2.get(Class2ToStringGeneric.class);
        String string = forClass.toString();
        assertEquals("com.link_intersystems.lang.reflect.testclasses.Class2ToStringGeneric<A extends java.lang.annotation.Annotation,C extends java.lang.annotation.Annotation & java.io.Serializable,B extends java.io.Serializable,D>", string);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForUnboundTypes() {
        Class2<Class2ToStringGenericNoBounds> forClass = Class2.get(Class2ToStringGenericNoBounds.class);
        String string = forClass.toString();
        assertEquals("com.link_intersystems.lang.reflect.testclasses.Class2ToStringGenericNoBounds<A>", string);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForNoClassBoundTypes() {
        Class2<Class2ToStringBoundTypeNotClass> forClass = Class2.get(Class2ToStringBoundTypeNotClass.class);
        String string = forClass.toString();
        assertEquals("com.link_intersystems.lang.reflect.testclasses.Class2ToStringBoundTypeNotClass<B extends java.io.Serializable,A extends B>", string);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void toStringForParameterizedTypeBoundTypes() {
        Class2<Class2ToStringParameterizedTypeBoundType> forClass = Class2.get(Class2ToStringParameterizedTypeBoundType.class);
        String string = forClass.toString();
        assertEquals("com.link_intersystems.lang.reflect.testclasses.Class2ToStringParameterizedTypeBoundType<B extends java.io.Serializable,A extends java.util.List<B>>", string);
    }

    @SuppressWarnings("rawtypes")
    @Test
    void contextClassLoaderAware() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(null);
            Class2<Class2ToStringGeneric> forClass = Class2.get(Class2ToStringGeneric.class);
            final ClassLoader classLoader = Class2ToStringGeneric.class.getClassLoader();
            ClassLoaderContextAware classLoaderContext = forClass.getClassLoaderContextAware();
            classLoaderContext.runInContext(new Runnable() {

                public void run() {
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    assertEquals(contextClassLoader, classLoader);
                }
            });
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    @Test
    void getPackage2() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Package package1 = GenericClassWithBeanType.class.getPackage();
        Package2 package2 = genericDefinition.getPackage();
        assertEquals(package1.getName(), package2.getName());
    }

    @Test
    void serializable() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        Class2<?> clone = Serialization.clone(genericDefinition);
        TypeVariable<?> typeVariable = clone.getTypeVariable("BEAN_TYPE");
        assertNotNull(typeVariable);
    }

    @Test
    void getArrayType() {
        Class2<?> objectType = Class2.get(Object.class);
        ArrayType<?> asArrayType = objectType.getArrayType();
        assertEquals(Object[].class, asArrayType.getType());
    }

    @Test
    void getArrayTypeOfArrayType() {
        Class2<?> objectArrayType = Class2.get(Object[].class);
        ArrayType<?> asArrayType = objectArrayType.getArrayType();
        assertEquals(Object[][].class, asArrayType.getType());
    }

    @Test
    void getArrayType2Cached() {
        Class2<?> genericDefinition = Class2.get(GenericClassWithBeanType.class);
        ArrayType<?> asArrayType1 = genericDefinition.getArrayType();
        ArrayType<?> asArrayType2 = genericDefinition.getArrayType();
        assertSame(asArrayType1, asArrayType2);
    }

}
