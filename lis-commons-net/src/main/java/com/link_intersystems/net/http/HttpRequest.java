package com.link_intersystems.net.http;

import java.io.IOException;
import java.net.URL;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpRequest {

    private HttpHeaders headers = new HttpHeaders();
    private boolean withOutput = false;

    private final URL url;
    private HttpRequestImplementor implementor;
    private HttpMethod httpMethod;

    public HttpRequest(HttpMethod httpMethod, URL url, HttpRequestImplementor implementor) {
        this.httpMethod = requireNonNull(httpMethod);
        this.url = requireNonNull(url);
        this.implementor = requireNonNull(implementor);
        String protocol = url.getProtocol();
        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new IllegalArgumentException("url must be a http url.");
        }
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public URL getURL() {
        return url;
    }

    public void setWithOutput(boolean withOutput) {
        this.withOutput = withOutput;
    }

    public boolean isWithOutput() {
        return withOutput;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = requireNonNull(headers);
    }

    public PreparedRequest prepare() throws IOException {
        return implementor.prepare(this);
    }
}
