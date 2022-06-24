package com.link_intersystems.net.http;

import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpRequestFactoryMocking extends HttpRequestFactory implements HttpRequestCustomizer {

    private final ByteArrayOutputStream outputStream;
    private final HttpRequestCustomizer requestCustomizer;
    private HttpRequestImplementor requestImplementor;
    private PreparedRequest preparedRequest;
    private HttpMethod method;

    HttpRequestFactoryMocking() throws IOException {
        requestImplementor = mock(HttpRequestImplementor.class);
        preparedRequest = mock(PreparedRequest.class);
        outputStream = new ByteArrayOutputStream();
        requestCustomizer = mock(HttpRequestCustomizer.class);

        when(requestImplementor.prepare(any(HttpRequest.class))).thenReturn(preparedRequest);
        when(preparedRequest.getOutputStream()).thenReturn(outputStream);
    }

    @Override
    public void customize(HttpRequest httpRequest) {
        requestCustomizer.customize(httpRequest);
    }

    @Override
    protected HttpRequestImplementor createImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {
        this.method = method;
        return requestImplementor;
    }

    void assertRequest(HttpMethod expectedMethod, String expectedUrl, HttpHeaders expectedHeaders) throws IOException {
        Assertions.assertEquals(expectedMethod, this.method, "HTTP method");
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(requestImplementor).prepare(requestCaptor.capture());

        HttpRequest httpRequest = requestCaptor.getValue();
        assertEquals(method, httpRequest.getMethod());
        assertEquals(new URL(expectedUrl), httpRequest.getURL());
        assertEquals(expectedHeaders, httpRequest.getHeaders());

        verify(preparedRequest).execute();
        verify(requestCustomizer).customize(requestCaptor.capture());
        HttpRequest customizerRequest = requestCaptor.getValue();
        assertEquals(method, customizerRequest.getMethod());
        assertEquals(new URL(expectedUrl), customizerRequest.getURL());
        assertEquals(expectedHeaders, customizerRequest.getHeaders());
    }

    public void assertRequestWithContent(HttpMethod expectedMethod, String expectedUrl, HttpHeaders expectedHeaders, byte[] expectedContent) throws IOException {
        assertRequest(expectedMethod, expectedUrl, expectedHeaders);

        byte[] content = outputStream.toByteArray();

        assertArrayEquals(expectedContent, content);
    }
}

