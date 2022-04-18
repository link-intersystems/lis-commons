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
package com.link_intersystems.beans.java;

import com.link_intersystems.EqualsAndHashCodeTest;

class ArrayIndexedPropertyEqualsAndHashCodeTest extends EqualsAndHashCodeTest {

    private TestBeansFactory beansFactory = new TestJavaBeansFactory();

    @Override
    protected Object createInstance() {
        SomeBean someBean = new SomeBean() {
            {
                setIndexedPropertyReadOnlyIndexOnlyAccess(new String[]{"a", ""});
            }
        };
        return beansFactory.createBean(someBean).getProperty("indexedPropertyReadOnlyIndexOnlyAccess");
    }

    @Override
    protected Object createNotEqualInstance() {
        SomeBean someBean = new SomeBean() {
            {
                setIndexedPropertyReadOnlyIndexOnlyAccess(new String[]{"a", "b"});
            }
        };
        return beansFactory.createBean(someBean).getProperty("indexedPropertyReadOnlyIndexOnlyAccess");
    }

}
