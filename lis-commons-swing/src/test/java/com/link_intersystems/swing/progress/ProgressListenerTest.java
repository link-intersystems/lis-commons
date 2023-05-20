package com.link_intersystems.swing.progress;

import org.junit.jupiter.api.Test;

class ProgressListenerTest {

    @Test
    void nullInstance() {
        ProgressListener progressListener = ProgressListener.nullInstance();
        progressListener.begin("A", 2);
        progressListener.worked(1);
        progressListener.done();
    }
}