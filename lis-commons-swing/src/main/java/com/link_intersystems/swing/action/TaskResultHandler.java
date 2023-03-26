package com.link_intersystems.swing.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TaskResultHandler<T, V> {

    static <T, V> TaskResultHandler<T, V> nullInstance() {
        return new TaskResultHandler<T, V>() {
            @Override
            public void done(T result) {
            }
        };
    }

    static void defaultFailed(ExecutionException e) {
        Throwable cause = e.getCause();

        if (cause instanceof RuntimeException) {
            RuntimeException re = (RuntimeException) cause;
            throw re;
        }

        throw new RuntimeException(cause);
    }

    default void publishIntermediateResults(List<V> chunks) {
    }

    void done(T result);

    default void failed(ExecutionException e) {
        defaultFailed(e);
    }

    default void interrupted(InterruptedException e) {
        throw new RuntimeException(e);
    }
}
