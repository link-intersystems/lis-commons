package com.link_intersystems.swing.action.concurrent;

import com.link_intersystems.util.concurrent.ProgressListener;
import com.link_intersystems.util.concurrent.task.Task;
import com.link_intersystems.util.concurrent.task.TaskExecutor;
import com.link_intersystems.util.concurrent.task.TaskListener;
import com.link_intersystems.util.concurrent.task.TaskProgress;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DirectTaskExecutor implements TaskExecutor {
    @Override
    public <T, V> void execute(Task<T, V> task, TaskListener<T, V> resultHandler, ProgressListener progressListener) {
        try {
            TaskProgress<V> taskProgress = new TaskProgress<V>() {

                private boolean done;

                @Override
                public void publish(V... intermediateResults) {
                    resultHandler.publishIntermediateResults(Arrays.asList(intermediateResults));
                }

                @Override
                public void begin(String name, int totalWork) {
                    progressListener.begin(name, totalWork);
                }

                @Override
                public void worked(int worked) {
                    progressListener.worked(worked);
                }

                @Override
                public void done() {
                    progressListener.done();
                }
            };
            T result = task.execute(taskProgress);

            resultHandler.done(result);
        } catch (Exception e) {
            resultHandler.failed(new ExecutionException(e));
        }
    }
}
