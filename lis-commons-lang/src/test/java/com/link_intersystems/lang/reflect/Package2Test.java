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
import org.junit.Test;

import static junit.framework.Assert.*;

public class Package2Test {

    @Test(expected = IllegalArgumentException.class)
    public void getNullPackageName() {
        Package2.get((String) null);
    }

    @Test
    public void getNullPackage() {
        Package2 package2 = Package2.get((Package) null);
        assertNull(package2);
    }

    @Test
    public void packageCache() {
        Package2 expected = Package2.get(Package2Test.class.getPackage());
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        assertSame(expected, package2);
    }

    @Test
    public void packageByName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage()
                .getName());
        assertEquals("com.link_intersystems.lang.reflect", package2.getName());
    }

    @Test
    public void getName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        assertEquals("com.link_intersystems.lang.reflect", package2.getName());
    }

    @Test
    public void getSimpleName() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        assertEquals("reflect", package2.getSimpleName());
    }

    @Test
    public void getSimpleNameForDefaultPackage() {
        Package2 package2 = Package2.get("");
        assertNull(package2);
    }

    @Test
    public void getPackage() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        assertEquals(package1, package2.getPackage());
    }

    @Test
    public void toStringTest() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        String string = package2.toString();
        assertNotNull(string);
    }

    @Test
    public void getParent() {
        Package package1 = Package2Test.class.getPackage();
        Package2 package2 = Package2.get(package1);
        Package2 parent = package2.getParent();
        assertEquals("com.link_intersystems.lang", parent.getName());
    }

    @Test
    public void serializable() {
        Package2 package2 = Package2.get(Package2Test.class.getPackage());
        Package2 clone = Serialization.clone(package2);
        assertEquals(package2, clone);
    }
}
