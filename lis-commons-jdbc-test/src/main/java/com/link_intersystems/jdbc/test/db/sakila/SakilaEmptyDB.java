package com.link_intersystems.jdbc.test.db.sakila;

import java.io.Reader;
import java.io.StringReader;

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
