package com.link_intersystems.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

/**
 * The {@link HttpClient} is a small facade that has convenience methods for common tasks.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpClient {

    private HttpRequestFactory requestFactory;

    public HttpClient(HttpRequestFactory requestFactory) {
        this.requestFactory = requireNonNull(requestFactory);
    }

    public HttpResponse get(String url) throws IOException {
        return this.get(url, emptyMap());
    }

    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        return get(new URL(url), headers);
    }

    public HttpResponse get(URL url) throws IOException {
        return get(url, emptyMap());
    }

    public HttpResponse get(URL url, Map<String, String> headers) throws IOException {
        HttpRequest httpRequest = requestFactory.get(url);

        headers.forEach(httpRequest::addHeader);

        PreparedRequest preparedRequest = httpRequest.prepare();

        return preparedRequest.execute();
    }

    public HttpResponse post(URL url) throws IOException {
        return post(url, emptyMap(), null);
    }

    public HttpResponse post(String url, Consumer<OutputStream> contentWriter) throws IOException {
        return post(new URL(url), emptyMap(), contentWriter);
    }

    public HttpResponse post(String url, Map<String, String> headers) throws IOException {
        return post(new URL(url), headers, null);
    }

    public HttpResponse post(URL url, Map<String, String> headers, Consumer<OutputStream> contentWriter) throws IOException {
        HttpRequest httpRequest = requestFactory.post(url);

        headers.forEach(httpRequest::addHeader);

        PreparedRequest preparedRequest = httpRequest.prepare();

        if (contentWriter != null) {
            OutputStream outputStream = preparedRequest.getOutputStream();
            contentWriter.accept(outputStream);
        }

        return preparedRequest.execute();
    }


    public HttpResponse put(URL url) throws IOException {
        return put(url, emptyMap(), null);
    }

    public HttpResponse put(String url, Consumer<OutputStream> contentWriter) throws IOException {
        return put(new URL(url), emptyMap(), contentWriter);
    }

    public HttpResponse put(String url, Map<String, String> headers) throws IOException {
        return put(new URL(url), headers, null);
    }

    public HttpResponse put(URL url, Map<String, String> headers, Consumer<OutputStream> contentWriter) throws IOException {
        HttpRequest httpRequest = requestFactory.put(url);

        headers.forEach(httpRequest::addHeader);

        PreparedRequest preparedRequest = httpRequest.prepare();

        if (contentWriter != null) {
            OutputStream outputStream = preparedRequest.getOutputStream();
            contentWriter.accept(outputStream);
        }

        return preparedRequest.execute();
    }

}
