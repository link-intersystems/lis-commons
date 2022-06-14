package com.link_intersystems.net.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface PreparedRequest {

    OutputStream getOutputStream() throws IOException;

    HttpResponse close() throws IOException;
}
