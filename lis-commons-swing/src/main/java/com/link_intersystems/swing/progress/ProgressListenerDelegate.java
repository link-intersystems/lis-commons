package com.link_intersystems.swing.progress;

public class ProgressListenerDelegate extends AbstractProgressListenerDelegate {

    private ProgressListener progressListener;

    public ProgressListenerDelegate(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public ProgressListener getProgressListener() {
        return progressListener;
    }

}
