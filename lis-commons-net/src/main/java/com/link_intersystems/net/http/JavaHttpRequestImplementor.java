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

    private HttpMethod method;
    private HttpRequestFactory httpRequestFactory;

    public JavaHttpRequestImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {
        this.method = requireNonNull(method);

        this.httpRequestFactory = requireNonNull(httpRequestFactory);
    }

    protected HttpURLConnection createConnection(URL url, boolean withOutput) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        Duration connectTimeout = httpRequestFactory.getConnectTimeout();
        if (connectTimeout != null) {
            conn.setConnectTimeout((int) connectTimeout.toMillis());
        }

        Duration readTimeout = httpRequestFactory.getReadTimeout();
        if (readTimeout != null) {
            conn.setReadTimeout((int) readTimeout.toMillis());
        }

        conn.setRequestMethod(method.name());
        conn.setDoOutput(withOutput);
        return conn;
    }

    @Override
    public PreparedRequest prepare(HttpRequest httpRequest) throws IOException {
        URL url = httpRequest.getURL();
        HttpURLConnection conn = createConnection(url, httpRequest.isWithOutput());

        HttpHeaders requestHeaders = httpRequest.getHeaders();
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
                    openOutputStream.flush();
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
