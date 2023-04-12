package com.link_intersystems.io.progress;


import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends FilterInputStream {
    private int read = 0;
    private int size;
    private ProgressListener progressListener;

    public ProgressInputStream(InputStream in, int size, ProgressListener progressListener) {
        super(in);

        if (size < 0) {
            throw new IllegalArgumentException("size must be 0 or greater");
        }

        this.size = size;
        this.progressListener = ProgressListener.nullSafe(progressListener);
    }

    public ProgressInputStream(InputStream in, ProgressListener progressListener) {
        super(in);

        try {
            size = in.available();
        } catch (IOException var3) {
            size = 0;
        }

        this.progressListener = ProgressListener.nullSafe(progressListener);
    }

    public int read() throws IOException {
        int c = in.read();

        if (c >= 0) {
            fireProgressEvent(new ProgressEvent(this, 0, size, ++read));
        }

        return c;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int nr = in.read(b, off, len);

        if (nr > 0) {
            fireProgressEvent(new ProgressEvent(this, 0, size, read += nr));
        }

        return nr;
    }

    public long skip(long n) throws IOException {
        long nr = in.skip(n);

        if (nr > 0L) {
            read = (int) ((long) read + nr);
            fireProgressEvent(new ProgressEvent(this, 0, size, read));
        }

        return nr;
    }

    public void close() throws IOException {
        in.close();
    }

    public synchronized void reset() throws IOException {
        in.reset();
        fireProgressEvent(new ProgressEvent(this, 0, size, read = size - in.available()));
    }

    private void fireProgressEvent(ProgressEvent e) {
        fireProgressEvent(progressListener, e);
    }

    protected void fireProgressEvent(ProgressListener progressListener, ProgressEvent e) {
        progressListener.progressChanged(e);
    }
}
