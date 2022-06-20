package com.link_intersystems.net.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ContentWriter {

    public void write(OutputStream outputStream) throws IOException;
}
