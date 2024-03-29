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

import com.link_intersystems.lang.ref.HardReference;
import com.link_intersystems.lang.ref.Reference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ThreadLocalProxyTest {

    private static final ThreadLocal<Reference<String>> THREAD_LOCAL = new ThreadLocal<>();

    @Test
    void proxyTest() {
        Reference<String> referenceProxy = ThreadLocalProxy.createProxy(THREAD_LOCAL, new HardReference<>(null), Reference.class);

        String string = referenceProxy.get();
        assertNull(string);

        String string1 = "string1";
        THREAD_LOCAL.set(new HardReference<>(string1));
        string = referenceProxy.get();
        assertEquals(string1, string);

        String string2 = "string2";
        THREAD_LOCAL.set(new HardReference<>(string2));
        string = referenceProxy.get();
        assertEquals(string2, string);

        THREAD_LOCAL.remove();
        string = referenceProxy.get();
        assertNull(string);
    }
}
