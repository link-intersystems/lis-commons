package com.link_intersystems.sql.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class StringScriptResource implements ScriptResource {

    private String script;

    public StringScriptResource(String script) {
        this.script = requireNonNull(script);
    }

    @Override
    public Reader open() throws IOException {
        return new StringReader(script);
    }
}
