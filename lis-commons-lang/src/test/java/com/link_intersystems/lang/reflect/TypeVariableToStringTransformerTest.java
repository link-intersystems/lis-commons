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

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.lang.reflect.testclasses.Class2ToStringBoundTypeNotClass;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringGeneric;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringGenericNoBounds;
import com.link_intersystems.lang.reflect.testclasses.Class2ToStringParameterizedTypeBoundType;

public class TypeVariableToStringTransformerTest {

	private TypeVariableToStringTransformer transformer;

	@Before
	public void setup() {
		transformer = new TypeVariableToStringTransformer();

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void toStringForBoundTypes() {
		Class2<Class2ToStringGeneric> class2ToStringGeneric = Class2
				.get(Class2ToStringGeneric.class);
		TypeVariable<?>[] typeVariables = class2ToStringGeneric
				.getTypeVariables();

		Object transformed = transformer.transform(typeVariables[0]);
		Assert.assertEquals("A extends java.lang.annotation.Annotation",
				transformed);

		transformed = transformer.transform(typeVariables[1]);
		Assert.assertEquals(
				"C extends java.lang.annotation.Annotation & java.io.Serializable",
				transformed);

		transformed = transformer.transform(typeVariables[2]);
		Assert.assertEquals("B extends java.io.Serializable", transformed);

		transformed = transformer.transform(typeVariables[3]);
		Assert.assertEquals("D", transformed);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void toStringForUnboundTypes() {
		Class2<Class2ToStringGenericNoBounds> forClass = Class2
				.get(Class2ToStringGenericNoBounds.class);
		TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

		Object transformed = transformer.transform(typeVariables[0]);
		Assert.assertEquals("A", transformed);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void toStringForNoClassBoundTypes() {
		Class2<Class2ToStringBoundTypeNotClass> forClass = Class2
				.get(Class2ToStringBoundTypeNotClass.class);
		TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

		Object transformed = transformer.transform(typeVariables[0]);
		Assert.assertEquals("B extends java.io.Serializable", transformed);

		transformed = transformer.transform(typeVariables[1]);
		Assert.assertEquals("A extends B", transformed);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void toStringForParameterizedTypeBoundTypes() {
		Class2<Class2ToStringParameterizedTypeBoundType> forClass = Class2
				.get(Class2ToStringParameterizedTypeBoundType.class);
		TypeVariable<?>[] typeVariables = forClass.getTypeVariables();

		Object transformed = transformer.transform(typeVariables[0]);
		Assert.assertEquals("B extends java.io.Serializable", transformed);

		transformed = transformer.transform(typeVariables[1]);
		Assert.assertEquals("A extends java.util.List<B>", transformed);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void transformUnknownType() {
		TypeVariable unknownTypeVar = EasyMock
				.createNiceMock(TypeVariable.class);
		EasyMock.expect(unknownTypeVar.getName()).andReturn("A");
		Type unknownType = EasyMock.createNiceMock(Type.class);
		Type[] types = new Type[] { unknownType };
		EasyMock.expect(unknownTypeVar.getBounds()).andReturn(types);
		EasyMock.replay(unknownTypeVar, unknownType);
		Object transformed = transformer.transform(unknownTypeVar);
		Assert.assertEquals("A extends ???", transformed);
	}
}
