package com.link_intersystems.net.http.test;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class RequestMocking {
    private int responseCode;
    private byte[] body;

    RequestMocking(int responseCode) {
        this.responseCode = responseCode;
    }

    int getResponseCode() {
        return responseCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
