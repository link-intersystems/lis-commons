package com.link_intersystems.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaHttpRequestImplementor implements HttpRequestImplementor {

    private String method;
    private HttpRequestFactory httpRequestFactory;
    private boolean doOutput;

    public JavaHttpRequestImplementor(String method, HttpRequestFactory httpRequestFactory) {
        this.method = requireNonNull(method).trim();

        if (this.method.isEmpty()) {
            throw new IllegalArgumentException("method must not be empty");
        }

        this.httpRequestFactory = requireNonNull(httpRequestFactory);
    }

    public void setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        Duration connectTimeout = httpRequestFactory.getConnectTimeout();
        if (connectTimeout != null) {
            conn.setConnectTimeout((int) connectTimeout.toMillis());
        }

        Duration readTimeout = httpRequestFactory.getReadTimeout();
        if (readTimeout != null) {
            conn.setReadTimeout((int) readTimeout.toMillis());
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(doOutput);
        return conn;
    }

    @Override
    public PreparedRequest prepare(URL url, HttpHeaders requestHeaders) throws IOException {
        HttpURLConnection conn = createConnection(url);

        setHeader(conn, requestHeaders);

        return new PreparedRequest() {

            private OutputStream openOutputStream;

            @Override
            public OutputStream getOutputStream() throws IOException {
                if (openOutputStream == null) {
                    openOutputStream = conn.getOutputStream();
                }
                return openOutputStream;
            }

            @Override
            public HttpResponse execute() throws IOException {
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
                return new HttpUrlConnectionResponse(conn);
            }
        };
    }

    private void setHeader(HttpURLConnection conn, HttpHeaders requestHeaders) {
        for (HttpHeader requestHeaderEntry : requestHeaders) {
            String headerName = requestHeaderEntry.getName();
            String headerValue = String.join(", ", requestHeaderEntry.getValues());
            conn.setRequestProperty(headerName, headerValue);
        }
    }
}
