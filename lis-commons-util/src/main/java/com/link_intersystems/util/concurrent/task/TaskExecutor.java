package com.link_intersystems.util.concurrent.task;


import com.link_intersystems.util.concurrent.ProgressListener;

public interface TaskExecutor {
    <T, V> void execute(Task<T, V> task, TaskListener<T, V> resultHandler, ProgressListener progressListener) throws Exception;
}
