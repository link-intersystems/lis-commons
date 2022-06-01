package com.link_intersystems.test.db;

import com.link_intersystems.sql.io.SqlScript;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface DBSetup {

    public List<String> getTableNames();

    public String getSchema();

    public void setupSchema(Connection connection) throws SQLException;

    public void setupDdl(Connection connection) throws SQLException;

    public void setupData(Connection connection) throws SQLException;
}
