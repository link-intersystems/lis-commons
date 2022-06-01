package com.link_intersystems.test.io;

import java.io.IOException;
import java.io.Reader;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ResetableReader extends Reader {

    private int index = -1;
    private CharSequence charSequence;

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
        index = 0;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (index == -1 || index >= charSequence.length()) {
            return -1;
        }

        int read = 0;

        while (read < len && index < charSequence.length()) {
            cbuf[off + read++] = charSequence.charAt(index++);
        }

        return read;
    }

    @Override
    public void close() throws IOException {
        index = -1;
    }
}
