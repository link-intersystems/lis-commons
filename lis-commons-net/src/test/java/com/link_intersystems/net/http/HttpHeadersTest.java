package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpHeadersTest {

    private HttpHeaders httpHeaders;
    private HttpHeader acceptPlainAndHtml;

    @BeforeEach
    void setUp() {
        httpHeaders = new HttpHeaders();

        httpHeaders.add("Accept", "text/plain");
        httpHeaders.add("Accept", "text/html");

        acceptPlainAndHtml = new HttpHeader("Accept", asList("text/plain", "text/html"));

    }

    @Test
    void createFromOtherHeaders() {
        httpHeaders.add("Accept", "text/plain,text/html");

        assertEquals(httpHeaders, new HttpHeaders(httpHeaders));
    }

    @Test
    void setHeader() {
        httpHeaders.add("Accept", "text/plain,text/html");
        httpHeaders.add("Expires", "Wed, 21 Oct 2015 07:28:00 GMT");
        HttpHeader expiresHeader = httpHeaders.get(1);

        HttpHeader httpHeader = new HttpHeader("Cache-Control", "no-cache");
        HttpHeader previousHeader = httpHeaders.set(1, httpHeader);

        assertEquals(httpHeader, httpHeaders.get(1));
        assertEquals(expiresHeader, previousHeader);
    }

    @Test
    void setHeaderAlreadyExists() {
        httpHeaders.add("Accept", "text/plain,text/html");
        httpHeaders.add("Expires", "Wed, 21 Oct 2015 07:28:00 GMT");

        assertThrows(IllegalStateException.class, () -> httpHeaders.set(0, new HttpHeader("Expires", "Wed, 21 Oct 2015 07:28:00 GMT")));
    }

    @Test
    void removeHeader() {

        httpHeaders.add("Expires", "Wed, 21 Oct 2015 07:28:00 GMT");

        httpHeaders.remove(1);

        assertEquals(1, httpHeaders.size());
        assertEquals(acceptPlainAndHtml, httpHeaders.get(0));
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
        httpHeaders.clear();
        httpHeaders.add("Accept", asList("  text/plain"));
        httpHeaders.add("ACCEPT", asList("text/html   "));

        assertEquals(acceptPlainAndHtml, httpHeaders.get("ACCEPT"));
    }

    @Test
    void getOnEmptyHeaders() {
        httpHeaders.clear();
        assertNull(httpHeaders.get("Accept"));
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
        httpHeaders.clear();
        httpHeaders.add("accept", asList("text/plain"));
        httpHeaders.add("ACCEPT", "text/html");

        assertEquals(acceptPlainAndHtml, httpHeaders.get("ACCEPT"));
    }
}