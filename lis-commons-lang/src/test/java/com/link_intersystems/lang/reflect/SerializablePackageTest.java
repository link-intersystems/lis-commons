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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SerializablePackageTest {

    @Test
    void serialize() {
        Package packageObject = SerializablePackageTest.class.getPackage();
        SerializablePackage serializablePackage = new SerializablePackage(packageObject);

        SerializablePackage deserialized = Serialization.clone(serializablePackage);

        Package deserializedPackageObject = deserialized.get();

        Assertions.assertEquals(packageObject, deserializedPackageObject);
    }

    @Test
    void classNotFound() throws Exception {
        assertThrows(IllegalStateException.class, () -> {
            Package packageObject = SerializablePackageTest.class.getPackage();
            String name = packageObject.getName();
            Whitebox.setInternalState(packageObject, String.class, "packagepath.that.does.not.exists", Package.class);
            try {
                SerializablePackage serializablePackage = new SerializablePackage(packageObject);
                SerializablePackage deserialized = Serialization.clone(serializablePackage);
                deserialized.get();
            } finally {
                Whitebox.setInternalState(packageObject, String.class, name, Package.class);
            }
        });
    }
}
