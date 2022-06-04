package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.db.GenericTestDBExtention;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.sql.Connection;

/**
 * A {@link ParameterResolver} that provides a {@link Connection} to a H2 in memory database initialized with the sakila
 * sample database provided by mysql
 * that only contains the film related tables: "actor", "film_actor", "film", "language", "film_category", "category".
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaEmptyTestDBExtension extends GenericTestDBExtention {

    public SakilaEmptyTestDBExtension() {
        super(new SakilaH2DatabaseFactory("empty"));
    }
}
