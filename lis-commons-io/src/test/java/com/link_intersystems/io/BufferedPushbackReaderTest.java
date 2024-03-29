package com.link_intersystems.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BufferedPushbackReaderTest {

    private BufferPushbackReader pushbackReader;
    private char[] buff;
    private StringBuilder sb;
    private Reader sourceReader;

    @BeforeEach
    void setUp() {
        sourceReader = new StringReader("Hello World");
        pushbackReader = new BufferPushbackReader(sourceReader, 10);

        buff = new char[5];

        sb = new StringBuilder();
    }

    @AfterEach
    void tearDown() throws IOException {
        pushbackReader.close();

        assertThrows(IOException.class, () -> sourceReader.read());
    }

    @Test
    void pushBackChar() throws IOException {
        assertEquals(5, pushbackReader.read(buff));

        pushbackReader.unread('i');
        pushbackReader.unread('H');

        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);
        assertEquals(3, pushbackReader.read(buff));
        sb.append(buff, 0, 3);

        assertEquals("Hi World", sb.toString());
    }

    @Test
    void pushBackCharBuffer() throws IOException {
        assertEquals(5, pushbackReader.read(buff));

        pushbackReader.unread("Hi".toCharArray());

        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);
        assertEquals(3, pushbackReader.read(buff));
        sb.append(buff, 0, 3);

        assertEquals("Hi World", sb.toString());
    }

    @Test
    void pushBackCharBufferPart() throws IOException {
        assertEquals(5, pushbackReader.read(buff));

        pushbackReader.unread(" Hi ".toCharArray(), 1, 2);

        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);
        assertEquals(3, pushbackReader.read(buff));
        sb.append(buff, 0, 3);

        assertEquals("Hi World", sb.toString());
    }

    @Test
    void noPushBack() throws IOException {
        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);

        assertEquals(' ', (char) pushbackReader.read());
        sb.append(' ');

        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);

        assertEquals("Hello World", sb.toString());
    }

    @Test
    void readOnlyPushback() throws IOException {
        assertEquals(5, pushbackReader.read(buff));
        assertEquals(' ', (char) pushbackReader.read());
        assertEquals(5, pushbackReader.read(buff));
        pushbackReader.unread(buff);
        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);

        assertEquals("World", sb.toString());
    }

    @Test
    void getBuffer() throws IOException {
        assertEquals(5, pushbackReader.read(buff));
        assertEquals(' ', (char) pushbackReader.read());
        assertEquals(5, pushbackReader.read(buff));
        pushbackReader.unread(buff);


        assertArrayEquals("World".toCharArray(), pushbackReader.getBuffer());

        assertEquals(5, pushbackReader.read(buff));
        sb.append(buff);

        assertEquals("World", sb.toString());
    }

}