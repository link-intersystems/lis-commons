package com.link_intersystems.sql.io;

import java.io.IOException;
import java.io.Reader;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ScriptResource {

    public Reader open() throws IOException;
}
