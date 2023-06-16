package com.link_intersystems.swing.progress;

import com.link_intersystems.util.concurrent.AbstractProgressListenerDelegate;
import com.link_intersystems.util.concurrent.ProgressListener;
import com.link_intersystems.util.concurrent.ProgressListenerDelegate;

class ProgressListenerDelegateTest extends AbstractProgressListenerDelegateTest {

    @Override
    protected AbstractProgressListenerDelegate createInstance(ProgressListener delegate) {
        return new ProgressListenerDelegate(delegate);
    }
}