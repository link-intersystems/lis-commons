package com.link_intersystems.swing.function;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

public class EventQueueExecutor implements Executor {

    public static final EventQueueExecutor INSTANCE = new EventQueueExecutor();

    @Override
    public void execute(Runnable r) {
        try {
            if (EventQueue.isDispatchThread()) {
                r.run();
            } else {
                EventQueue.invokeAndWait(r);
            }
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
