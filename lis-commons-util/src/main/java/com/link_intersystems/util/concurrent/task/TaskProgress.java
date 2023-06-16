package com.link_intersystems.util.concurrent.task;


import com.link_intersystems.util.concurrent.ProgressListener;

public interface TaskProgress<V> extends ProgressListener {
    public void publish(V... intermediateResults);
}
