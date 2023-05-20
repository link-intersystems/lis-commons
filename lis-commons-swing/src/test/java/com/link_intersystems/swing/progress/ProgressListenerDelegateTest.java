package com.link_intersystems.swing.progress;

class ProgressListenerDelegateTest extends AbstractProgressListenerDelegateTest {

    @Override
    protected AbstractProgressListenerDelegate createInstance(ProgressListener delegate) {
        return new ProgressListenerDelegate(delegate);
    }
}