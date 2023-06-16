package com.link_intersystems.swing.action.concurrent;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

/**
 * An {@link Executor} that insures that each {@link Runnable} runs on the event-dispatch-thread aka. event queue.
 */
public class EventDispatchThreadExecutor implements Executor {

    public static final EventDispatchThreadExecutor INSTANCE = new EventDispatchThreadExecutor();

    @Override
    public void execute(Runnable command) {
        if (EventQueue.isDispatchThread()) {
            command.run();
            return;
        }

        try {
            SwingUtilities.invokeAndWait(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw new RuntimeException(targetException);
        }
    }
}
