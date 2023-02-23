package com.link_intersystems.swing.function;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventQueueExecutorTest {

    @Test
    void execute() {
        EventQueueExecutor eventQueueExecutor = new EventQueueExecutor();
        Runnable runnable = mock(Runnable.class);
        eventQueueExecutor.execute(runnable);
        verify(runnable).run();
    }

    @Test
    void executeInvokedFromEventDispatchThread() throws InterruptedException, InvocationTargetException {
        EventQueueExecutor eventQueueExecutor = new EventQueueExecutor();
        Runnable runnable = mock(Runnable.class);
        EventQueue.invokeAndWait(() -> eventQueueExecutor.execute(runnable));
        verify(runnable).run();
    }
}