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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class FieldTypeTransformerTest {

    /*
     * only for test
     */
    protected FieldTypeTransformerTest fieldTypeTransformerTest;

    @Test
    public void transformField() throws SecurityException, NoSuchFieldException {
        FieldTypeTransformer field2TypeTransformer = new FieldTypeTransformer();
        Field declaredField = FieldTypeTransformerTest.class
                .getDeclaredField("fieldTypeTransformerTest");
        Object transform = field2TypeTransformer.apply(declaredField);
        assertNotNull(transform);
        assertEquals(FieldTypeTransformerTest.class, transform);
    }

}
