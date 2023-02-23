package com.link_intersystems.swing.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BackgroundWorkResultHandler<T, V> {

    static <T, V> BackgroundWorkResultHandler<T, V> nullInstance() {
        return new BackgroundWorkResultHandler<T, V>() {
            @Override
            public void done(T result) {
            }
        };
    }

    default void publishIntermediateResults(List<V> chunks) {
    }

    void done(T result);

    default void failed(ExecutionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
            RuntimeException re = (RuntimeException) cause;
            throw re;
        }

        throw new RuntimeException(cause);
    }

    default void interrupted(InterruptedException e) {
        throw new RuntimeException(e);
    }
}
