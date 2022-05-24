package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.db.AbstractTestDBExtension;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.sql.Connection;

/**
 * A {@link ParameterResolver} that provides a {@link Connection} to a H2 in memory database initialized with the sakila
 * sample database provided by mysql.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTestDBExtension extends AbstractTestDBExtension {

    public SakilaTestDBExtension() {
        super(new SakilaDB());
    }
}
