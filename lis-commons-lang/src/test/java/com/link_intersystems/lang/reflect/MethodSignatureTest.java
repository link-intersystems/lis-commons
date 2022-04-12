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

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.link_intersystems.EqualsAndHashCodeTest;
import com.link_intersystems.lang.Signature;

public class MethodSignatureTest extends EqualsAndHashCodeTest {

	@BeforeEach
	@Override
	public void createTestInstances() throws Exception {
		super.createTestInstances();
	}

	@Test
	public void toStringTest() throws Exception {
		Signature signature = (Signature) createInstance();
		String string = signature.toString();
		assertEquals(
				"public final String doSomething(int, int[], String, java.util.List<E>)",
				string);
	}

	@Override
	protected Object createInstance() throws Exception {
		Method method = SignatureTestClass.class
				.getDeclaredMethod("doSomething", int.class, int[].class,
						String.class, List.class);
		Method2 MethodInvokable = new Method2(method);
		return MethodInvokable.getSignature();
	}

	@Override
	protected Object createNotEqualInstance() throws Exception {
		Method method = SignatureOtherTestClass.class.getDeclaredMethod(
				"doSomething", int.class, int[].class, String.class,
				List.class, String.class);
		Method2 MethodInvokable = new Method2(method);
		return MethodInvokable.getSignature();
	}
}
