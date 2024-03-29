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
package com.link_intersystems.lang.ref;

import com.link_intersystems.lang.reflect.Serialization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

class NullReferenceTest {

    @Test
    void isNull() {
        SerializableReference<Object> instance = NullReference.getInstance();
        Object object = instance.get();
        Assertions.assertNull(object);
    }

    @Test
    void serialization() {
        SerializableReference<Object> instance = NullReference.getInstance();
        SerializableReference<Object> clone = Serialization.clone(instance);
        Object object = clone.get();
        Assertions.assertNull(object);
    }

    @Test
    void serializationMethods() {
        NullReference<Object> nullReference = new NullReference<Object>();
        Serializable serialize = nullReference.serialize(null);
        Assertions.assertNull(serialize);
        Object deserialize = nullReference.deserialize(serialize);
        Assertions.assertNull(deserialize);
    }
}
