package com.link_intersystems.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DefaultCharSequenceDetectorTest {

    private CharSequenceDetector streamSequenceDetector;

    @BeforeEach
    void setUp() {
        streamSequenceDetector = new CharSequenceDetector();
    }


    @Test
    void detectEmptyCharSequence() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader("hello"), 14);
        String sequence = "";

        assertFalse(streamSequenceDetector.detect(reader, sequence));
    }

    @Test
    void detectEmptyCharSequenceOnEmptyInput() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader(""), 14);
        String sequence = "";

        assertTrue(streamSequenceDetector.detect(reader, sequence));
    }


    @Test
    void detect() throws IOException {
        PushbackReader reader = new PushbackReader(new StringReader("<artifactId>${artifactId}</artifactId>"), 14);
        String sequence = "${artifactId}";

        assertFalse(streamSequenceDetector.detect(reader, sequence));

        assertEquals("<artifactId>", readNextChars(reader, 12));

        assertTrue(streamSequenceDetector.detect(reader, sequence));

        assertEquals("</artifactId>", readNextChars(reader, 13));
        assertFalse(streamSequenceDetector.detect(reader, sequence));
    }

    private String readNextChars(Reader reader, int chars) throws IOException {
        char[] nextChars = new char[chars];
        int read = reader.read(nextChars);
        assertEquals(chars, read, "read chars");
        return new String(nextChars, 0, read);
    }

    @Test
    void detectSingleChar() throws IOException {
        String sequence = "}";
        PushbackReader reader = new PushbackReader(new StringReader("<artifactId>${artifactId}</artifactId>"), 14);
        assertFalse(streamSequenceDetector.detect(reader, sequence));

        assertEquals("<artifactId>", readNextChars(reader, 12));

        assertFalse(streamSequenceDetector.detect(reader, sequence));
        readNextChars(reader, 12);
        assertTrue(streamSequenceDetector.detect(reader, sequence));

        assertEquals("</artifactId>", readNextChars(reader, 13));
        assertFalse(streamSequenceDetector.detect(reader, sequence));
    }

    @Test
    void detectInLargeStream() throws IOException {
        int pushbackSize = 100;
        Reader resourceReader = readClasspathResource("DefaultCharSequenceDetector.txt");

        int pos = 0;
        CharSequenceDetector sequenceDetector = new CharSequenceDetector();

        try (PushbackReader reader = new PushbackReader(resourceReader, pushbackSize)) {
            while (reader.ready()) {
                if (sequenceDetector.detect(reader, "49cb-a14e")) {
                    break;
                }
                reader.read();
                pos++;
            }
        }

        int line = 800;
        int uuidLength = 36;
        int newLinesLength = line - 1;
        int posOfSequenceWithLine = 14;
        int expectedPos = (line - 1) * uuidLength + newLinesLength + posOfSequenceWithLine;

        assertEquals(expectedPos, pos);
    }

    private BufferedReader readClasspathResource(String resource) {
        return new BufferedReader(new InputStreamReader(
                DefaultCharSequenceDetectorTest.class.getResourceAsStream(resource),
                StandardCharsets.UTF_8));
    }
}