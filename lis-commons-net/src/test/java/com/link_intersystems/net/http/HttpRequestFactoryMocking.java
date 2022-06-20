package com.link_intersystems.net.http;

import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpRequestFactoryMocking extends HttpRequestFactory {

    private final ByteArrayOutputStream outputStream;
    private HttpRequestImplementor requestImplementor;
    private PreparedRequest preparedRequest;
    private String method;

    @SuppressWarnings("unchecked")
    HttpRequestFactoryMocking() throws IOException {
        requestImplementor = mock(HttpRequestImplementor.class);
        preparedRequest = mock(PreparedRequest.class);
        outputStream = new ByteArrayOutputStream();

        when(requestImplementor.prepare(any(URL.class), any(Map.class))).thenReturn(preparedRequest);
        when(preparedRequest.getOutputStream()).thenReturn(outputStream);
    }

    @Override
    protected HttpRequestImplementor createImplementor(String method, HttpRequestFactory httpRequestFactory) {
        this.method = method;
        return requestImplementor;
    }

    void assertRequest(String expectedMethod, String expectedUrl, Map<String, String> expectedHeaders) throws IOException {
        Assertions.assertEquals(expectedMethod, this.method, "HTTP method");
        verify(requestImplementor).prepare(new URL(expectedUrl), expectedHeaders);
        verify(preparedRequest).execute();
    }

    public void assertRequestWithContent(String expectedMethod, String expectedUrl, Map<String, String> expectedHeaders, byte[] expectedContent) throws IOException {
        assertRequest(expectedMethod, expectedUrl, expectedHeaders);

        byte[] content = outputStream.toByteArray();

        assertArrayEquals(expectedContent, content);
    }
}

