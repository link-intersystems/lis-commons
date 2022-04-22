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

import static org.junit.jupiter.api.Assertions.assertThrows;

class Package2Test {

    @Test
    void getNullPackage() {
        Package2 package2 = Package2.get((Package) null);
        Assertions.assertNull(package2);
    }

    @Test
    void packageCache() {
        Package2 expected = Package2.get(Package2Test.class.getPackage());
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        Assertions.assertSame(expected, package2);
    }

    @Test
    void packageByName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage().getName());
        Assertions.assertEquals("com.link_intersystems.lang.reflect", package2.getName());
    }

    @Test
    void getName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        Assertions.assertEquals("com.link_intersystems.lang.reflect", package2.getName());
    }

    @Test
    void getSimpleName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        Assertions.assertEquals("reflect", package2.getSimpleName());
    }

    @Test
    void getSimpleNameForDefaultPackage() {
        Package2 package2 = Package2.get("");
        Assertions.assertNull(package2);
    }

    @Test
    void getPackage() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        Assertions.assertEquals(package1, package2.getPackage());
    }

    @Test
    void toStringTest() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        String string = package2.toString();
        Assertions.assertNotNull(string);
    }

    @Test
    void getParent() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        Package2 parent = package2.getParent();
        Assertions.assertEquals("com.link_intersystems.lang", parent.getName());
    }

    @Test
    void serializable() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        Package2 clone = Serialization.clone(package2);
        Assertions.assertEquals(package2, clone);
    }
}
