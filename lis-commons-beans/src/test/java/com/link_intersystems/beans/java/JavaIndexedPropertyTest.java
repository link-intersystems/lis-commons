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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.beans.IntrospectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(JavaBeansExtension.class)
class JavaIndexedPropertyTest extends AbstractPropertyTest<String[]> {


    private SomeBeanFixture someBeanFixture;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws IntrospectionException {
        someBeanFixture = new SomeBeanFixture(beansFactory);
    }

    @Test
    void getValueByIndex() {
        Object propertyValue = someBeanFixture.stringProperty.getValue(0);
        assertEquals(someBeanFixture.stringArrayPropertyValue[0], propertyValue);
    }

    @Test
    void setValueByIndex() {
        someBeanFixture.stringProperty.setValue(0, "test");
        Object propertyValue = someBeanFixture.stringProperty.getValue(0);
        assertEquals("test", propertyValue);
    }

    @Override
    protected JavaProperty getReadOnlyProperty() {
        return someBeanFixture.readOnlyProperty;
    }

    @Override
    protected JavaProperty getWriteOnlyProperty() {
        return someBeanFixture.writeOnlyProperty;
    }

    @Override
    protected JavaProperty getProperty() {
        return someBeanFixture.stringProperty;
    }

    @Override
    protected String getPropertyName() {
        return "stringArrayProperty";
    }

    @Override
    protected String[] getPropertySetValue() {
        return someBeanFixture.stringArrayPropertyValue;
    }

    @Override
    protected String[] getExpectedPropertyValue() {
        return someBeanFixture.stringArrayPropertyValue;
    }

}
