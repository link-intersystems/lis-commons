package com.link_intersystems.swing.progress;

public abstract class AbstractProgressListenerDelegate implements ProgressListener {
    public abstract ProgressListener getProgressListener();

    @Override
    public void begin(String name, int totalWork) {
        ProgressListener progressListener = getProgressListener();
        if (progressListener == null) {
            return;
        }
        progressListener.begin(name, totalWork);
    }

    @Override
    public void worked(int worked) {
        ProgressListener progressListener = getProgressListener();
        if (progressListener == null) {
            return;
        }
        progressListener.worked(worked);
    }

    @Override
    public void done() {
        ProgressListener progressListener = getProgressListener();
        if (progressListener == null) {
            return;
        }
        progressListener.done();
    }
}
