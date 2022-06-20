package com.link_intersystems.net.http.java;

import com.link_intersystems.net.http.HttpRequestFactory;
import com.link_intersystems.net.http.HttpRequestImplementor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultHttpRequestFactory extends HttpRequestFactory {

    private Set<String> outputSupportedMethods = new HashSet<>(Arrays.asList("POST", "PUT", "DELETE", "PATCH"));

    public void setOutputSupportedMethods(Set<String> outputSupportedMethods) {
        this.outputSupportedMethods = requireNonNull(outputSupportedMethods);
    }

    @Override
    protected HttpRequestImplementor createImplementor(String method, HttpRequestFactory httpRequestFactory) {
        JavaHttpRequestImplementor javaHttpRequestImplementor = new JavaHttpRequestImplementor(method, httpRequestFactory);
        boolean outputSupported = isOutputSupported(method);
        javaHttpRequestImplementor.setDoOutput(outputSupported);
        return javaHttpRequestImplementor;
    }

    protected boolean isOutputSupported(String method) {
        return outputSupportedMethods.contains(method);
    }
}
