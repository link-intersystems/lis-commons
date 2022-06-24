package com.link_intersystems.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import static java.util.Objects.requireNonNull;

/**
 * The {@link HttpClient} is a small facade that has convenience methods for common tasks.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpClient {

    private HttpRequestCustomizer requestCustomizer = new DefaultHttpRequestCustomizer();

    private HttpRequestFactory requestFactory;

    public HttpClient() {
        this(new DefaultHttpRequestFactory());
    }

    public HttpClient(HttpRequestFactory requestFactory) {
        this.requestFactory = requireNonNull(requestFactory);
    }

    public HttpRequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestCustomizer(HttpRequestCustomizer requestCustomizer) {
        this.requestCustomizer = requireNonNull(requestCustomizer);
    }

    public HttpRequestCustomizer getRequestCustomizer() {
        return requestCustomizer;
    }

    // GET

    public HttpResponse get(String url) throws IOException {
        return this.get(url, new HttpHeaders());
    }

    public HttpResponse get(URL url) throws IOException {
        return get(url, new HttpHeaders());
    }

    public HttpResponse get(String url, HttpHeaders headers) throws IOException {
        return get(new URL(url), headers);
    }

    public HttpResponse get(URL url, HttpHeaders headers) throws IOException {
        HttpRequest httpRequest = requestFactory.get(url);
        httpRequest.setHeaders(headers);

        requestCustomizer.customize(httpRequest);

        PreparedRequest preparedRequest = httpRequest.prepare();

        return preparedRequest.execute();
    }

    // POST

    public HttpResponse post(String url) throws IOException {
        return post(new URL(url), new HttpHeaders(), null);
    }

    public HttpResponse post(URL url) throws IOException {
        return post(url, new HttpHeaders(), null);
    }

    public HttpResponse post(String url, ContentWriter contentWriter) throws IOException {
        return post(new URL(url), new HttpHeaders(), contentWriter);
    }

    public HttpResponse post(URL url, ContentWriter contentWriter) throws IOException {
        return post(url, new HttpHeaders(), contentWriter);
    }

    public HttpResponse post(String url, HttpHeaders headers) throws IOException {
        return post(new URL(url), headers, null);
    }

    public HttpResponse post(URL url, HttpHeaders headers) throws IOException {
        return post(url, headers, null);
    }

    public HttpResponse post(String url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        return post(new URL(url), headers, contentWriter);
    }

    public HttpResponse post(URL url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        HttpRequest httpRequest = requestFactory.post(url);

        return executeRequest(httpRequest, headers, contentWriter);
    }

    //PUT

    public HttpResponse put(String url) throws IOException {
        return put(new URL(url), new HttpHeaders(), null);
    }

    public HttpResponse put(URL url) throws IOException {
        return put(url, new HttpHeaders(), null);
    }

    public HttpResponse put(String url, ContentWriter contentWriter) throws IOException {
        return put(new URL(url), new HttpHeaders(), contentWriter);
    }

    public HttpResponse put(URL url, ContentWriter contentWriter) throws IOException {
        return put(url, new HttpHeaders(), contentWriter);
    }

    public HttpResponse put(String url, HttpHeaders headers) throws IOException {
        return put(new URL(url), headers, null);
    }

    public HttpResponse put(URL url, HttpHeaders headers) throws IOException {
        return put(url, headers, null);
    }

    public HttpResponse put(String url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        return put(new URL(url), headers, contentWriter);
    }

    public HttpResponse put(URL url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        HttpRequest httpRequest = requestFactory.put(url);

        return executeRequest(httpRequest, headers, contentWriter);
    }

    // DELETE

    public HttpResponse delete(String url) throws IOException {
        return delete(new URL(url), new HttpHeaders(), null);
    }

    public HttpResponse delete(URL url) throws IOException {
        return delete(url, new HttpHeaders(), null);
    }

    public HttpResponse delete(String url, ContentWriter contentWriter) throws IOException {
        return delete(new URL(url), new HttpHeaders(), contentWriter);
    }

    public HttpResponse delete(URL url, ContentWriter contentWriter) throws IOException {
        return delete(url, new HttpHeaders(), contentWriter);
    }

    public HttpResponse delete(String url, HttpHeaders headers) throws IOException {
        return delete(new URL(url), headers, null);
    }

    public HttpResponse delete(URL url, HttpHeaders headers) throws IOException {
        return delete(url, headers, null);
    }

    public HttpResponse delete(String url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        return delete(new URL(url), headers, contentWriter);
    }

    public HttpResponse delete(URL url, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        HttpRequest httpRequest = requestFactory.delete(url);

        return executeRequest(httpRequest, headers, contentWriter);
    }

    private HttpResponse executeRequest(HttpRequest httpRequest, HttpHeaders headers, ContentWriter contentWriter) throws IOException {
        httpRequest.setHeaders(headers);

        requestCustomizer.customize(httpRequest);

        PreparedRequest preparedRequest = httpRequest.prepare();

        if (contentWriter != null) {
            OutputStream outputStream = preparedRequest.getOutputStream();
            contentWriter.write(outputStream);
        }

        return preparedRequest.execute();
    }

}
