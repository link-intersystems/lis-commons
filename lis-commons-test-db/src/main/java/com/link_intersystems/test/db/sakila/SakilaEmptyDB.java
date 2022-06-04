package com.link_intersystems.test.db.sakila;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An empty version of the {@link SakilaDB} that contains no data, just the empty tables.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaEmptyDB extends SakilaDB {

    public Reader getDataResource() {
        return new StringReader("");
    }
}
