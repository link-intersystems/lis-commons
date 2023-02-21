package com.link_intersystems.swing.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AsycWorkLifecycle<T, V> {

    static <T, V> AsycWorkLifecycle<T, V> nullInstance() {
        return new AsycWorkLifecycle<T, V>() {
        };
    }

    default void prepareForExecution() {
    }

    default void intermediateResults(List<V> chunks) {
    }

    default void done(T result) {
    }

    default void failed(ExecutionException e) {
        throw new RuntimeException(e);
    }

    default void interrupted(InterruptedException e) {
        throw new RuntimeException(e);
    }
}
