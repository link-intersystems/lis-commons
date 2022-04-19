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

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Access modifiers definition. The {@link Modifier} specified by the java
 * language are not sufficient some use cases, because a {@link Modifier} doesn't
 * declare the default modifier. The default is defined implicitly if none of the other
 * access modifiers are declared for a {@link Member}. Here is an example why we need to
 * define our own enum for access modifier. <br/>
 *
 * Suppose you want to describe the default modifier.
 * The definition would be something like this...
 * 
 * <pre>
 * (({@link Modifier#PROTECTED} & searchForModifier) != 0)
 * 		|| <b>!</b>(({@link Modifier#PUBLIC}
 * 				| {@link Modifier#PROTECTED}
 * 				| {@link Modifier#PRIVATE}) & searchForModifier) != 0)
 * </pre>
 *
 * That's the reason why the {@link AccessType} exists.
 * 
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @since 1.0.0;
 */
public enum AccessType {

	/**
	 * The default access modifier.
	 * 
	 * @since 1.0.0;
	 */
	DEFAULT(Modifier.PUBLIC & Modifier.PRIVATE & Modifier.PROTECTED) {
		@Override
		public boolean isMatching(int modifiers) {
			return (modifiers & getModifier()) == 0;
		}
	},

	/**
	 * The public access modifier.
	 * 
	 * @since 1.0.0;
	 */
	PUBLIC(Modifier.PUBLIC),
	/**
	 * The private access modifier.
	 * 
	 * @since 1.0.0;
	 */
	PRIVATE(Modifier.PRIVATE),
	/**
	 * The protected access modifier.
	 * 
	 * @since 1.0.0;
	 */
	PROTECTED(Modifier.PROTECTED);

	private final int modifier;

	protected final int getModifier() {
		return modifier;
	}

	private AccessType(int modifier) {
		this.modifier = modifier;
	}

	/**
	 * Match method for the {@link AccessType}.
	 * 
	 * @return true if this {@link AccessType} matches the given modifiers.
	 * @since 1.0.0;
	 */
	public boolean isMatching(int modifiers) {
		return (modifiers & modifier) != 0;
	}
}