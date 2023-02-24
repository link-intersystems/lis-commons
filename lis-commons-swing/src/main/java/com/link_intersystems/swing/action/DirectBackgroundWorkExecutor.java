package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DirectBackgroundWorkExecutor implements BackgroundWorkExecutor {
    @Override
    public <T, V> void execute(BackgroundWork<T, V> backgroundWork, BackgroundWorkResultHandler<T, V> resultHandler, ProgressListener progressListener) {
        try {
            T result = backgroundWork.execute(new BackgroundProgress<V>() {
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
