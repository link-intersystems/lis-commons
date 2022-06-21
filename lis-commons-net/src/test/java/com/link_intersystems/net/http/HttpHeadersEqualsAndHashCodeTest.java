package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpHeadersEqualsAndHashCodeTest {

    private HttpHeaders httpHeaders;
    private HttpHeaders equalHeaders;
    private HttpHeaders notEqualHeaders;

    @BeforeEach
    void setUp() {

        httpHeaders = new HttpHeaders();
        httpHeaders.add("ACCEPT", "text/plain,text/html");

        equalHeaders = new HttpHeaders();
        equalHeaders.add("accept", "text/plain,text/html");

        notEqualHeaders = new HttpHeaders();
        notEqualHeaders.add("accept", "TEXT/PLAIN,TEXT/HTML");
    }

    /**
     * The RFC-2616 states:
     *
     * <pre>
     *     4.2 Message Headers
     *
     *    ... Field names are case-insensitive.
     * </pre>
     */
    @Test
    void equalIgnoreCase() {
        assertEquals(httpHeaders, equalHeaders);
        assertEquals(httpHeaders.hashCode(), equalHeaders.hashCode());
    }

    @Test
    void notEqualIgnoreCase() {
        assertNotEquals(httpHeaders, notEqualHeaders);
        assertNotEquals(httpHeaders.hashCode(), notEqualHeaders.hashCode());
    }
}