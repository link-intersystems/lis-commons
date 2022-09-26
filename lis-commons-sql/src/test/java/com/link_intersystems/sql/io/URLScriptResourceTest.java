package com.link_intersystems.sql.io;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class URLScriptResourceTest extends AbstractScriptResourceTest {

    protected ScriptResource createScriptResource(URL scriptResourceURL, Charset charset) {
        URLScriptResource scriptResource = new URLScriptResource(scriptResourceURL);
        scriptResource.setBufferSize(1024);
        scriptResource.setCharset(charset);
        return scriptResource;
    }


    @Test
    void wrongBufferSize() throws MalformedURLException {
        URLScriptResource urlScriptResource = new URLScriptResource(new URL("file:///none_existent.sql"));

        assertThrows(IllegalArgumentException.class, () -> urlScriptResource.setBufferSize(0));
    }
}