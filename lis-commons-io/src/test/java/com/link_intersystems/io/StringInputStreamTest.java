package com.link_intersystems.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class StringInputStreamTest {

    @Test
    void read() throws IOException {
        String str = "öäüß";

        try (InputStream inputStream = new StringInputStream(str)) {
            byte[] bytes = str.getBytes(UTF_8);

            for (int i = 0; i < bytes.length; i++) {
                byte aByte = bytes[i];
                assertEquals(aByte, inputStream.read());
            }

            assertEquals(-1, inputStream.read());
            assertEquals(-1, inputStream.read());
        }
    }

    @Test
    void readClosedStream() throws IOException {
        InputStream inputStream = new StringInputStream("öäüß");
        inputStream.close();

        assertThrows(IOException.class, () -> inputStream.read());
    }

    @Test
    void markSupported() throws IOException {
        try (InputStream in = new StringInputStream("Hello World")) {
            assertTrue(in.markSupported());
        }
    }

    @Test
    void markAndReset() throws IOException {
        try (InputStream in = new StringInputStream("Hello World")) {

            in.mark(5);
            byte[] bytes = new byte[5];

            in.read(bytes);
            assertArrayEquals("Hello".getBytes(UTF_8), bytes);

            in.reset();

            in.read(bytes);
            assertArrayEquals("Hello".getBytes(UTF_8), bytes);
        }
    }

    @Test
    void multipleMarks() throws IOException {
        try (InputStream in = new StringInputStream("Hello World")) {

            in.mark(2);
            byte[] bytes = new byte[2];

            in.read(bytes);
            assertArrayEquals("He".getBytes(UTF_8), bytes);
            in.reset();

            in.read(bytes);
            assertArrayEquals("He".getBytes(UTF_8), bytes);

            in.read(bytes);
            assertArrayEquals("ll".getBytes(UTF_8), bytes);

            bytes = new byte[5];
            in.mark(5);
            in.read(bytes);
            assertArrayEquals("o Wor".getBytes(UTF_8), bytes);
        }
    }

    @Test
    void readlimitExceeded() throws IOException {
        try (InputStream in = new StringInputStream("Hello World")) {

            in.mark(4);
            byte[] bytes = new byte[5];

            in.read(bytes);
            assertArrayEquals("Hello".getBytes(UTF_8), bytes);

            assertThrows(IOException.class, () -> in.reset());
        }
    }

    @Test
    void resetInitialStream() throws IOException {
        try (InputStream in = new StringInputStream("Hello World")) {
            assertThrows(IOException.class, in::reset);
        }
    }
}