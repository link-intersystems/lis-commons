package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.db.H2DatabaseFactory;
import com.link_intersystems.test.jdbc.H2Database;
import com.link_intersystems.test.jdbc.H2JdbcUrl;
import com.link_intersystems.test.jdbc.SqlScript;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaDBFactory implements H2DatabaseFactory {

    public H2Database create() throws SQLException, IOException {
        H2Database h2Database = new H2Database();

        createSchema(h2Database);
        createDDL(h2Database);

        H2JdbcUrl jdbcUrl = h2Database.getJdbcUrl();
        h2Database = new H2Database(new H2JdbcUrl.Builder(jdbcUrl).setSchema("sakila").build());

        insertData(h2Database);

        return h2Database;
    }

    public void createSchema(H2Database h2Database) throws SQLException {
        h2Database.execute("CREATE SCHEMA IF NOT EXISTS sakila");
    }

    public void createDDL(H2Database h2Database) throws IOException, SQLException {
        InputStream sakilaDDLInputStream = SakilaDBFactory.class.getResourceAsStream("sakila-ddl.sql");
        SqlScript ddlScript = new SqlScript(sakilaDDLInputStream);
        h2Database.executeScript(ddlScript);
    }

    public void insertData(H2Database h2Database) throws SQLException {
        h2Database.doWithConnection(connection -> {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                SqlScript sqlScript = new SqlScript(SakilaDBFactory.class.getResourceAsStream("sakila-db.sql"));
                sqlScript.execute(connection);
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            } catch (IOException e) {
                throw new SQLException(e);
            }
        });
    }
}
