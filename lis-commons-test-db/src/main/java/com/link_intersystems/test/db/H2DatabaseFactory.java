package com.link_intersystems.test.db;

import com.link_intersystems.sql.io.SqlScript;
import com.link_intersystems.test.jdbc.H2Database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2DatabaseFactory {
    private DBSetup dbSetup;

    public H2DatabaseFactory(DBSetup dbSetup) {
        this.dbSetup = dbSetup;
    }

    public H2Database create() throws SQLException {
        H2Database h2Database = new H2Database();

        Connection connection = h2Database.getConnection();

        SqlScript schemaSqlScript = dbSetup.getSchemaScript();
        schemaSqlScript.execute(connection);

        String defaultSchema = dbSetup.getDefaultSchema();
        h2Database.setSchema(defaultSchema);

        SqlScript ddlScript = dbSetup.getDdlScript();
        ddlScript.execute(connection);

        SqlScript dataScript = dbSetup.getDataScript();

        dataScript.execute(connection);

        return h2Database;
    }
}
