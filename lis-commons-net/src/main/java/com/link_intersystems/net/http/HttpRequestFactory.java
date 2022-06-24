package com.link_intersystems.net.http;

import java.net.URL;
import java.time.Duration;

import static com.link_intersystems.net.http.HttpMethod.*;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class HttpRequestFactory {

    private Duration connectTimeout = Duration.of(5, SECONDS);
    private Duration readTimeout = Duration.of(1, MINUTES);

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public HttpRequest get(URL url) {
        return createRequest(GET, url);
    }

    public HttpRequest post(URL url) {
        HttpRequest httpRequest = createRequest(POST, url);
        httpRequest.setWithOutput(true);
        return httpRequest;
    }

    public HttpRequest put(URL url) {
        HttpRequest httpRequest = createRequest(PUT, url);
        httpRequest.setWithOutput(true);
        return httpRequest;
    }

    public HttpRequest delete(URL url) {
        HttpRequest httpRequest = createRequest(DELETE, url);
        httpRequest.setWithOutput(true);
        return httpRequest;
    }

    protected HttpRequest createRequest(HttpMethod method, URL url) {
        HttpRequestImplementor implementor = createImplementor(method, this);
        return new HttpRequest(method, url, implementor);
    }

    protected abstract HttpRequestImplementor createImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory);


}
