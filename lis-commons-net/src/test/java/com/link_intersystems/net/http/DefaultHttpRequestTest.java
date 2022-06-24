package com.link_intersystems.net.http;

import com.link_intersystems.net.http.test.HttpMockServer;
import com.link_intersystems.net.http.test.junit.HttpMockServerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(HttpMockServerExtension.class)
public class DefaultHttpRequestTest {

    private HttpMockServer httpMockServer;
    private HttpRequestFactory requestFactory;

    @BeforeEach
    void setUp(HttpMockServer httpMockServer) {
        this.httpMockServer = httpMockServer;
        requestFactory = new DefaultHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.of(1, ChronoUnit.SECONDS));
    }

    @Test
    void get() throws IOException, URISyntaxException {
        URL baseURL = httpMockServer.getBaseUrl();
        URL requestUrl = baseURL.toURI().resolve(new URI("/somePath")).toURL();
        httpMockServer.whenRequestPath(requestUrl.getPath()).respond(200);

        HttpRequest httpRequest = requestFactory.get(requestUrl);
        HttpHeaders requestHeaders = httpRequest.getHeaders();
        requestHeaders.add("Accept", "application/json");
        HttpResponse httpResponse = httpRequest.prepare().execute();

        assertEquals(200, httpResponse.getResponseCode());

        HttpMockServer.ReceivedRequest latestRequest = httpMockServer.getLatestRequest();
        Map<String, String> headers = latestRequest.getHeaders();

        assertEquals("application/json", headers.get("Accept"));
    }

    @Test
    void post() throws IOException, URISyntaxException {
        URL baseURL = httpMockServer.getBaseUrl();
        URL requestUrl = baseURL.toURI().resolve(new URI("/somePath")).toURL();
        httpMockServer.whenRequestPath(requestUrl.getPath()).respond(200, "Response to Hello World".getBytes(StandardCharsets.UTF_8));

        HttpRequest httpRequest = requestFactory.post(requestUrl);
        HttpHeaders requestHeaders = httpRequest.getHeaders();
        requestHeaders.add("Accept", "application/json");
        PreparedRequest preparedRequest = httpRequest.prepare();
        preparedRequest.getOutputStream().write("Hello World".getBytes(StandardCharsets.UTF_8));
        HttpResponse httpResponse = preparedRequest.execute();

        assertEquals(200, httpResponse.getResponseCode());

        HttpMockServer.ReceivedRequest latestRequest = httpMockServer.getLatestRequest();
        Map<String, String> headers = latestRequest.getHeaders();

        assertEquals("application/json", headers.get("Accept"));
        byte[] content = latestRequest.getBody();

        assertArrayEquals("Hello World".getBytes(StandardCharsets.UTF_8), content);

        String contentAsString = httpResponse.getDecodedContent();
        assertEquals("Response to Hello World", contentAsString);
    }

    @Test
    void put() throws IOException, URISyntaxException {
        URL baseURL = httpMockServer.getBaseUrl();
        URL requestUrl = baseURL.toURI().resolve(new URI("/somePath")).toURL();
        httpMockServer.whenRequestPath(requestUrl.getPath()).respond(200, "Response to Hello World".getBytes(StandardCharsets.UTF_8));

        HttpRequest httpRequest = requestFactory.put(requestUrl);
        HttpHeaders requestHeaders = httpRequest.getHeaders();
        requestHeaders.add("Accept", "application/json");
        PreparedRequest preparedRequest = httpRequest.prepare();
        preparedRequest.getOutputStream().write("Hello World".getBytes(StandardCharsets.UTF_8));
        HttpResponse httpResponse = preparedRequest.execute();

        assertEquals(200, httpResponse.getResponseCode());

        HttpMockServer.ReceivedRequest latestRequest = httpMockServer.getLatestRequest();
        Map<String, String> headers = latestRequest.getHeaders();

        assertEquals("application/json", headers.get("Accept"));
        byte[] content = latestRequest.getBody();

        assertArrayEquals("Hello World".getBytes(StandardCharsets.UTF_8), content);

        String contentAsString = httpResponse.getDecodedContent();
        assertEquals("Response to Hello World", contentAsString);
    }

    @Test
    void delete() throws IOException, URISyntaxException {
        URL baseURL = httpMockServer.getBaseUrl();
        URL requestUrl = baseURL.toURI().resolve(new URI("/somePath")).toURL();
        httpMockServer.whenRequestPath(requestUrl.getPath()).respond(200, "Response to Hello World".getBytes(StandardCharsets.UTF_8));

        HttpRequest httpRequest = requestFactory.delete(requestUrl);
        HttpHeaders requestHeaders = httpRequest.getHeaders();
        requestHeaders.add("Accept", "application/json");
        requestHeaders.add("Content-Length", "11");
        PreparedRequest preparedRequest = httpRequest.prepare();
        HttpResponse httpResponse = preparedRequest.execute();

        assertEquals(200, httpResponse.getResponseCode());

        HttpMockServer.ReceivedRequest latestRequest = httpMockServer.getLatestRequest();
        Map<String, String> headers = latestRequest.getHeaders();

        assertEquals("application/json", headers.get("Accept"));

        String contentAsString = httpResponse.getDecodedContent();
        assertEquals("Response to Hello World", contentAsString);
    }

    @AfterEach
    void tearDown() throws IOException {
        httpMockServer.close();
    }
}
