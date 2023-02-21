package com.link_intersystems.swing;

public interface ProgressListener {
    static ProgressListener nullInstance() {
        return new ProgressListener() {
            @Override
            public void begin(int totalWork) {
            }

            @Override
            public void worked(int worked) {
            }
        };
    }

    void begin(int totalWork);

    void worked(int worked);

}
