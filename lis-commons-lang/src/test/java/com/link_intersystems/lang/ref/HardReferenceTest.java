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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HardReferenceTest {

    @Test
    void nullValue() {
        new HardReference<Object>(null);
    }

    @Test
    void stringValue() {
        String test = "TEST";
        Reference<String> reference = new HardReference<String>(test);
        String actual = reference.get();
        Assertions.assertSame(test, actual);
    }
}
