package com.link_intersystems.util.concurrent;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ProgressListener {

    public void begin(int totalWork);

    public void worked(int work);

    public void done();
}
