package com.link_intersystems.util.concurrent;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NullProgressListener implements ProgressListener {

    public static final NullProgressListener INSTANCE = new NullProgressListener();

    @Override
    public void begin(int totalWork) {
    }

    @Override
    public void worked(int work) {
    }

    @Override
    public void done() {
    }

}
