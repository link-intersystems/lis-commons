package com.link_intersystems.jdbc.test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface ConnectionCallback {

    public void execute(Connection connection) throws SQLException;
}
