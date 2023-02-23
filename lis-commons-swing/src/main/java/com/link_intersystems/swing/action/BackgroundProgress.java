package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

public interface BackgroundProgress<V> extends ProgressListener {
    public void publish(V... intermediateResults);
}
