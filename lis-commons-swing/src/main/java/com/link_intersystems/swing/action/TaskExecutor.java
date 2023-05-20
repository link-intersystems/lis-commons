package com.link_intersystems.swing.action;

import com.link_intersystems.swing.progress.ProgressListener;

public interface TaskExecutor {
    <T, V> void execute(Task<T, V> task, TaskResultHandler<T, V> resultHandler, ProgressListener progressListener) throws Exception;
}
