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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.link_intersystems.lang.Signature;
import com.link_intersystems.lang.ref.SerializableReference;

public class Member2Test {

	private Member2<?> overriding;
	private Member2<?> overridden;

	@Before
	public void setup() {
		overriding = PowerMock.createPartialMock(Member2.class, "getModifiers");
		overridden = PowerMock.createPartialMock(Member2.class, "getModifiers");
	}

	@Test
	public void overriddenPublic() {
		expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
		expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		boolean areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
		expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertFalse(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overriding.getModifiers()).andReturn(0);
		expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertFalse(areAccessModifierCompatible);

	}

	@Test
	public void overriddenProtected() {
		expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);

		expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
		replay(overriding, overridden);
		boolean areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);
		expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overridden.getModifiers()).andReturn(Modifier.PROTECTED);
		expect(overriding.getModifiers()).andReturn(0);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertFalse(areAccessModifierCompatible);

	}

	@Test
	public void overriddenDefault() {
		expect(overridden.getModifiers()).andReturn(0);
		expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		boolean areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overridden.getModifiers()).andReturn(0);
		expect(overriding.getModifiers()).andReturn(Modifier.PROTECTED);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overridden.getModifiers()).andReturn(0);
		expect(overriding.getModifiers()).andReturn(0);
		replay(overriding, overridden);

		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertTrue(areAccessModifierCompatible);
	}

	@Test
	public void privateMethods() {
		expect(overridden.getModifiers()).andReturn(Modifier.PRIVATE);
		expect(overriding.getModifiers()).andReturn(Modifier.PUBLIC);
		replay(overriding, overridden);
		boolean areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertFalse(areAccessModifierCompatible);

		reset(overriding, overridden);
		expect(overridden.getModifiers()).andReturn(Modifier.PUBLIC);
		expect(overriding.getModifiers()).andReturn(Modifier.PRIVATE);
		replay(overriding, overridden);
		areAccessModifierCompatible = overriding
				.isAccessModifierOverriddingCompatible(overridden);
		assertFalse(areAccessModifierCompatible);
	}

	@Test
	public void getSerializableRefForMember2() throws NoSuchMethodException {
		Class2<Member2Test> class2 = Class2.get(Member2Test.class);
		Method2 declaringMethod2 = class2
				.getDeclaringMethod2("getSerializableRefForMember2");
		SerializableReference<Method2> serializableReference = Member2
				.getSerializableReference(declaringMethod2);
		Method2 method2 = serializableReference.get();
		assertSame(declaringMethod2, method2);
	}

	@Test
	public void isDeclaredException() {
		Method[] declaredMethods = TestInvokableMember.class
				.getDeclaredMethods();
		TestInvokableMember testInvokableMember = new TestInvokableMember(
				declaredMethods[0]);
		assertTrue(testInvokableMember.isDeclaredException(IOException.class));
		assertTrue(testInvokableMember.isDeclaredException(new IOException()));
		assertTrue(testInvokableMember
				.isDeclaredException(FileNotFoundException.class));
		assertFalse(testInvokableMember
				.isDeclaredException(ClassCastException.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void isDeclaredExceptionForNullClass() {
		Method[] declaredMethods = TestInvokableMember.class
				.getDeclaredMethods();
		TestInvokableMember testInvokableMember = new TestInvokableMember(
				declaredMethods[0]);
		testInvokableMember
				.isDeclaredException((Class<? extends Exception>) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isDeclaredExceptionForNullException() {
		Method[] declaredMethods = TestInvokableMember.class
				.getDeclaredMethods();
		TestInvokableMember testInvokableMember = new TestInvokableMember(
				declaredMethods[0]);
		testInvokableMember.isDeclaredException((Exception) null);
	}

	private static class TestInvokableMember extends Member2<Method> {

		protected TestInvokableMember(Method member) {
			super(member);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = -5699959517523716890L;

		@Override
		public Class<?>[] getDeclaredExceptionTypes() {
			return new Class<?>[] { ClassNotFoundException.class,
					IOException.class };
		}

		@Override
		public Signature getSignature() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Type[] getGenericParameterTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isVarArgs() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected Class<?> getReturnType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<?>[] getParameterTypes() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
