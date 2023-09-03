package com.link_intersystems.jdbc.test.db.sakila;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A slim version of the {@link SakilaDB} that contains only data for:
 * <ul>
 *     <li>actor</li>
 *     <li>film_actor</li>
 *     <li>film</li>
 *     <li>language</li>
 *     <li>film_category</li>
 *     <li>category</li>
 * </ul>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaSlimDB extends SakilaDB {

    public static List<String> getTableNames() {
        return new ArrayList<>(Arrays.asList("actor", "film_actor", "film", "language", "film_category", "category"));
    }

    public Reader getDataResource() throws IOException {
        return SakilaResources.getSlimDataSql();
    }

}
