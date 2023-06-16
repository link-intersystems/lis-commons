package com.link_intersystems.util.concurrent;

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
