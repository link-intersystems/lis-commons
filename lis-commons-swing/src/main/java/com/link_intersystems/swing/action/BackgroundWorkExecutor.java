package com.link_intersystems.swing.action;

import com.link_intersystems.swing.ProgressListener;

public interface BackgroundWorkExecutor {
    <T, V> void execute(BackgroundWork<T, V> backgroundWork, BackgroundWorkResultHandler<T, V> resultHandler, ProgressListener progressListener) throws Exception;
}
