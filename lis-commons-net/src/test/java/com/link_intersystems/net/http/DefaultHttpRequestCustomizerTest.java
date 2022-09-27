package com.link_intersystems.net.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DefaultHttpRequestCustomizerTest {

    private DefaultHttpRequestCustomizer requestCustomizer;
    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = mock(HttpRequest.class);

        requestCustomizer = new DefaultHttpRequestCustomizer();
    }


    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    void methodsWithOutput(String httpMethod) {
        when(httpRequest.getMethod()).thenReturn(HttpMethod.valueOf(httpMethod));

        requestCustomizer.customize(httpRequest);

        verify(httpRequest, times(1)).setWithOutput(true);
    }

    @Test
    void customWithOutput() {
        requestCustomizer.setMethodsWithOutput(HttpMethod.POST);

        when(httpRequest.getMethod()).thenReturn(HttpMethod.POST);
        requestCustomizer.customize(httpRequest);
        verify(httpRequest, times(1)).setWithOutput(true);

        reset(httpRequest);

        when(httpRequest.getMethod()).thenReturn(HttpMethod.PUT);
        requestCustomizer.customize(httpRequest);
        verify(httpRequest, times(1)).setWithOutput(false);
    }
}