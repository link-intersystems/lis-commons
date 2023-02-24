package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.*;

public class SwingWorkerBackgroundWorkExecutor implements BackgroundWorkExecutor {

    @Override
    public <T, V> void execute(BackgroundWork<T, V> backgroundWork, BackgroundWorkResultHandler<T, V> lifecycle, ProgressListener progressListener) {
        SwingWorker<T, V> swingWorker = new SwingWorkerAdapter<>(backgroundWork, lifecycle, progressListener);
        swingWorker.execute();
    }

    private class SwingWorkerAdapter<T, V> extends SwingWorker<T, V> {
        private final BackgroundWork<T, V> backgroundWork;
        private final BackgroundWorkResultHandler<T, V> resultHandler;
        private ProgressListener progressListener;

        public SwingWorkerAdapter(BackgroundWork<T, V> backgroundWork, BackgroundWorkResultHandler<T, V> resultHandler, ProgressListener progressListener) {
            this.backgroundWork = requireNonNull(backgroundWork);
            this.resultHandler = requireNonNull(resultHandler);
            this.progressListener = requireNonNull(progressListener);
        }

        @Override
        protected T doInBackground() throws Exception {
            return backgroundWork.execute(new BackgroundProgress<V>() {

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
