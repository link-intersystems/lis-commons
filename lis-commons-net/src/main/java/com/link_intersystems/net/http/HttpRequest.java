package com.link_intersystems.net.http;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpRequest {

    private HttpHeaders requestHeaders = new HttpHeaders();
    private boolean withOutput = false;

    private final URL url;
    private HttpRequestImplementor implementor;

    public HttpRequest(URL url, HttpRequestImplementor implementor) {
        this.url = requireNonNull(url);
        this.implementor = requireNonNull(implementor);
        String protocol = url.getProtocol();
        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new IllegalArgumentException("url must be a http url.");
        }
    }

    public void setWithOutput(boolean withOutput) {
        this.withOutput = withOutput;
    }

    public boolean isWithOutput() {
        return withOutput;
    }

    public void addHeader(String name, String value) {
        requestHeaders.add(name, value);
    }

    public PreparedRequest prepare() throws IOException {
        return implementor.prepare(url, requestHeaders);
    }
}
