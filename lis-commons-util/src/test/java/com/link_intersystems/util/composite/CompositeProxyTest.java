package com.link_intersystems.util.composite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompositeProxyTest {

    interface A {
        int doSomething(String arg0, String arg1);

        int doSomething2(String arg0, String arg1);
    }

    interface B extends A {
        void run();
    }

    interface C {

        static int add(int a, int b) {
            return a + b;
        }

        void run();
    }

    private Runnable runnable1;
    private Runnable runnable2;
    private Runnable runnableComposite;

    @BeforeEach
    void setUp() {
        runnable1 = mock(Runnable.class);
        runnable2 = mock(Runnable.class);

        runnableComposite = CompositeProxy.create(Runnable.class, runnable1, runnable2);
    }

    @Test
    void canNotHandleReturnTypes() {
        assertThrows(IllegalArgumentException.class, () -> CompositeProxy.create(B.class, mock(B.class)));

        C c = CompositeProxy.create(C.class, mock(C.class));
        assertNotNull(c);
    }

    @Test
    void invoke() {
        runnableComposite.run();

        verify(runnable1).run();
        verify(runnable2).run();
    }

    @Test
    void invokeOnException() {
        RuntimeException runtimeException1 = new RuntimeException();
        doThrow(runtimeException1).when(runnable1).run();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> runnableComposite.run());

        assertEquals(runtimeException1, runtimeException);

        verify(runnable1).run();
        verify(runnable2, never()).run();
    }

    @Test
    void equals() {
        Runnable runnable = CompositeProxy.create(Runnable.class, runnable1, runnable2);

        assertEquals(runnableComposite, runnable);
    }

    @Test
    void hashCodeTest() {
        Runnable runnable = CompositeProxy.create(Runnable.class, runnable1, runnable2);

        assertEquals(runnableComposite.hashCode(), runnable.hashCode());
    }
}