package com.link_intersystems.sql.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractScriptResourceTest {
    private String scriptContent;

    @BeforeEach
    void setUp() throws IOException {
        URL resource = InputStreamScriptResourceTest.class.getResource("sakila.sql");
        try (Reader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
            scriptContent = readContent(reader);
        }
    }

    protected String readContent(Reader reader) throws IOException {
        StringWriter sw = new StringWriter();
        int read = -1;
        while ((read = reader.read()) != -1) {
            sw.append((char) read);
        }
        return sw.toString();
    }

    @Test
    void open() throws Exception {
        String readScript = readScriptResource(StandardCharsets.UTF_8);

        assertEquals(scriptContent, readScript);
    }

    @Test
    void openWrongCharset() throws Exception {
        String readScript = readScriptResource(StandardCharsets.US_ASCII);

        assertNotEquals(scriptContent, readScript);
    }

    private String readScriptResource(Charset charset) throws Exception {
        URL resource = InputStreamScriptResourceTest.class.getResource("sakila.sql");

        String scriptRessourceContent;
        ScriptResource scriptResource = createScriptResource(resource, charset);
        Reader open = scriptResource.open();
        scriptRessourceContent = readContent(open);

        return scriptRessourceContent;
    }

    protected abstract ScriptResource createScriptResource(URL scriptResourceURL, Charset charset) throws Exception;


}
