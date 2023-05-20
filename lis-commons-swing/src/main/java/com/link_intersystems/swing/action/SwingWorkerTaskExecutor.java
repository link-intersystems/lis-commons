package com.link_intersystems.swing.action;

import com.link_intersystems.swing.progress.ProgressListener;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class SwingWorkerTaskExecutor implements TaskExecutor {

    @Override
    public <T, V> void execute(Task<T, V> task, TaskResultHandler<T, V> lifecycle, ProgressListener progressListener) {
        SwingWorker<T, V> swingWorker = new SwingWorkerAdapter<>(task, lifecycle, progressListener);
        swingWorker.execute();
    }

    private class SwingWorkerAdapter<T, V> extends SwingWorker<T, V> {
        private final Task<T, V> task;
        private final TaskResultHandler<T, V> resultHandler;
        private ProgressListener progressListener;

        public SwingWorkerAdapter(Task<T, V> task, TaskResultHandler<T, V> resultHandler, ProgressListener progressListener) {
            this.task = requireNonNull(task);
            this.resultHandler = requireNonNull(resultHandler);
            this.progressListener = requireNonNull(progressListener);
        }

        @Override
        protected T doInBackground() throws Exception {
            return task.execute(new TaskProgress<V>() {

                @Override
                public void begin(String name, int totalWork) {
                    progressListener.begin(name, totalWork);
                }

                @Override
                public void publish(V... intermediateResults) {
                    if (intermediateResults.length == 0) {
                        return;
                    }

                    SwingWorkerAdapter.this.publish(intermediateResults);
                }

                @Override
                public void worked(int worked) {
                    progressListener.worked(worked);
                }

                @Override
                public void done() {
                    progressListener.done();
                }
            });
        }

        @Override
        protected void process(List<V> chunks) {
            resultHandler.publishIntermediateResults(chunks);
        }

        @Override
        protected void done() {
            try {
                T result = get();
                resultHandler.done(result);
            } catch (InterruptedException ex) {
                resultHandler.interrupted(ex);
            } catch (ExecutionException ex) {
                resultHandler.failed(ex);
            }
        }
    }
}
