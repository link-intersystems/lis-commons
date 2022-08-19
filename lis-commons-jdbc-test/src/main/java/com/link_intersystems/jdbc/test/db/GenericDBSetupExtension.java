package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;
import com.link_intersystems.jdbc.test.db.setup.DBSetupH2DatabaseFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class GenericDBSetupExtension extends GenericTestDBExtention implements DBSetup {
    @Override
    protected H2DatabaseFactory createH2DatabaseFactory() {
        return new DBSetupH2DatabaseFactory(this);
    }

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
