package com.link_intersystems.swing.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TaskResultHandler<T, V> {

    static <T, V> TaskResultHandler<T, V> nullInstance() {
        return result -> {
        };
    }

    static void defaultFailed(Throwable e) {
        Throwable cause = e;
        if (e instanceof ExecutionException) {
            cause = e.getCause();
        }

        if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
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

    default void aborted(Exception e) {
    }
}
