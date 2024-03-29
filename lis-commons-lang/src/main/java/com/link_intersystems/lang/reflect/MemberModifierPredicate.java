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

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A {@link Predicate} that evaluates {@link Member}'s modifiers. See
 * {@link #test(Member)} for details.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.0.0;
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
     * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     * intersystems.com]</a>
     * @since 1.0.0;
     */
    public enum Match {
        /**
         * Exact modifier match. E.g. {@link Member#getModifiers()} ==
         * definedModifiers.
         *
         * @since 1.0.0;
         */
        EXACT {
            @Override
            boolean matches(int modifiers, int otherModifieres) {
                return modifiers == otherModifieres;
            }
        },
        /**
         * At least one modifier must match. If the {@link Member}'s modifiers
         * are public static final and the modifiers compared to are final it
         * matches.
         *
         * @since 1.0.0;
         */
        AT_LEAST_ONE {
            @Override
            boolean matches(int modifiers, int otherModifiers) {
                return (modifiers & otherModifiers) != 0;
            }
        },
        /**
         * At least all modifier must match. If the {@link Member}'s modifiers
         * are public abstract and the modifiers compared to are public it
         * matches. In contrast to {@link #EXACT} this {@link Match} type does
         * not enforce that all modifiers must match. It only defines that the
         * modifiers compared to must all match.
         *
         * @since 1.0.0;
         */
        AT_LEAST_ALL {
            @Override
            boolean matches(int modifiers, int otherModifiers) {
                return (modifiers & otherModifiers) == modifiers;
            }
        };

        abstract boolean matches(int modifiers, int otherModifiers);
    }

    /**
     * Constructs a {@link MemberModifierPredicate} that maches the defined
     * modifers with {@link Match} type {@link Match#AT_LEAST_ALL};
     *
     * @param modifiers
     * @since 1.0.0;
     */
    public MemberModifierPredicate(int modifiers) {
        this(modifiers, Match.AT_LEAST_ALL);
    }

    /**
     * Constructs a {@link MemberModifierPredicate} that maches the defined
     * modifers with the given {@link Match} type.
     *
     * @param modifiers
     * @since 1.0.0;
     */
    public MemberModifierPredicate(int modifiers, Match match) {
        requireNonNull(match);
        this.modifiers = modifiers;
        this.match = match;
    }

    /**
     * @return true if the object evaluated against is a {@link Member} and that
     * {@link Member}s modifiers match the modifiers that this
     * {@link MemberModifierPredicate} was constructed with according to
     * the {@link Match} type.
     * @throws IllegalArgumentException if the object this {@link Predicate} is evaluated against is
     *                                  not a {@link Member}.
     * @since 1.0.0;
     */
    public boolean test(Member member) {
        int modifiers = member.getModifiers();
        return match.matches(this.modifiers, modifiers);
    }

}
