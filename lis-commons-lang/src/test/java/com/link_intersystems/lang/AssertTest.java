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
package com.link_intersystems.lang;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssertTest {

    @Test
    void instantiable() {
        new Assert() {

        };
    }

    @Test
    void condition() {
        try {
            Assert.isTrue(false, "%s must be true", "condition");
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("condition must be true", localizedMessage);
        }
        Assert.isTrue(true, "% must be true", "condition");
    }

    @Test
    void instanceOf() {
        try {
            Assert.instanceOf("test", new Object(), Serializable.class, String.class);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("test must be an instance of [interface java.io.Serializable, class java.lang.String]", localizedMessage);
        }
        try {
            Assert.instanceOf("test", null, Serializable.class, String.class);
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String localizedMessage = e.getLocalizedMessage();
            assertEquals("test must be an instance of [interface java.io.Serializable, class java.lang.String]", localizedMessage);
        }
        Assert.instanceOf("test", "", String.class);
    }


}
