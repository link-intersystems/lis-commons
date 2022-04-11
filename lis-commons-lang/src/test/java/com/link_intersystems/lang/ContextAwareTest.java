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

import com.link_intersystems.lang.ContextAware.ContextProvider;
import com.link_intersystems.lang.ContextAware.RunInContext;
import com.link_intersystems.lang.reflect.Method2;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

public class ContextAwareTest {

    private static final String CALLED = "CALLED";
    private Runnable runnableMock;
    private Callable<String> callableMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        runnableMock = EasyMock.createStrictMock(Runnable.class);
        runnableMock.run();
        EasyMock.expectLastCall();

        callableMock = EasyMock.createStrictMock(Callable.class);
        callableMock.call();
        EasyMock.expectLastCall().andReturn(CALLED);

        EasyMock.replay(runnableMock, callableMock);
    }

    @Test
    public void runnable() {
        final TestContextAware contextAware = new TestContextAware();

        // runnable called?
        contextAware.runInContext(runnableMock);
        EasyMock.verify(runnableMock);

        // context activated?
        contextAware.runInContext(new Runnable() {

            public void run() {
                assertTrue(contextAware.isActivated());

            }
        });
        assertFalse(contextAware.isActivated());
    }

    @Test
    public void runnableWithException() throws Exception {
        final TestContextAware contextAware = new TestContextAware();

        // callable called?
        contextAware.runInContext(runnableMock);
        EasyMock.verify(runnableMock);

        // context activated?
        try {
            contextAware.runInContext(new Runnable() {

                public void run() {
                    assertTrue(contextAware.isActivated());
                    throw new RuntimeException();
                }
            });
            assertTrue("Exception expected", false);
        } catch (RuntimeException e) {
        }
        assertFalse(contextAware.isActivated());
    }

    @Test(expected = UndeclaredThrowableException.class)
    public void runnableWithUndeclaredException() throws Exception {
        final ContextProvider contextProvider = EasyMock.createStrictMock(ContextProvider.class);
        contextProvider.provideContext(EasyMock.anyObject(RunInContext.class));
        EasyMock.expectLastCall().andThrow(new IOException());
        EasyMock.replay(contextProvider);

        final TestContextAware contextAware = new TestContextAware() {
            @Override
            protected Object activateContext() {
                return contextProvider;
            }

            @Override
            protected void deactivateContext(Object context) {
            }
        };

        // callable called?
        contextAware.runInContext(runnableMock);
    }

    @Test(expected = IOException.class)
    public void callable() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        try {
            contextAware.runInContext(new Callable<String>() {

                public String call() throws Exception {
                    assertTrue(contextAware.isActivated());
                    throw new IOException();
                }
            });
        } catch (Exception e) {
            throw e;
        }

        assertFalse(contextAware.isActivated());
    }

    @Test
    public void callableWithException() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        String runInContext = contextAware.runInContext(new Callable<String>() {

            public String call() throws Exception {
                assertTrue(contextAware.isActivated());
                return "TEST2";
            }
        });
        assertEquals("TEST2", runInContext);
        assertFalse(contextAware.isActivated());
    }

    @Test
    public void callableWithContextProvider() throws Exception {
        final ContextProvider contextProvider = EasyMock.createStrictMock(ContextProvider.class);
        contextProvider.provideContext(EasyMock.anyObject(RunInContext.class));
        EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {

            public Void answer() throws Throwable {
                Object[] currentArguments = EasyMock.getCurrentArguments();
                RunInContext contextJoinPoint = (RunInContext) currentArguments[0];
                contextJoinPoint.proceed();
                return null;
            }
        });
        EasyMock.replay(contextProvider);

        final TestContextAware contextAware = new TestContextAware() {
            @Override
            protected Object activateContext() {
                return contextProvider;
            }

            @Override
            protected void deactivateContext(Object context) {
            }
        };

        // callable called?
        contextAware.runInContext(callableMock);
        EasyMock.verify(callableMock);
    }

    @Test
    public void proxy() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        TargetInterface targetInterface = createStrictMock(TargetInterface.class);

        expect(targetInterface.concat("HELLO ", "WORLD")).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                Method2 method = contextAware.getInvocationMethod();
                Assert.assertNotNull(method);
                Method declaredMethod = TargetInterface.class.getDeclaredMethod("concat", String.class, String.class);
                Assert.assertEquals(declaredMethod, method.getMember());
                return "Hello World";
            }
        });

        replay(targetInterface);
        TargetInterface targetInterfaceContextAware = contextAware.createContextProxy(targetInterface);
        Method2 method = contextAware.getInvocationMethod();
        Assert.assertNull(method);
        String concat = targetInterfaceContextAware.concat("HELLO ", "WORLD");
        method = contextAware.getInvocationMethod();
        Assert.assertNull(method);
        assertEquals("Hello World", concat);
        assertFalse(contextAware.isActivated());
    }

    @Test
    public void proxyThrowsException() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        TargetInterface targetInterface = createStrictMock(TargetInterface.class);

        expect(targetInterface.concat("HELLO ", "WORLD")).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                Method2 method = contextAware.getInvocationMethod();
                Assert.assertNotNull(method);
                Method declaredMethod = TargetInterface.class.getDeclaredMethod("concat", String.class, String.class);
                Assert.assertEquals(declaredMethod, method.getMember());
                throw new ClassCastException();
            }
        });

        replay(targetInterface);
        TargetInterface targetInterfaceContextAware = contextAware.createContextProxy(targetInterface);
        Method2 method = contextAware.getInvocationMethod();
        Assert.assertNull(method);
        try {
            targetInterfaceContextAware.concat("HELLO ", "WORLD");
        } catch (ClassCastException illegalAccessException) {
            method = contextAware.getInvocationMethod();
            Assert.assertNull(method);
        }
        assertFalse(contextAware.isActivated());
    }

    @Test
    public void proxyNullArgsMethod() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        TargetInterface targetInterface = createStrictMock(TargetInterface.class);

        expect(targetInterface.getString()).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                return "Hello World";
            }
        });

        replay(targetInterface);
        TargetInterface targetInterfaceContextAware = contextAware.createContextProxy(targetInterface);

        String concat = targetInterfaceContextAware.getString();
        assertEquals("Hello World", concat);
        assertFalse(contextAware.isActivated());
    }

    @Test
    public void contextJoinPoint() throws Exception {
        class TestContextJoinPoint implements ContextProvider {

            public void provideContext(RunInContext contextJoinPoint) throws Exception {

                contextJoinPoint.proceed();

            }

        }

        ContextAware<ContextProvider> contextAware = new ContextAware<ContextProvider>() {

            protected ContextProvider activateContext() {
                TestContextJoinPoint testContextJoinPoint = new TestContextJoinPoint();
                return testContextJoinPoint;
            }

            @Override
            protected void deactivateContext(ContextProvider context) {
            }

        };

        contextAware.runInContext(runnableMock);
        EasyMock.verify(runnableMock);
    }

    @Test
    public void invokeStaticMethod() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        String runInContext = contextAware.invokeStaticInContext(String.class, "valueOf", Boolean.TRUE);
        assertEquals("true", runInContext);
        assertFalse(contextAware.isActivated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeUndeclaredStaticMethod() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        contextAware.invokeStaticInContext(String.class, "toString");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeUndeclaredStaticMethod2() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        contextAware.invokeStaticInContext(String.class, "toString", "");
    }

    @Test
    public void invokeMethod() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        TargetInterface targetInterface = createStrictMock(TargetInterface.class);

        expect(targetInterface.getString()).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                return "Hello World";
            }
        });

        replay(targetInterface);
        Object runInContext = contextAware.invokeInContext(targetInterface, "getString");
        assertEquals("Hello World", runInContext);
        assertFalse(contextAware.isActivated());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void contextListenerTest() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        TargetInterface targetInterface = createStrictMock(TargetInterface.class);
        ContextListener<Object> contextListener = createStrictMock(ContextListener.class);

        contextListener.contextActivated("TEST");
        EasyMock.expectLastCall();

        expect(targetInterface.getString()).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                return "Hello World";
            }
        });

        contextListener.contextDeactivated("TEST");
        EasyMock.expectLastCall();

        replay(targetInterface, contextListener);

        contextAware.addContextListener(contextListener);
        Object runInContext = contextAware.invokeInContext(targetInterface, "getString");
        EasyMock.verify(targetInterface, contextListener);
        EasyMock.reset(targetInterface, contextListener);

        expect(targetInterface.getString()).andAnswer(new IAnswer<String>() {

            public String answer() throws Throwable {
                assertTrue(contextAware.isActivated());
                return "Hello World";
            }
        });
        replay(targetInterface, contextListener);
        contextAware.removeContextListener(contextListener);
        runInContext = contextAware.invokeInContext(targetInterface, "getString");
        assertEquals("Hello World", runInContext);
        assertFalse(contextAware.isActivated());
        EasyMock.verify(targetInterface, contextListener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeMethodNullTarget() throws Exception {
        TestContextAware contextAware = new TestContextAware();
        contextAware.invokeInContext((Object) null, "getString");
    }

    @Test(expected = IllegalArgumentException.class)
    public void proxyNoInterfaces() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        contextAware.createContextProxy(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void proxyForNullTarget() throws Exception {
        final TestContextAware contextAware = new TestContextAware();
        contextAware.createContextProxy(null);
    }

    private static interface TargetInterface {

        public String concat(String s1, String s2);

        public String getString();
    }

    private static class TestContextAware extends ContextAware<Object> {

        private boolean activated;

        @Override
        protected Object activateContext() {
            this.activated = true;
            return new String("TEST");
        }

        @Override
        protected void deactivateContext(Object context) {
            assertEquals("TEST", context);
            activated = false;
        }

        public boolean isActivated() {
            return activated;
        }

    }
}
