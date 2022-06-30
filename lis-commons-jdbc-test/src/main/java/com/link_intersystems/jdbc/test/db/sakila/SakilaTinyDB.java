package com.link_intersystems.jdbc.test.db.sakila;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * A tiny version of the {@link SakilaDB} that contains only data for the actors with id 1 and 2.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTinyDB extends SakilaDB {

    public Reader getDataResource() {
        return new InputStreamReader(SakilaTinyDB.class.getResourceAsStream("sakila-tiny-db.sql"), StandardCharsets.UTF_8);
    }
}
