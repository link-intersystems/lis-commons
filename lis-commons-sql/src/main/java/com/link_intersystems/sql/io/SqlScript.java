package com.link_intersystems.sql.io;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SqlScript {

    public static SqlScript emptyScript() {
        return new SqlScript("") {
            @Override
            public void execute(StatementCallback statementCallback) throws SQLException {
            }

            @Override
            public void execute(Connection connection) throws SQLException {
            }
        };
    }

    private ScriptResource scriptResource;

    private Predicate<String> statementFiler = s -> true;

    public SqlScript(String script) {
        this(() -> new StringReader(script));
    }

    public SqlScript(ScriptResource scriptResource) {
        this.scriptResource = Objects.requireNonNull(scriptResource);
    }

    public void setStatementFilter(Predicate<String> statementFiler) {
        this.statementFiler = statementFiler;
    }

    public void execute(StatementCallback statementCallback) throws SQLException {
        try (StatementReader statementReader = new StatementReader(scriptResource.open())) {
            statementReader.setStatementFilter(statementFiler);

            while (statementReader.hasNext()) {
                String nextStatement = statementReader.next();
                statementCallback.doWithStatement(nextStatement);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void execute(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            execute(sqlStatement -> {
                stmt.addBatch(sqlStatement);
            });

            stmt.executeBatch();
        }
    }

}
