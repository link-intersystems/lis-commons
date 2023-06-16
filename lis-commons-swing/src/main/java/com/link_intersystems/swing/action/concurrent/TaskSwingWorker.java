package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.ProgressListener;
import com.link_intersystems.util.composite.CompositeProxy;
import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskListener;
import com.link_intersystems.util.concurrent.task.TaskProgress;

import javax.swing.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class TaskSwingWorker<T, V> extends SwingWorker<T, V> {

    private class SwingWorkerTaskProgress implements TaskProgress<V> {

        @Override
        public void begin(String name, int totalWork) {
            progressListener.begin(name, totalWork);
        }

        @Override
        public void publish(V... intermediateResults) {
            TaskSwingWorker.this.publish(intermediateResults);
        }

        @Override
        public void worked(int worked) {
            progressListener.worked(worked);
        }

        @Override
        public void done() {
            progressListener.done();
        }
    }

    private final Task<T, V> task;
    private Set<TaskListener<T, V>> taskListeners = new LinkedHashSet<>();
    private Set<ProgressListener> progressListeners = new LinkedHashSet<>();

    private TaskListener<T, V> taskListener = CompositeProxy.create(TaskListener.class, taskListeners);
    private ProgressListener progressListener = CompositeProxy.create(ProgressListener.class, progressListeners);

    public TaskSwingWorker(Task<T, V> task) {
        this.task = requireNonNull(task);
    }

    public void addProgressListener(ProgressListener progressListener) {
        this.progressListeners.add(progressListener);
    }

    public void addTaskListener(TaskListener<T, V> taskListener) {
        this.taskListeners.add(taskListener);
    }

    @Override
    protected T doInBackground() throws Exception {
        return task.execute(new SwingWorkerTaskProgress());
    }

    @Override
    protected void process(List<V> chunks) {
        taskListener.publishIntermediateResults(chunks);
    }

    @Override
    protected void done() {
        try {
            T result = get();
            taskListener.done(result);
        } catch (InterruptedException ex) {
            taskListener.interrupted(ex);
        } catch (ExecutionException ex) {
            taskListener.failed(ex);
        }
    }
}
