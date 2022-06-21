package com.link_intersystems.net.http;

import com.link_intersystems.net.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpUrlConnectionResponse implements HttpResponse {

    private HttpURLConnection conn;

    public HttpUrlConnectionResponse(HttpURLConnection conn) {
        this.conn = conn;
    }

    @Override
    public int getResponseCode() throws IOException {
        return conn.getResponseCode();
    }

    @Override
    public HttpHeaders getHeaders() {
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        return new HttpHeaders(headerFields);
    }

    @Override
    public InputStream getContent() throws IOException {
        getResponseCode();
        return conn.getInputStream();
    }
}
