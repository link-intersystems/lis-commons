package com.link_intersystems.net.http;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.link_intersystems.net.http.HttpMethod.*;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultHttpRequestFactory extends HttpRequestFactory {

    @Override
    protected HttpRequestImplementor createImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {
        return new JavaHttpRequestImplementor(method, httpRequestFactory);
    }
}
