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
import static junit.framework.Assert.assertTrue;

import java.awt.Container;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComponent;

import org.apache.commons.collections4.Predicate;
import org.junit.Test;

public class DeclaringClassMemberPredicateTest {

	@Test
	public void evaluateTrue() throws SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Predicate declaringClassMemberPredicate = ReflectFacade
				.getDeclaringClassPredicate(JComponent.class);
		Method declaredMethod = JComponent.class
				.getDeclaredMethod("getPreferredSize");
		boolean evaluate = declaringClassMemberPredicate
				.evaluate(declaredMethod);
		assertTrue(evaluate);

		Constructor<JComponent> declaredConstructor = JComponent.class
				.getDeclaredConstructor();
		evaluate = declaringClassMemberPredicate.evaluate(declaredConstructor);
		assertTrue(evaluate);

		Field declaredField = JComponent.class.getDeclaredField("WHEN_FOCUSED");
		evaluate = declaringClassMemberPredicate.evaluate(declaredField);
		assertTrue(evaluate);
	}

	@Test
	public void evaluateFalse() throws SecurityException,
			NoSuchMethodException, NoSuchFieldException {
		Predicate declaringClassMemberPredicate = ReflectFacade
				.getDeclaringClassPredicate(JComponent.class);
		Method declaredMethod = Container.class
				.getDeclaredMethod("getPreferredSize");
		boolean evaluate = declaringClassMemberPredicate
				.evaluate(declaredMethod);
		assertFalse(evaluate);

		Constructor<Container> declaredConstructor = Container.class
				.getDeclaredConstructor();
		evaluate = declaringClassMemberPredicate.evaluate(declaredConstructor);
		assertFalse(evaluate);

		Field declaredField = Container.class.getDeclaredField("INCLUDE_SELF");
		evaluate = declaringClassMemberPredicate.evaluate(declaredField);
		assertFalse(evaluate);
	}

}
