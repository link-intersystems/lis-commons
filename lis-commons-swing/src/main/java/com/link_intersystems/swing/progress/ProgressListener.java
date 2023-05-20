package com.link_intersystems.swing.progress;

public interface ProgressListener {
    static ProgressListener nullInstance() {
        return new ProgressListener() {
            @Override
            public void begin(String name, int totalWork) {
            }

            @Override
            public void worked(int worked) {
            }

            @Override
            public void done() {
            }
        };
    }

    void begin(String name, int totalWork);

    void worked(int worked);

    void done();

}
