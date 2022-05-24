package com.link_intersystems.test.db.sakila;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaSlimDB extends SakilaDB {

    @Override
    public List<String> getTableNames() {
        return new ArrayList<>(Arrays.asList("actor", "film_actor", "film", "language", "film_category", "category"));
    }

    public Reader getDataResource() {
        return new InputStreamReader(SakilaSlimDB.class.getResourceAsStream("sakila-slim-db.sql"), StandardCharsets.UTF_8);
    }
}
