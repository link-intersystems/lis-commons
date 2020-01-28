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

import org.apache.commons.collections4.Predicate;

import com.link_intersystems.lang.Assert;

/**
 * A {@link Predicate} that evaluates {@link Member}'s modifiers. See
 * {@link #evaluate(Object)} for details.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.0.0.0
 */
public class MemberModifierPredicate implements Predicate<Member>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8409323272367693837L;

	private final int modifiers;

	private Match match;

	/**
	 * The {@link Match} type that the {@link MemberModifierPredicate} uses.
	 *
	 * @author René Link <a
	 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
	 *         intersystems.com]</a>
	 * @since 1.0.0.0
	 */
	public enum Match {
		/**
		 * Exact modifier match. E.g. {@link Member#getModifiers()} ==
		 * definedModifiers.
		 *
		 * @since 1.0.0.0
		 */
		EXACT,
		/**
		 * At least one modifier must match. If the {@link Member}'s modifiers
		 * are public static final and the modifiers compared to are final it
		 * matches.
		 *
		 * @since 1.0.0.0
		 */
		AT_LEAST_ONE,
		/**
		 * At least all modifier must match. If the {@link Member}'s modifiers
		 * are public abstract and the modifiers compared to are public it
		 * matches. In contrast to {@link #EXACT} this {@link Match} type does
		 * not enforce that all modifiers must match. It only defines that the
		 * modifiers compared to must all match.
		 *
		 * @since 1.0.0.0
		 */
		AT_LEAST_ALL
	}

	/**
	 * Constructs a {@link MemberModifierPredicate} that maches the defined
	 * modifers with {@link Match} type {@link Match#AT_LEAST_ALL};
	 *
	 * @param modifiers
	 * @since 1.0.0.0
	 */
	public MemberModifierPredicate(int modifiers) {
		this(modifiers, Match.AT_LEAST_ALL);
	}

	/**
	 * Constructs a {@link MemberModifierPredicate} that maches the defined
	 * modifers with the given {@link Match} type.
	 *
	 * @param modifiers
	 * @since 1.0.0.0
	 */
	public MemberModifierPredicate(int modifiers, Match match) {
		Assert.notNull("match", match);
		this.modifiers = modifiers;
		this.match = match;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return true if the object evaluated against is a {@link Member} and that
	 *         {@link Member}s modifiers match the modifiers that this
	 *         {@link MemberModifierPredicate} was constructed with according to
	 *         the {@link Match} type.
	 * @param object
	 *            must be a {@link Member}.
	 * @since 1.0.0.0
	 * @throws IllegalArgumentException
	 *             if the object this {@link Predicate} is evaluated against is
	 *             not a {@link Member}.
	 */
	public boolean evaluate(Member object) {
		Member member = (Member) object;
		int modifiers = member.getModifiers();
		if (match.equals(Match.EXACT)) {
			return this.modifiers == modifiers;
		} else if (match.equals(Match.AT_LEAST_ONE)) {
			return (modifiers & this.modifiers) != 0;
		} else {
			return (modifiers & this.modifiers) == this.modifiers;
		}
	}

}
