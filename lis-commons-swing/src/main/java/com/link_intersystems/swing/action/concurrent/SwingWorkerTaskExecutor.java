package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.ProgressListener;
import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskExecutor;
import com.link_intersystems.util.concurrent.task.TaskListener;

public class SwingWorkerTaskExecutor implements TaskExecutor {

    @Override
    public <T, V> void execute(Task<T, V> task, TaskListener<T, V> taskListener, ProgressListener progressListener) {
        TaskSwingWorker<T, V> swingWorker = new TaskSwingWorker<>(task);
        swingWorker.addProgressListener(progressListener);
        swingWorker.addTaskListener(taskListener);
        swingWorker.execute();
    }

}
