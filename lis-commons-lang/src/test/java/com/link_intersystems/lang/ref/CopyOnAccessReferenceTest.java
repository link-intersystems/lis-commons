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

import java.io.Serializable;

class CopyOnAccessReferenceTest {

    @Test
    void copyOnAccess() {
        StringTestHolder stringTestHolder = new StringTestHolder();
        stringTestHolder.string = "Test";

        Reference<StringTestHolder> copyOnAccessReference = new CopyOnAccessReference<StringTestHolder>(
                stringTestHolder);
        StringTestHolder copy = copyOnAccessReference.get();
        Assertions.assertEquals(stringTestHolder, copy);
        Assertions.assertNotSame(stringTestHolder, copy);

        Assertions.assertEquals("Test", copy.string);

        stringTestHolder.string = "Test2";
        StringTestHolder copy2 = copyOnAccessReference.get();
        Assertions.assertEquals(stringTestHolder, copy2);
        Assertions.assertNotSame(stringTestHolder, copy2);

        Assertions.assertEquals("Test2", copy2.string);
    }

    @Test
    void copyOnAccessForNullRef() {
        Reference<StringTestHolder> copyOnAccessReference = new CopyOnAccessReference<StringTestHolder>(
                null);
        StringTestHolder copy = copyOnAccessReference.get();
        Assertions.assertNull(copy);
    }
}

class StringTestHolder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String string;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((string == null) ? 0 : string.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StringTestHolder other = (StringTestHolder) obj;
        if (string == null) {
            return other.string == null;
        } else return string.equals(other.string);
    }

}
