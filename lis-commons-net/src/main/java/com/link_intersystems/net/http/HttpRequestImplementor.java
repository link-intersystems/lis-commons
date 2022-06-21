package com.link_intersystems.net.http;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface HttpRequestImplementor {
    PreparedRequest prepare(URL url, HttpHeaders requestHeaders) throws IOException;
}
