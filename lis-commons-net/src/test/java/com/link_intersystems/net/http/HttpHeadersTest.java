package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpHeadersTest {

    private HttpHeaders httpHeaders;

    @BeforeEach
    void setUp() {
        httpHeaders = new HttpHeaders();
    }

    @Test
    void createFromOtherHeaders() {
        httpHeaders.put("Accept", "text/plain,text/html");

        assertEquals(httpHeaders, new HttpHeaders(httpHeaders));
    }

    @Test
    void putNullHeaderName() {
        assertThrows(IllegalArgumentException.class, () -> httpHeaders.put(null, asList("text/plain", "text/html")));
    }

    @Test
    void putNullHeaderValue() {
        assertThrows(IllegalArgumentException.class, () -> httpHeaders.put("Accept", (String) null));
    }

    @Test
    void putNullHeaderValues() {
        assertThrows(IllegalArgumentException.class, () -> httpHeaders.put("Accept", (List<String>) null));
    }

    @Test
    void putEmptyHeaderValues() {
        assertThrows(IllegalArgumentException.class, () -> httpHeaders.put("Accept", Collections.emptyList()));
    }

    @Test
    void multiValueHeaderAsString() {
        httpHeaders.put("Accept", "text/plain,text/html");

        assertEquals(1, httpHeaders.size());
        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("Accept"));
    }

    @Test
    void multiValueHeaderAsStringTrimmed() {
        httpHeaders.put("Accept", "  text/plain  ,   text/html   ");

        assertEquals(1, httpHeaders.size());
        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("Accept"));
    }

    @Test
    void putMultiValueHeaderListTrimmed() {
        httpHeaders.put("Accept", asList("  text/plain", "text/html   "));

        assertEquals(1, httpHeaders.size());
        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("Accept"));
    }


    /**
     * RFC 2616
     * <pre>
     *    Each header field consists
     *    of a name followed by a colon (":") and the field value. Field names
     *    are case-insensitive.
     * </pre>
     */
    @Test
    void getCaseInsensitive() {
        httpHeaders.put("Accept", asList("  text/plain"));
        httpHeaders.put("ACCEPT", asList("text/html   "));

        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("ACCEPT"));
    }

    @Test
    void getNonStringKey() {
        httpHeaders.put("Accept", asList("  text/plain", "text/html   "));

        assertNull(httpHeaders.get(1L));
    }

    @Test
    void getOnEmptyHeaders() {
        assertNull(httpHeaders.get("Accept"));
    }

    /**
     * From the RFC-2616:
     * <pre>
     *     It MUST be possible to combine the multiple header fields into one
     *    "field-name: field-value" pair, without changing the semantics of the
     *    message, by appending each subsequent field-value to the first, each
     *    separated by a comma.
     * </pre>
     */
    @Test
    void joinHeaders() {
        httpHeaders.put("Accept", asList("text/plain"));
        httpHeaders.put("Accept", "text/html");

        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("ACCEPT"));
    }

    /**
     * RFC 2616
     * <pre>
     *    Each header field consists
     *    of a name followed by a colon (":") and the field value. Field names
     *    are case-insensitive.
     * </pre>
     */
    @Test
    void joinHeadersCaseInsensitive() {
        httpHeaders.put("accept", asList("text/plain"));
        httpHeaders.put("ACCEPT", "text/html");

        assertEquals(asList("text/plain", "text/html"), httpHeaders.get("ACCEPT"));
    }
}