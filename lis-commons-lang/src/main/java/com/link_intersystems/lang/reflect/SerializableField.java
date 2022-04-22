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
import com.link_intersystems.lang.ref.AbstractSerializableReference;
import com.link_intersystems.lang.ref.Reference;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Represents a {@link Field} object {@link Reference} that is
 * {@link Serializable}.
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @since 1.0.0;
 *
 */
class SerializableField extends AbstractSerializableReference<Field> {

    /**
     *
     */
    private static final long serialVersionUID = -6636456646515992155L;

    public SerializableField(Field field) {
        super(field);
        Assert.notNull("field", field);
    }

    @Override
    protected Serializable serialize(Field nonSerializableObject) {
        MemberSerialization<Field> memberSerializationInfo = new MemberSerialization<Field>(
                nonSerializableObject);
        return memberSerializationInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Field deserialize(Serializable restoreInfo)
            throws NoSuchFieldException {
        MemberSerialization<Field> memberSerializationInfo = (MemberSerialization<Field>) restoreInfo;
        Class<?> declaringClass = memberSerializationInfo.getDeclaringClass();
        String fieldName = memberSerializationInfo.getMemberName();
        int modifiers = memberSerializationInfo.getModifiers();
        Field field = declaringClass.getDeclaredField(fieldName);
        int currentModifiers = getModifier(field);
        if (modifiers != currentModifiers) {
            throw new IllegalStateException(
                    "Unable to restore field "
                            + fieldName
                            + " declared at "
                            + declaringClass
                            + ". Modifiers changed since serialization. Expected modifiers are "
                            + Modifier.toString(modifiers)
                            + ", but current modifiers are "
                            + Modifier.toString(currentModifiers));
        }
        return field;
    }

    int getModifier(Field field) {
        int currentModifiers = field.getModifiers();
        return currentModifiers;
    }

}