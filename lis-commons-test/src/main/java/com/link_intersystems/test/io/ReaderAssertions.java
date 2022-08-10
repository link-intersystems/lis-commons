package com.link_intersystems.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ReaderAssertions {

    private BufferedReader reader;

    public ReaderAssertions(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            fail(e);
            return null;
        }
    }

    public void assertEmptyLine() {
        assertLine("");
    }

    public void assertNoLine() {
        assertLine(Objects::isNull);
    }

    public void assertLine(String expectedLine) {
        String line = readLine();
        assertEquals(expectedLine, line);
    }


    public void assertLineMatches(String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        String line = readLine();
        assertNotNull(line, () -> "No line available to match '" + regexp + "'");
        Matcher matcher = pattern.matcher(line);
        assertTrue(matcher.matches(), () -> "'" + line + "' should match '" + regexp + "'");
    }

    public void assertLineContains(String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        String line = readLine();
        assertNotNull(line, () -> "No line available to match '" + regexp + "'");
        Matcher matcher = pattern.matcher(line);
        assertTrue(matcher.find(), () -> "'" + line + "' should contain '" + regexp + "'");
    }

    public void assertLine(Predicate<String> linePredicate) {
        String line = readLine();
        assertTrue(linePredicate.test(line), () -> "'" + line + "' did not match the predicate '" + linePredicate + "'");
    }
}
