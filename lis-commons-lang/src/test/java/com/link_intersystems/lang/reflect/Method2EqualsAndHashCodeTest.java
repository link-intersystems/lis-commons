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

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;

import com.link_intersystems.EqualsAndHashCodeTest;

public class Method2EqualsAndHashCodeTest extends EqualsAndHashCodeTest {

	@Before
	@Override
	public void createTestInstances() throws Exception {
		super.createTestInstances();
	}

	@Override
	protected Object createInstance() throws Exception {
		Method method = ArrayList.class.getDeclaredMethod("add", Object.class);
		Method2 applicableMethod = new Method2(method);
		return applicableMethod;
	}

	@Override
	protected Object createNotEqualInstance() throws Exception {
		Method method = ArrayList.class.getDeclaredMethod("add", int.class,
				Object.class);
		Method2 applicableMethod = new Method2(method);
		return applicableMethod;
	}

}
