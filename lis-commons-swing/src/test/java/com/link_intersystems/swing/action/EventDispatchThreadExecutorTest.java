package com.link_intersystems.swing.action;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventDispatchThreadExecutorTest {


    @Test
    void invokedByTheEventDispatchThread() throws InterruptedException, InvocationTargetException {
        Runnable runnable = mock(Runnable.class);

        SwingUtilities.invokeAndWait(() -> EventDispatchThreadExecutor.INSTANCE.execute(runnable));

        verify(runnable).run();
    }

    @Test
    void invokedByNoneEventDispatchThread() {
        Runnable runnable = mock(Runnable.class);

        EventDispatchThreadExecutor.INSTANCE.execute(runnable);

        verify(runnable).run();
    }

    @Test
    void invocationTargetException() {
        Runnable runnable = mock(Runnable.class);
        RuntimeException runtimeException = new RuntimeException();
        doThrow(runtimeException).when(runnable).run();

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> EventDispatchThreadExecutor.INSTANCE.execute(runnable));

        verify(runnable).run();

        assertEquals(runtimeException, thrownException.getCause());
    }

}