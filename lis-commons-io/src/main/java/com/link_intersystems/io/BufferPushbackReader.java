package com.link_intersystems.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * An extension of the {@link PushbackReader} that can return the unread buffer.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BufferPushbackReader extends PushbackReader {

    private int bufferPos = 0;

    public BufferPushbackReader(Reader in) {
        this(in, 1);
    }

    public BufferPushbackReader(Reader in, int size) {
        super(in, size);
    }

    @Override
    public void unread(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            super.unread(cbuf, off, len);
            bufferPos += len;
        }
    }

    @Override
    public void unread(int c) throws IOException {
        synchronized (lock) {
            super.unread(c);
            bufferPos++;
        }
    }

    public char[] getBuffer() throws IOException {
        synchronized (lock) {
            char[] buff = new char[bufferPos];
            read(buff, 0, bufferPos);
            bufferPos = 0;
            unread(buff);
            return buff;
        }
    }

}
