package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DirectTaskExecutor implements TaskExecutor {
    @Override
    public <T, V> void execute(Task<T, V> task, TaskResultHandler<T, V> resultHandler, ProgressListener progressListener) {
        try {
            T result = task.execute(new TaskProgress<V>() {
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
            });
            resultHandler.done(result);
        } catch (Exception e) {
            resultHandler.failed(new ExecutionException(e));
        }
    }
}