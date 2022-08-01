package com.link_intersystems.io;

import java.io.IOException;
import java.io.Reader;

import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PushbackReader extends Reader {

    private Reader reader;
    private StringBuilder pushback = new StringBuilder();

    public PushbackReader(Reader reader) {
        this.reader = requireNonNull(reader);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int pushbackRead = 0;

        if (pushback.length() > 0) {
            pushbackRead = min(len, pushback.length());

            pushback.getChars(0, pushbackRead, cbuf, off);
            deletePushbackBuffer(pushback);

            if (len == pushbackRead) return pushbackRead;
        }

        return reader.read(cbuf, off + pushbackRead, len - pushbackRead) + pushbackRead;
    }

    protected void deletePushbackBuffer(StringBuilder pushback) {
        pushback.delete(0, pushback.length());
    }

    public void pushback(CharSequence charSequence) {
        pushback.append(charSequence);
    }

    public void pushback(char c) {
        pushback.append(c);
    }

    public void pushback(char[] buf) {
        pushback.append(buf);
    }

    public void pushback(char[] buf, int off, int len) {
        pushback.append(buf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
