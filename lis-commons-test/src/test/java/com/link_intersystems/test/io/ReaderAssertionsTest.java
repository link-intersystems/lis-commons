package com.link_intersystems.test.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ReaderAssertionsTest {


    @Test
    void failOnIOExceptio() throws IOException {
        Reader reader = mock(Reader.class);
        when(reader.read(any(), anyInt(), anyInt())).thenThrow(new IOException());

        ReaderAssertions readerAssertions = new ReaderAssertions(reader);
        assertThrows(AssertionError.class, readerAssertions::assertEmptyLine);
    }


    @Test
    void assertEmptyLine() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader("\n"));
        readerAssertions.assertEmptyLine();
    }

    @Test
    void assertNoLine() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader(""));
        readerAssertions.assertNoLine();
    }

    @Test
    void assertLine() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader("Hello\nWorld"));
        readerAssertions.assertLine("Hello");
        readerAssertions.assertLine("World");
    }

    @Test
    void assertLineMatches() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader("ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console..."));
        readerAssertions.assertLineMatches("^ERROR .* log4j-core .*$");
    }

    @Test
    void assertLineMatchesNoLine() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader(""));

        assertThrows(AssertionError.class, () -> readerAssertions.assertLineMatches("^ERROR .* log4j-core .*$"));
    }

    @Test
    void assertLineContains() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader("ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console..."));
        readerAssertions.assertLineContains(" log4j-[a-z]{4}");
    }

    @Test
    void assertLineContainsNoLine() {
        ReaderAssertions readerAssertions = new ReaderAssertions(new StringReader(""));
        assertThrows(AssertionError.class, () -> readerAssertions.assertLineContains(" log4j-[a-z]{4}"));
    }
}