package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.task.TaskListener;

public interface TaskActionListener<T, V> extends TaskListener<T, V> {

    default void aboutToRun() {
    }

    default void doFinally() {
    }
}
