package com.link_intersystems.sql.io;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface StatementCallback {

    public void doWithStatement(String sqlStatement) throws SQLException;
}
