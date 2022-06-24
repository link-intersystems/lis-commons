package com.link_intersystems.net.http;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.link_intersystems.net.http.HttpMethod.*;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultHttpRequestCustomizer implements HttpRequestCustomizer {

    private Set<HttpMethod> methodsWithOutput = new HashSet<>(Arrays.asList(POST, PUT, DELETE));

    public void setMethodsWithOutput(Set<HttpMethod> methodsWithOutput) {
        this.methodsWithOutput = requireNonNull(methodsWithOutput);
    }

    @Override
    public void customize(HttpRequest httpRequest) {
        HttpMethod method = httpRequest.getMethod();
        boolean withOutput = isOutputSupported(method);
        httpRequest.setWithOutput(withOutput);
    }

    protected boolean isOutputSupported(HttpMethod method) {
        return methodsWithOutput.contains(method);
    }
}
