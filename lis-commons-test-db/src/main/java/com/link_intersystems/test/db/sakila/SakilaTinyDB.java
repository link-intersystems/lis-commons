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
public class SakilaTinyDB extends SakilaSlimDB {

    public Reader getDataResource() {
        return new InputStreamReader(SakilaTinyDB.class.getResourceAsStream("sakila-tiny-db.sql"), StandardCharsets.UTF_8);
    }
}
