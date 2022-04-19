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

class MethodTypeTransformerTest {

    /*
     * only for test
     */
    protected MethodTypeTransformerTest someMethod() {
        return null;
    }

    @Test
    void transformField() throws SecurityException,
            NoSuchMethodException {
        MethodTypeTransformer methodTypeTransformer = new MethodTypeTransformer();
        Method method = MethodTypeTransformerTest.class
                .getDeclaredMethod("someMethod");
        Object transform = methodTypeTransformer.apply(method);
        Assertions.assertNotNull(transform);
        Assertions.assertEquals(MethodTypeTransformerTest.class, transform);
    }

}
