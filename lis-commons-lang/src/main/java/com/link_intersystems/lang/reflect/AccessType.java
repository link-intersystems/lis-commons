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
 * language are not sufficient for the use case that the {@link com.link_intersystems.lang.reflect.criteria.MemberCriteria}
 * supports, because {@link Modifier} doesn't declare the default modifier. The
 * default is defined implicitly if none of the other access modifiers are
 * declared for a {@link Member}. Here is an example why we need to define our
 * own enum for access modifier. <br/>
 * Suppose you want to describe that all members should be found that have protected
 * or default access modifiers. How do you select them with the {@link Modifier}
 * class??? {@link Modifier#PROTECTED} | ??? <br/>
 * The answer is to say that you want to select all that match the protected and
 * all that doesn't have any of the access modifiers. <br/>
 * So the definition would be something like...
 * 
 * <pre>
 * (({@link Modifier#PROTECTED} & searchForModifier) != 0)
 * 		|| <b>!</b>(({@link Modifier#PUBLIC}
 * 				| {@link Modifier#PROTECTED}
 * 				| {@link Modifier#PRIVATE}) & searchForModifier) != 0)
 * </pre>
 * 
 * And now the big question is... how can you define a method for the
 * {@link com.link_intersystems.lang.reflect.criteria.MemberCriteria} to allow callers to select this???<br/>
 * 
 * <pre>
 * // Default java would not work
 * public void withAccess(int modifiers);
 * 
 * // clients would only be able to select public, protected and/or private.
 * memberFInder.withAccess({@link Modifier#PROTECTED} | ??? how to select default ???);
 * </pre>
 * 
 * Thats the reason why the {@link AccessType} exists.
 * 
 * @author René Link [<a
 *         href="mailto:rene.link@link-intersystems.com">rene.link@link-
 *         intersystems.com</a>]
 * @since 1.0.0;
 * @see com.link_intersystems.lang.reflect.criteria.MemberCriteria#withAccess(AccessType...)
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