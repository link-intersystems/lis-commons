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

import com.link_intersystems.lang.reflect.testclasses.Class2ToStringBoundTypeNotClass;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringGeneric;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringGenericNoBounds;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringParameterizedTypeBoundType;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeVariableToStringTransformerTest {

    private TypeVariableToStringTransformer transformer;

    @BeforeEach
    public void setup() {
        transformer = new TypeVariableToStringTransformer();

    }

    @SuppressWarnings("rawtypes")
    @Test
    public void toStringForBoundTypes() {
        Class2<Class2ToStringGeneric> class2ToStringGeneric = Class2.get(Class2ToStringGeneric.class);
        TypeVariable<?>[] typeVariables = class2ToStringGeneric.getTypeVariables();

        Object transformed = transformer.apply(typeVariables[0]);
        assertEquals("A extends java.lang.annotation.Annotation", transformed);

        transformed = transformer.apply(typeVariables[1]);
        assertEquals("C extends java.lang.annotation.Annotation & java.io.Serializable", transformed);

        transformed = transformer.apply(typeVariables[2]);
        assertEquals("B extends java.io.Serializable", transformed);

        transformed = transformer.apply(typeVariables[3]);
        assertEquals("D", transformed);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void toStringForUnboundTypes() {
        Class2<Class2ToStringGenericNoBounds> forClass = Class2.get(Class2ToStringGenericNoBounds.class);
        TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

        Object transformed = transformer.apply(typeVariables[0]);
        assertEquals("A", transformed);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void toStringForNoClassBoundTypes() {
        Class2<Class2ToStringBoundTypeNotClass> forClass = Class2.get(Class2ToStringBoundTypeNotClass.class);
        TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

        Object transformed = transformer.apply(typeVariables[0]);
        assertEquals("B extends java.io.Serializable", transformed);

        transformed = transformer.apply(typeVariables[1]);
        assertEquals("A extends B", transformed);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void toStringForParameterizedTypeBoundTypes() {
        Class2<Class2ToStringParameterizedTypeBoundType> forClass = Class2.get(Class2ToStringParameterizedTypeBoundType.class);
        TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

        Object transformed = transformer.apply(typeVariables[0]);
        assertEquals("B extends java.io.Serializable", transformed);

        transformed = transformer.apply(typeVariables[1]);
        assertEquals("A extends java.util.List<B>", transformed);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void transformUnknownType() {
        TypeVariable unknownTypeVar = EasyMock.createNiceMock(TypeVariable.class);
        EasyMock.expect(unknownTypeVar.getName()).andReturn("A");
        Type unknownType = EasyMock.createNiceMock(Type.class);
        Type[] types = new Type[]{unknownType};
        EasyMock.expect(unknownTypeVar.getBounds()).andReturn(types);
        EasyMock.replay(unknownTypeVar, unknownType);
        Object transformed = transformer.apply(unknownTypeVar);
        assertEquals("A extends ???", transformed);
    }
}
