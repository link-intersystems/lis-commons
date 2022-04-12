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
package com.link_intersystems.lang.reflect.criteria;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

public class SelectionEnumTest {

    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{{Result.FIRST}, {Result.ALL}, {Result.LAST}

        });
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void valueOfAndName(Result enumObj) {
        String name = enumObj.name();
        Result valueOf = Result.valueOf(name);
        assertEquals(enumObj, valueOf);
    }
}
