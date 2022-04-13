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

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.easymock.EasyMock.createNiceControl;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.*;

class ParameterTest  {

    private IMocksControl mocksControl;
    private Member2<?> member2Mock;

    @BeforeEach
    public void setup() {
        mocksControl = createNiceControl();
        member2Mock = mocksControl.createMock(Member2.class);
    }

    @Test
    void newWithoutMember2() {
        assertThrows(IllegalArgumentException.class, () -> new Parameter(null, 1));
    }

    @Test
    void indexParameterOutOfBounds() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[2]).anyTimes();
        mocksControl.replay();
        assertThrows(IndexOutOfBoundsException.class, () -> new Parameter(member2Mock, -1));
    }

    @Test
    void indexParameterOutOfBounds2() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[2]).anyTimes();
        mocksControl.replay();
        assertThrows(IndexOutOfBoundsException.class, () -> new Parameter(member2Mock, 3));
    }

    @Test
    void parameterClass() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{String.class}).anyTimes();
        mocksControl.replay();
        Parameter parameter = new Parameter(member2Mock, 0);
        Class2<?> parameterClass2 = parameter.getParameterClass2();
        Class<?> type = parameterClass2.getType();
        assertEquals(String.class, type);
    }

    @Test
    void applicableSubtype() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{InputStream.class}).anyTimes();
        mocksControl.replay();
        Parameter parameter = new Parameter(member2Mock, 0);
        boolean applicableSubtype = parameter.isApplicableSubtype(FilterInputStream.class);
        assertTrue(applicableSubtype);

        applicableSubtype = parameter.isApplicableSubtype(null);
        assertTrue(applicableSubtype);

        applicableSubtype = parameter.isApplicableSubtype(OutputStream.class);
        assertFalse(applicableSubtype);
    }

    /**
     * Just to increase code coverage and assert that the retuned string is
     * never null.
     */
    @Test
    void toStringTest() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{InputStream.class}).anyTimes();
        mocksControl.replay();
        Parameter parameter = new Parameter(member2Mock, 0);
        String string = parameter.toString();
        assertNotNull(string);
    }

    @Test
    void isVarargs() {
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{String.class}).anyTimes();
        expect(member2Mock.isVarArgs()).andReturn(true).anyTimes();
        mocksControl.replay();
        Parameter parameter = new Parameter(member2Mock, 0);
        boolean varArg = parameter.isVarArg();
        assertTrue(varArg);

        mocksControl.reset();
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{String.class}).anyTimes();
        expect(member2Mock.isVarArgs()).andReturn(false).anyTimes();
        mocksControl.replay();
        parameter = new Parameter(member2Mock, 0);
        varArg = parameter.isVarArg();
        assertFalse(varArg);

        mocksControl.reset();
        expect(member2Mock.getParameterTypes()).andReturn(new Class<?>[]{String.class, Object.class}).anyTimes();
        expect(member2Mock.isVarArgs()).andReturn(true).anyTimes();
        mocksControl.replay();
        parameter = new Parameter(member2Mock, 0);
        varArg = parameter.isVarArg();
        assertFalse(varArg);
    }

    @Test
    void getName() throws SecurityException, NoSuchMethodException {
        Method2 method2 = Method2.forMethod(ParameterTest.class.getDeclaredMethod("testMethod", String.class));
        List<Parameter> parameters = method2.getParameters();
        Parameter parameter = parameters.get(0);
        String name = parameter.getName();
        assertEquals("arg0", name);
    }

    public void testMethod(String testParam) {
    }
}
