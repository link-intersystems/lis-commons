package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.db.GenericTestDBExtention;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.sql.Connection;

/**
 * A {@link ParameterResolver} that provides a {@link Connection} and {@link com.link_intersystems.test.jdbc.H2Database}
 * for a H2 database initialized with the {@link SakilaDB}.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTestDBExtension extends GenericTestDBExtention {

    public SakilaTestDBExtension() {
        super(new SakilaH2DatabaseFactory());
    }
}
