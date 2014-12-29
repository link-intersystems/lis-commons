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

import java.io.Serializable;
import java.lang.reflect.Member;

import com.link_intersystems.lang.Assert;

/**
 * Holds information about a {@link Member} in a serializable form to restore
 * that {@link Member} later.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 * @since 1.2.0.0
 */
class MemberSerialization<T extends Member> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4436811817861411997L;

	/**
	 * The name of the member
	 */
	private String memberName;

	private int modifiers;

	/**
	 * A serializable reference to the class that declares the method
	 * represented by this {@link SerializableMethod}.
	 */
	private Class<?> declaringClass;

	public MemberSerialization(Member member) {
		Assert.notNull("member", member);
		memberName = member.getName();
		modifiers = member.getModifiers();
		declaringClass = member.getDeclaringClass();
	}

	protected String getMemberName() {
		return memberName;
	}

	protected int getModifiers() {
		return modifiers;
	}

	protected Class<?> getDeclaringClass() {
		return declaringClass;
	}

}