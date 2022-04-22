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
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Objects;

abstract class AbstractMemberInvokable<M extends Member2<? extends Member>>
        extends AbstractInvokable implements Invokable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5885139676808487497L;

    private final M invokableMember;

    private final Object target;

    public AbstractMemberInvokable(Object target, M invokableMember) {
        this.target = target;
        this.invokableMember = Objects.requireNonNull(invokableMember);
    }

    /**
     * {@inheritDoc} <br/>
     * An {@link AbstractMemberInvokable} is applicable for some arguments if
     * it's {@link #getInvokableMember()}'s
     * {@link Member2#isApplicable(Object[])} is applicable for the arguments.
     */
    protected boolean isApplicable(Object... args) {
        M invokableMember2 = getInvokableMember();
        return invokableMember2.isApplicable(args);
    }

    protected AccessibleObject getAccessibleObject() {
        M invokableMember = getInvokableMember();
        Member member = invokableMember.getMember();
        return AccessibleObject.class.cast(member);
    }

    /**
     * @return the target object to invoke.
     * @since 1.0.0;
     */
    protected final Object getTarget() {
        return target;
    }

    /**
     * @return the {@link Member2} that this
     * {@link AbstractMemberInvokable} is based on.
     * @since 1.2.0;
     */
    protected final M getInvokableMember() {
        return invokableMember;
    }

    @Override
    protected boolean isDeclaredException(Exception e) {
        return invokableMember.isDeclaredException(e);
    }

}
