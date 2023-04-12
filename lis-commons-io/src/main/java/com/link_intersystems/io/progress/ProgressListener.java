package com.link_intersystems.io.progress;

import java.util.EventListener;

public interface ProgressListener extends EventListener {
    ProgressListener NULL = pe -> {
    };

    static ProgressListener nullSafe(ProgressListener progressListener) {
        return progressListener == null ? NULL : progressListener;
    }

    void progressChanged(ProgressEvent progressEvent);
}