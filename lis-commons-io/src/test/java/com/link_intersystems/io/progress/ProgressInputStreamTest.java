package com.link_intersystems.io.progress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressInputStreamTest {

    private ProgressInputStream inputStream;
    private ProgressListener progressListener;
    private InputStream rawInput;

    @BeforeEach
    void setUp() {
        rawInput = new BufferedInputStream(new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8)));
        progressListener = mock(ProgressListener.class);
        inputStream = new ProgressInputStream(rawInput, progressListener);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(progressListener);
    }

    @Test
    void read() throws IOException {
        int read = inputStream.read();
        assertEquals('H', (char) read);

        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 1));
    }

    @Test
    void readArray() throws IOException {
        byte[] arr = new byte[5];
        int read = inputStream.read(arr);

        assertEquals(5, read);
        assertArrayEquals("Hello".getBytes(StandardCharsets.UTF_8), arr);
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 5));
    }

    @Test
    void skip() throws IOException {
        inputStream.skip(6);
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 6));

        byte[] arr = new byte[5];
        int read = inputStream.read(arr);

        assertEquals(5, read);
        assertArrayEquals("World".getBytes(StandardCharsets.UTF_8), arr);
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 11));
    }

    @Test
    void close() throws IOException {
        inputStream.close();

        assertThrows(IOException.class, () -> rawInput.read());
    }

    @Test
    void reset() throws IOException {
        inputStream.mark(5);

        byte[] arr = new byte[5];
        int read = inputStream.read(arr);
        assertEquals(5, read);
        assertArrayEquals("Hello".getBytes(StandardCharsets.UTF_8), arr);
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 5));

        inputStream.reset();
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 11, 0));

        read = inputStream.read(arr);
        assertEquals(5, read);
        assertArrayEquals("Hello".getBytes(StandardCharsets.UTF_8), arr);
        verify(progressListener, times(2)).progressChanged(new ProgressEvent(inputStream, 0, 11, 5));
    }

    @Test
    void newInstanceWithSize() throws IOException {
        inputStream = new ProgressInputStream(rawInput, 5, progressListener);

        byte[] arr = new byte[5];
        int read = inputStream.read(arr);
        assertEquals(5, read);
        assertArrayEquals("Hello".getBytes(StandardCharsets.UTF_8), arr);
        verify(progressListener).progressChanged(new ProgressEvent(inputStream, 0, 5, 5));
    }

    @Test
    void newInstanceWithWrongSize() {
        assertThrows(IllegalArgumentException.class, () -> new ProgressInputStream(rawInput, -1, progressListener));
    }

    @Test
    void nullProgressListener() throws IOException {
        inputStream = new ProgressInputStream(rawInput, null);
        inputStream.read();
    }
}