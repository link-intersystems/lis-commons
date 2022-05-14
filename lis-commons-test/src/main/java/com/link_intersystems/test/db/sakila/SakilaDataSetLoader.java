package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.jdbc.SqlScript;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaDataSetLoader {

    public void execute(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            SqlScript sqlScript = new SqlScript(SakilaDataSetLoader.class.getResourceAsStream("sakila-db.sql"));
            sqlScript.execute(connection);
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
