package com.link_intersystems.test.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ResetableReaderTest {

    @Test
    void read() throws IOException {
        ResetableReader resetableReader = new ResetableReader();
        resetableReader.setCharSequence("test");

        assertEquals('t', resetableReader.read());
        assertEquals('e', resetableReader.read());
        assertEquals('s', resetableReader.read());
        assertEquals('t', resetableReader.read());
        assertEquals(-1, resetableReader.read());
        resetableReader.close();

        resetableReader.setCharSequence("abc");

        assertEquals('a', resetableReader.read());
        assertEquals('b', resetableReader.read());
        assertEquals('c', resetableReader.read());
        assertEquals(-1, resetableReader.read());
    }


    @Test
    void close() throws IOException {
        ResetableReader resetableReader = new ResetableReader();
        resetableReader.setCharSequence("test");

        assertEquals('t', resetableReader.read());
        assertEquals('e', resetableReader.read());
        assertEquals('s', resetableReader.read());
        assertEquals('t', resetableReader.read());
        assertEquals(-1, resetableReader.read());

        resetableReader.close();

        assertEquals(-1, resetableReader.read());
    }
}