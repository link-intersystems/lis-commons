package com.link_intersystems.sql.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class InputStreamScriptResource implements ScriptResource {

    private Charset charset = StandardCharsets.UTF_8;
    private InputStream inputStream;

    public InputStreamScriptResource(InputStream inputStream) {
        this.inputStream = requireNonNull(inputStream);
    }

    public void setCharset(Charset charset) {
        this.charset = requireNonNull(charset);
    }

    @Override
    public Reader open() throws IOException {
        return new InputStreamReader(inputStream, charset);
    }
}
