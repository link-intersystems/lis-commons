package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

public interface AsyncWorkExecutor {
    <T, V> void execute(AsycWork<T, V> asycWork, AsycWorkLifecycle<T, V> lifecycle, ProgressListener progressListener);
}
