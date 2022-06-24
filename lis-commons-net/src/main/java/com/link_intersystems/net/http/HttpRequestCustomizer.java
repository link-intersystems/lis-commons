package com.link_intersystems.net.http;

/**
 * Resolves effective {@link HttpHeaders} for a
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface HttpRequestCustomizer {

    default public void customize(HttpRequest httpRequest){
    }
}
