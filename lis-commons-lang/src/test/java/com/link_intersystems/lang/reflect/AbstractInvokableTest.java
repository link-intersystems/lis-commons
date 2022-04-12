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

import com.link_intersystems.lang.ClassLoaderContextAware;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ClassLoaderContextAware.class})
public abstract class AbstractInvokableTest {

    private static final String EXCEPTION_PARAM_CONST = "EXCEPTION";

    private TestAbstractInvocable invokableToTest;

    private IOException ioException;

    @BeforeEach
    public void setupAbstractInvokableInstance() throws Exception {
        ioException = new IOException();
        invokableToTest = new TestAbstractInvocable();
        invokableToTest.setThrowException(ioException, EXCEPTION_PARAM_CONST);
    }

    @Test
    public void callableAdapter() throws Exception {
        invokableToTest.setReturn("result: param", "param");
        Callable<Object> asCallable = invokableToTest.asCallable("param");
        Object result = asCallable.call();
        assertEquals("result: param", result);
    }

    @Test
    public void runnableAdapter() throws Exception {
        invokableToTest.setReturn("param", "param");
        Runnable runnable = invokableToTest.asRunnable("param");
        runnable.run();
        boolean invoked = invokableToTest.isInvoked();
        assertTrue(invoked);
    }

    @Test
    public void privilegedExceptionActionAdapter() throws Exception {
        invokableToTest.setReturn("result: param", "param");
        PrivilegedExceptionAction<String> privilegedExceptionAction = invokableToTest.asPrivilegedExceptionAction("param");
        String result = privilegedExceptionAction.run();
        assertEquals("result: param", result);
        boolean invoked = invokableToTest.isInvoked();
        assertTrue(invoked);
    }

