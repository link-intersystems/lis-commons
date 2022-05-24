package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.H2Database;
import com.link_intersystems.test.jdbc.H2JdbcUrl;
import com.link_intersystems.test.jdbc.SqlScript;

import java.io.IOException;
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

        SqlScript ddlScript = dbSetup.getDdlScript();
        ddlScript.execute(connection);

        H2JdbcUrl jdbcUrl = h2Database.getJdbcUrl();
        String defaultSchema = dbSetup.getDefaultSchema();
        h2Database = new H2Database(new H2JdbcUrl.Builder(jdbcUrl).setSchema(defaultSchema).build());

        SqlScript dataScript = dbSetup.getDataScript();

        dataScript.execute(connection);

        return h2Database;
    }
}
