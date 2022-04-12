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

import com.link_intersystems.lang.Serialization;
import com.link_intersystems.lang.SerializationException;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.io.Serializable;
import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SerializableFieldTest {

    protected String testField;

    @Test
    public void nullConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new SerializableField(null));
    }

    @Test
    public void serialize() throws SecurityException, NoSuchFieldException {
        Field field = SerializableFieldTest.class.getDeclaredField("testField");
        SerializableField serializableField = new SerializableField(field);

        SerializableField deserialized = Serialization.clone(serializableField);

        Field deserializedField = deserialized.get();

        assertEquals(field, deserializedField);
    }

    @Test
    public void classNotFound() throws Exception {
        assertThrows(SerializationException.class, () -> {

            Field field = SerializableFieldTest.class.getDeclaredField("testField");
            String name = field.getName();
            Whitebox.setInternalState(field, "name", "noSuchField");
            try {

                SerializableField serializableField = new SerializableField(field);

                SerializableField deserialized = Serialization.clone(serializableField);

                deserialized.get();
            } finally {
                /*
                 * Restore original state
                 */
                Whitebox.setInternalState(field, "name", name);
            }
        });
    }

    @Test
    public void modifierChanged() throws SecurityException, NoSuchFieldException {
        Field field = SerializableFieldTest.class.getDeclaredField("testField");
        SerializableField serializableField = new SerializationExceptionSerializableField(field);
        assertThrows(SerializationException.class, () -> Serialization.clone(serializableField));
    }

    private static class SerializationExceptionSerializableField extends SerializableField {

        private int modifier = 0;

        public SerializationExceptionSerializableField(Field field) {
            super(field);
        }

        /**
         *
         */
        private static final long serialVersionUID = 2898113044136529103L;

        @Override
        protected Serializable serialize(Field nonSerializableObject) {
            modifier = nonSerializableObject.getModifiers() - 1;
            return super.serialize(nonSerializableObject);
        }

        @Override
        int getModifier(Field field) {
            return modifier;
        }
    }

}
