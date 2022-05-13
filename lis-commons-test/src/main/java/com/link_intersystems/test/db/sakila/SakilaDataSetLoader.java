package com.link_intersystems.test.db.sakila;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaDataSetLoader {

    public void execute(Connection connection) throws SQLException {
        InputStream resourceAsStream = SakilaDataSetLoader.class.getResourceAsStream("sakila-db.xml");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            DatabaseConnection databaseConnection = new DatabaseConnection(connection);
            DatabaseConfig config = databaseConnection.getConfig();
            config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
            FlatXmlDataSet sakilaDataSet = new FlatXmlDataSetBuilder().build(resourceAsStream);
            DatabaseOperation.INSERT.execute(databaseConnection, sakilaDataSet);
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (DatabaseUnitException e) {
            throw new SQLException(e);
        }
    }
}
