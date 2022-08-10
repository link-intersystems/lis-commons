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

import com.link_intersystems.lang.Signature;
import com.link_intersystems.test.EqualsAndHashCodeTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class ConstructorSignatureTest extends EqualsAndHashCodeTest {

    @BeforeEach
    @Override
    public void createTestInstances() throws Exception {
        super.createTestInstances();
    }

    @Test
    void toStringTest() throws Exception {
        Signature signature = (Signature) createNotEqualInstance();
        String string = signature.toString();
        Assertions.assertEquals("protected com.link_intersystems.lang.reflect.SignatureTestClass(String, String)", string);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected Object createInstance() throws Exception {
        Constructor<?> constructor = SignatureTestClass.class
                .getDeclaredConstructor(String.class);
        Constructor2<?> Constructor2 = new Constructor2(constructor);
        return Constructor2.getSignature();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Object createNotEqualInstance() throws Exception {
        Constructor<?> constructor = SignatureTestClass.class
                .getDeclaredConstructor(String.class, String.class);
        Constructor2<?> Constructor2 = new Constructor2(constructor);
        return Constructor2.getSignature();
    }
}
