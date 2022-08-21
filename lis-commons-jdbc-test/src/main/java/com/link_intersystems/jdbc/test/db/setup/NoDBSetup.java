package com.link_intersystems.jdbc.test.db.setup;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;

import java.sql.Connection;
import java.sql.SQLException;

public class NoDBSetup implements DBSetup {
    @Override
    public void setupSchema(Connection connection) throws SQLException {
    }

    @Override
    public void setupDdl(Connection connection) throws SQLException {
    }

    @Override
    public void setupData(Connection connection) throws SQLException {
    }
}
