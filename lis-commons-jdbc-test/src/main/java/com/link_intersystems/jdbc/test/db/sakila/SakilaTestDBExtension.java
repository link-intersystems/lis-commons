package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.db.GenericTestDBExtention;
import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.db.H2DatabaseFactory;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.sql.Connection;

/**
 * A {@link ParameterResolver} that provides a {@link Connection} and {@link H2Database}
 * for a H2 database initialized with the {@link SakilaDB}.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTestDBExtension extends GenericTestDBExtention {

    @Override
    protected H2DatabaseFactory createH2DatabaseFactory() {
        return new SakilaH2DatabaseFactory();
    }
}