    @Test
    @SuppressWarnings("all")
    public void invokeWithNullParams() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> invokableToTest.invoke(null));
    }

    @Test
    public void doInvokeThrowsInvocationTargetExceptionWithError() throws Exception {
        invokableToTest.setThrowException(new InvocationTargetException(new Error()), EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(false);
        assertThrows(UndeclaredThrowableException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void doInvokeThrowsInvocationTargetExceptionWithUndeclaredTarget() throws Exception {
        invokableToTest.setThrowException(new InvocationTargetException(ioException), EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(false);
        assertThrows(UndeclaredThrowableException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void doInvokeThrowsInvocationTargetExceptionWithDeclaredTarget() throws Exception {
        invokableToTest.setThrowException(new InvocationTargetException(ioException), EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(true);
        assertThrows(IOException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void checkedUndeclaredException() throws Exception {
        invokableToTest.setThrowException(ioException, EXCEPTION_PARAM_CONST);
        assertThrows(UndeclaredThrowableException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void checkedDeclaredException() throws Exception {
        invokableToTest.setThrowException(ioException, EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(true);
        assertThrows(IOException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void invokeThrowsCheckedDeclaredException() throws Exception {
        invokableToTest.setThrowException(ioException, EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(true);
        assertThrows(IOException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void invokeThrowsUndeclaredException() throws Exception {
        invokableToTest.setThrowException(ioException, EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(false);
        assertThrows(UndeclaredThrowableException.class, () -> invokableToTest.invoke(EXCEPTION_PARAM_CONST));
    }

    @Test
    public void invokeWithContextClassLoader() throws Exception {
        final ClassLoader classLoader = new URLClassLoader(new URL[0]);
        invokableToTest.doInvoke();
        invokableToTest.invokeWithContextClassLoader(classLoader);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader invocationContextClassLoader = invokableToTest.getInvocationContextClassLoader();
        assertFalse(invocationContextClassLoader.equals(contextClassLoader));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void invokeWithContextClassLoaderCausingUndeclaredException() throws Exception {
        PowerMock.mockStatic(ClassLoaderContextAware.class);
        ClassLoaderContextAware loaderContextAware = PowerMock.createNiceMock(ClassLoaderContextAware.class);
        ClassLoaderContextAware.forClassLoader(getClass().getClassLoader());
        PowerMock.expectLastCall().andReturn(loaderContextAware);
        loaderContextAware.runInContext(EasyMock.anyObject(Callable.class));
        PowerMock.expectLastCall().andThrow(ioException);
        PowerMock.replay(loaderContextAware, ClassLoaderContextAware.class);
        invokableToTest.setDeclared(false);
        assertThrows(UndeclaredThrowableException.class, () -> invokableToTest.invokeWithContextClassLoader(getClass().getClassLoader()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void invokeWithContextClassLoaderCausingDeclaredException() throws Exception {
        PowerMock.mockStatic(ClassLoaderContextAware.class);
        ClassLoaderContextAware loaderContextAware = PowerMock.createNiceMock(ClassLoaderContextAware.class);
        ClassLoaderContextAware.forClassLoader(getClass().getClassLoader());
        PowerMock.expectLastCall().andReturn(loaderContextAware);
        loaderContextAware.runInContext(EasyMock.anyObject(Callable.class));
        PowerMock.expectLastCall().andThrow(ioException);
        PowerMock.replay(loaderContextAware, ClassLoaderContextAware.class);
        invokableToTest.setDeclared(true);
        assertThrows(IOException.class, () -> invokableToTest.invokeWithContextClassLoader(getClass().getClassLoader()));
    }

    @Test
    public void asRunnableThrowsException() throws Exception {
        invokableToTest.setDeclared(true);
        Runnable asRunnable = invokableToTest.asRunnable(EXCEPTION_PARAM_CONST);
        assertThrows(RuntimeException.class, () -> asRunnable.run());
    }

    @Test
    public void asRunnableThrowsRuntimeException() throws Exception {
        IndexOutOfBoundsException ofBoundsException = new IndexOutOfBoundsException();
        invokableToTest.setThrowException(ofBoundsException, EXCEPTION_PARAM_CONST);
        invokableToTest.setDeclared(true);
        Runnable asRunnable = invokableToTest.asRunnable(EXCEPTION_PARAM_CONST);
        assertThrows(IndexOutOfBoundsException.class, () -> asRunnable.run());
    }

    @Test
    public void defaultIsDeclaredExceptionReturnValue() throws Exception {
        AbstractInvokable abstractInvokable = new AbstractInvokable() {

            @Override
            protected <T> T doInvoke(Object... params) throws Exception {
                return null;
            }
        };

        boolean declaredException = abstractInvokable.isDeclaredException(ioException);
        assertFalse(declaredException);

        declaredException = abstractInvokable.isDeclaredException(new RuntimeException());
        assertFalse(declaredException);

        declaredException = abstractInvokable.isDeclaredException(null);
        assertFalse(declaredException);
    }

    private static class TestAbstractInvocable extends AbstractInvokable {

        private Exception throwException;

        private boolean isDeclared;

        private Object[] throwParams;

        private Object returnValue;

        private Object[] returnParams;

        private boolean isInvoked;

        private ClassLoader contextClassLoader;

        @SuppressWarnings("unchecked")
        @Override
        protected <T> T doInvoke(Object... params) throws Exception {
            isInvoked = true;
            contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (this.throwParams != null && Arrays.equals(params, this.throwParams) && throwException != null) {
                throw throwException;
            }

            if (this.returnParams != null && Arrays.equals(params, this.returnParams)) {
                return (T) returnValue;
            }
            return null;
        }

        public ClassLoader getInvocationContextClassLoader() {
            return contextClassLoader;
        }

        public boolean isInvoked() {
            return isInvoked;
        }

        @Override
        protected boolean isDeclaredException(Exception e) {
            return isDeclared;
        }

        public void setDeclared(boolean isDeclared) {
            this.isDeclared = isDeclared;
        }

        public void setThrowException(Exception throwException, Object... params) {
            this.throwException = throwException;
            this.throwParams = params;
        }

        public void setReturn(Object returnValue, Object... params) {
            this.returnValue = returnValue;
            returnParams = params;
        }
    }
}
