package com.link_intersystems.test.db.sakila;

import com.link_intersystems.sql.io.SqlScript;
import com.link_intersystems.test.db.DBSetup;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaDB implements DBSetup {

    public List<String> getTableNames() {
        return new ArrayList<>(Arrays.asList(
                "actor",
                "film_actor",
                "film",
                "language",
                "film_category",
                "category",
                "inventory",
                "store",
                "rental",
                "staff",
                "payment",
                "customer",
                "address",
                "city",
                "country"));
    }

    @Override
    public String getSchema() {
        return "sakila";
    }

    public SqlScript getSchemaScript() {
        return new SqlScript(() -> new StringReader("CREATE SCHEMA IF NOT EXISTS " + getSchema()));
    }

    @Override
    public void setupSchema(Connection connection) throws SQLException {
        getSchemaScript().execute(connection);
    }

    public SqlScript getDdlScript() {
        return new SqlScript(this::getDdlResource);
    }

    @Override
    public void setupDdl(Connection connection) throws SQLException {
        getDdlScript().execute(connection);
    }

    public SqlScript getDataScript() {
        return new SqlScript(this::getDataResource);
    }

    public Reader getDdlResource() {
        return new InputStreamReader(SakilaDB.class.getResourceAsStream("sakila-ddl.sql"), StandardCharsets.UTF_8);
    }

    public Reader getDataResource() {
        return new InputStreamReader(SakilaDB.class.getResourceAsStream("sakila-db.sql"), StandardCharsets.UTF_8);
    }

    @Override
    public void setupData(Connection connection) throws SQLException {
        getDataScript().execute(connection);
    }
}
