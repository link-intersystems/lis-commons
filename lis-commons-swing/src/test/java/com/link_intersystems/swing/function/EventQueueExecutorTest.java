package com.link_intersystems.swing.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventQueueExecutorTest {

    private EventQueueExecutor eventQueueExecutor;

    @BeforeEach
    void setUp(){
        eventQueueExecutor = new EventQueueExecutor();
    }

    @Test
    void execute() {
        Runnable runnable = mock(Runnable.class);
        eventQueueExecutor.execute(runnable);
        verify(runnable).run();
    }

    @Test
    void executeInvokedFromEventDispatchThread() throws InterruptedException, InvocationTargetException {
        Runnable runnable = mock(Runnable.class);
        EventQueue.invokeAndWait(() -> eventQueueExecutor.execute(runnable));
        verify(runnable).run();
    }

    @Test
    void runnableThrowsExeption() {
        IllegalStateException exception = new IllegalStateException();

        Runnable runnable = () -> {
            throw exception;
        };

        RuntimeException re = assertThrows(RuntimeException.class, () -> eventQueueExecutor.execute(runnable));
        assertEquals(re, exception);
    }

    @Test
    void interruptedException() {
        EventQueueExecutor eventQueueExecutor = new EventQueueExecutor();

        Thread thread = Thread.currentThread();

        Runnable runnable = () -> {thread.interrupt();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        assertThrows(RuntimeException.class, () -> eventQueueExecutor.execute(runnable));
    }
}