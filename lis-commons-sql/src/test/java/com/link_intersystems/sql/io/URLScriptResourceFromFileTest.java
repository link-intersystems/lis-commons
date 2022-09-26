package com.link_intersystems.sql.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class URLScriptResourceFromFileTest extends AbstractScriptResourceTest {

    protected ScriptResource createScriptResource(URL scriptResourceURL, Charset charset) throws URISyntaxException {
        File file = new File(scriptResourceURL.toURI());
        URLScriptResource scriptResource = URLScriptResource.fromFile(file);
        scriptResource.setBufferSize(1024);
        scriptResource.setCharset(charset);
        return scriptResource;
    }

}