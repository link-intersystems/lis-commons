package com.link_intersystems.test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface ConnectionCallbackWithResult<T> {

    public <T> T execute(Connection connection) throws SQLException;
}
