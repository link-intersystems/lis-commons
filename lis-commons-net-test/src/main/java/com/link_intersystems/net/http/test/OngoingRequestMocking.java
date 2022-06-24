package com.link_intersystems.net.http.test;

import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class OngoingRequestMocking {
    private String requestPath;
    private Map<String, RequestMocking> requestMockings;

    OngoingRequestMocking(String requestPath, Map<String, RequestMocking> requestMockings) {
        this.requestPath = requestPath;
        this.requestMockings = requestMockings;
    }

    public void respond(int responseCode) {
        requestMockings.put(requestPath, new RequestMocking(responseCode));
    }

    public void respond(int responseCode, byte[] body) {
        RequestMocking requestMocking = new RequestMocking(responseCode);
        requestMocking.setBody(body);
        requestMockings.put(requestPath, requestMocking);
    }
}
