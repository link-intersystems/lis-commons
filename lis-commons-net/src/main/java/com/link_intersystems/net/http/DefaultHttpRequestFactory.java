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

    private Set<HttpMethod> outputSupportedMethods = new HashSet<>(Arrays.asList(POST, PUT, DELETE));

    public void setOutputSupportedMethods(Set<HttpMethod> outputSupportedMethods) {
        this.outputSupportedMethods = requireNonNull(outputSupportedMethods);
    }

    @Override
    protected HttpRequestImplementor createImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {
        JavaHttpRequestImplementor javaHttpRequestImplementor = new JavaHttpRequestImplementor(method, httpRequestFactory);
        boolean outputSupported = isOutputSupported(method);
        javaHttpRequestImplementor.setDoOutput(outputSupported);
        return javaHttpRequestImplementor;
    }

    protected boolean isOutputSupported(HttpMethod method) {
        return outputSupportedMethods.contains(method);
    }
}
