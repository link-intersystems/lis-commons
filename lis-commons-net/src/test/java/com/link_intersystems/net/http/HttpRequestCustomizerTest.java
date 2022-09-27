package com.link_intersystems.net.http;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HttpRequestCustomizerTest {

    @Test
    void customizeDefault() {
        HttpRequest httpRequest = mock(HttpRequest.class);
        new HttpRequestCustomizer() {
        }.customize(httpRequest);

        verifyNoInteractions(httpRequest);
    }
}