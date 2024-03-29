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

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.Signature;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A predicate that evaluates to true if the object it is evaluates against is a
 * constructor or method and has the same signature as the constructor or method
 * that this {@link SignaturePredicate} has been constructed with.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0;
 */
public class SignaturePredicate<T extends Member> implements Predicate<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4057856418953113928L;

    private Member2<?> member2;

    /**
     * Constructs a {@link SignaturePredicate} that evaluates to true if the
     * object it is evaluates against is a constructor with the given
     * {@link Constructor}'s signature.
     *
     * @param constructor
     * @since 1.0.0;
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public SignaturePredicate(Constructor<?> constructor) {
        this(new Constructor2(constructor));
    }

    /**
     * Constructs a {@link SignaturePredicate} that evaluates to true if the
     * object it is evaluates against is a method with the given {@link Method}
     * 's signature.
     *
     * @param method
     * @since 1.0.0;
     */
    public SignaturePredicate(Method method) {
        this(new Method2(method));
    }

    private SignaturePredicate(Member2<?> member2) {
        this.member2 = requireNonNull(member2);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Member2<?> createMember2(Member member) {
        Assert.instanceOf("member", member, Method.class, Constructor.class);

        if (member instanceof Method) {
            return new Method2((Method) member);
        } else {
            return new Constructor2((Constructor<?>) member);
        }
    }

    private Member2<?> getMember2() {
        return member2;
    }

    /**
     * @param object
     *            should be a {@link Constructor}, {@link Method} or a
     *            {@link Signature}. All other types evaluate to false.
     *
     * @since 1.0.0;
     * @version 1.2.0;
     */
    public boolean test(T object) {
        Member2<?> otherInvokable = createMember2(object);
        Signature otherSignature = otherInvokable.getSignature();
        Signature signature = getMember2().getSignature();
        return signature.equals(otherSignature);
    }
}
