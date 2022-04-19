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

import java.lang.reflect.Method;
import java.util.ArrayList;

class MemberNameComparatorTest {

    @Test
    void compareNullValues() {
        int compare = ReflectFacade.getMemberNameComparator().compare(null,
                null);
        Assertions.assertEquals(0, compare);
    }

    @Test
    void compareNullWithMember() throws SecurityException,
            NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
                Object.class);
        int compare = ReflectFacade.getMemberNameComparator().compare(null,
                declaredMethod);
        Assertions.assertEquals(-1, compare);
    }

    @Test
    void compareMemberWithNull() throws SecurityException,
            NoSuchMethodException {
        Method declaredMethod = ArrayList.class.getDeclaredMethod("add",
                Object.class);
        int compare = ReflectFacade.getMemberNameComparator().compare(
                declaredMethod, null);
        Assertions.assertEquals(1, compare);
    }

    @Test
    void compareMembers() throws SecurityException,
            NoSuchMethodException {
        Method addMethod = ArrayList.class.getDeclaredMethod("add",
                Object.class);
        Method sizeMethod = ArrayList.class.getDeclaredMethod("size");
        int compare = ReflectFacade.getMemberNameComparator().compare(
                addMethod, sizeMethod);
        Assertions.assertTrue(compare < 0);
        compare = ReflectFacade.getMemberNameComparator().compare(sizeMethod,
                addMethod);
        Assertions.assertTrue(compare > 0);
    }
}
