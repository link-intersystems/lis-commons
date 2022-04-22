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

import com.link_intersystems.lang.Signature;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Basic fields and methods for all member2 signatures.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 *
 * @since 1.2.0;
 */
abstract class Member2Signature<T extends Member2<?>> implements Signature {

    private final T member2;

    public Member2Signature(T member2) {
        this.member2 = requireNonNull(member2);
    }

    protected final T getMember2() {
        return member2;
    }

    protected final Member getMember() {
        return getMember2().getMember();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Modifier.toString(member2.getModifiers()));
        builder.append(" ");
        Class2<?> returnClass2 = member2.getReturnClass2();
        Class<?> declaringClass = member2.getMember().getDeclaringClass();
        if (!returnClass2.getType().equals(declaringClass)) {
            builder.append(returnClass2);
            builder.append(" ");
        }

        builder.append(member2.getName());
        builder.append("(");
        List<Parameter> parameterTypes = member2.getParameters();
        for (Iterator<Parameter> iterator = parameterTypes.iterator(); iterator
                .hasNext(); ) {
            Parameter parameter = iterator.next();
            Class2<?> parameterClass2 = parameter.getParameterClass2();
            builder.append(parameterClass2.toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append(")");

        return builder.toString();
    }

}