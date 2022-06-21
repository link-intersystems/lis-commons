package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpHeaderTest {

    private HttpHeader acceptPlainAndHtml;

    @BeforeEach
    void setUp() {
        acceptPlainAndHtml = new HttpHeader("Accept", asList("text/plain", "text/html"));
    }


    @Test
    void nullHeaderName() {
        assertThrows(IllegalArgumentException.class, () -> new HttpHeader(null, asList("text/plain", "text/html")));
    }


    @Test
    void nullHeaderValues() {
        assertThrows(IllegalArgumentException.class, () -> new HttpHeader("Accept", (List<String>) null));
    }

    @Test
    void emptyHeaderValues() {
        assertThrows(IllegalArgumentException.class, () -> new HttpHeader("Accept", Collections.emptyList()));
    }

    @Test
    void multiValueHeaderTrimmed() {
        HttpHeader httpHeader = new HttpHeader("Accept", asList("  text/plain", "text/html   "));

        assertEquals(acceptPlainAndHtml, httpHeader);
    }

    @Test
    void toStringTest() {
        HttpHeader httpHeader = new HttpHeader("Accept", asList("  text/plain", "text/html   "));

        assertEquals("Accept: text/plain, text/html", httpHeader.toString());
    }
}