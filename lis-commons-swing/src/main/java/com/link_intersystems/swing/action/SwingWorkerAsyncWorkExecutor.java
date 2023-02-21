package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SwingWorkerAsyncWorkExecutor implements AsyncWorkExecutor {

    @Override
    public <T, V> void execute(AsycWork<T, V> asycWork, AsycWorkLifecycle<T, V> lifecycle, ProgressListener progressListener) {
        SwingWorker<T, V> swingWorker = new SwingWorkerAdapter<>(asycWork, lifecycle, progressListener);
        swingWorker.execute();
    }

    private class SwingWorkerAdapter<T, V> extends SwingWorker<T, V> {
        private final AsycWork<T, V> asycWork;
        private final AsycWorkLifecycle<T, V> lifecycle;
        private ProgressListener progressListener;

        public SwingWorkerAdapter(AsycWork<T, V> asycWork, AsycWorkLifecycle<T, V> lifecycle, ProgressListener progressListener) {
            this.asycWork = asycWork;
            this.lifecycle = lifecycle;
            this.progressListener = progressListener;
        }

        @Override
        protected T doInBackground() throws Exception {
            return asycWork.execute(new AsyncProgress<V>() {

                @Override
                public void begin(int totalWork) {
                    progressListener.begin(totalWork);
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
            lifecycle.intermediateResults(chunks);
        }

        @Override
        protected void done() {
            try {
                T result = get();
                lifecycle.done(result);
            } catch (InterruptedException ex) {
                lifecycle.interrupted(ex);
            } catch (ExecutionException ex) {
                lifecycle.failed(ex);
            }
        }
    }
}
