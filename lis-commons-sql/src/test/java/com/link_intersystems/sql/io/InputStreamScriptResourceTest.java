package com.link_intersystems.sql.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class InputStreamScriptResourceTest extends AbstractScriptResourceTest {

    protected InputStreamScriptResource createScriptResource(URL scriptResourceURL, Charset charset) throws IOException {
        InputStreamScriptResource scriptResource = new InputStreamScriptResource(scriptResourceURL.openStream());
        scriptResource.setCharset(charset);
        return scriptResource;
    }

}