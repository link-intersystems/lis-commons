package com.link_intersystems.sql.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class StringScriptResourceTest extends AbstractScriptResourceTest {

    protected ScriptResource createScriptResource(URL scriptResourceURL, Charset charset) throws IOException {
        String content = readContent(new InputStreamReader(scriptResourceURL.openStream(), charset));
        StringScriptResource scriptResource = new StringScriptResource(content);
        return scriptResource;
    }



}