package com.link_intersystems.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * An {@link InputStream} adapter for a {@link CharSequence}.Even though this class is based on the {@link CharSequence} interface,
 * the is named {@link StringInputStream}, because almost  noone  would find it if it would be named CharSequenceInputStream.
 */
public class StringInputStream extends InputStream {
    private CharBuffer charBuffer = CharBuffer.allocate(1);
    private ByteBuffer byteBuffer = ByteBuffer.allocate(0);

    private int pos = 0;
    private CharSequence charSequence;

    private Charset charset;
    private int readLimitPos = -1;
    private int resetPos = -1;

    public StringInputStream(CharSequence charSequence) {
        this(charSequence, UTF_8);
    }

    public StringInputStream(CharSequence charSequence, Charset charset) {
        this.charSequence = requireNonNull(charSequence);
        this.charset = requireNonNull(charset);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.readLimitPos = pos + readlimit;
        this.resetPos = pos;
    }

    @Override
    public synchronized void reset() throws IOException {
        if (readLimitPos == -1) {
            throw new IOException("Stream not marked.");
        }

        if (pos > readLimitPos) {
            throw new IOException("Read limit exceeded.");
        }

        pos = resetPos;
        byteBuffer = ByteBuffer.allocate(0);
    }

    @Override
    public int read() throws IOException {
        try {
            if (!byteBuffer.hasRemaining()) {
                int charAt = readChar();
                if (charAt == -1) {
                    return -1;
                }

                charBuffer.put((char) charAt);
                charBuffer.flip();
                byteBuffer = charset.encode(charBuffer);
                charBuffer.flip();
            }

            return (int) byteBuffer.get();
        } catch (NullPointerException e) {
            throw new IOException("Stream closed.");
        }
    }

    private int readChar() {
        if (pos < charSequence.length()) {
            return charSequence.charAt(pos++);
        }
        return -1;
    }

    @Override
    public void close() throws IOException {
        charSequence = null;
        byteBuffer = null;
        charBuffer = null;
    }
}
