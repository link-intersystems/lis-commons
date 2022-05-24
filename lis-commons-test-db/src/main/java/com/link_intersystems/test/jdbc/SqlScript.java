package com.link_intersystems.test.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SqlScript {

    public static interface ReaderSupplier {

        public Reader get() throws IOException;
    }

    public static interface StatementCallback {

        public void doWithStatement(String sqlStatement) throws SQLException;
    }

    private ReaderSupplier scriptReaderSupplier;

    private Predicate<String> statementFiler = s -> true;

    public SqlScript(ReaderSupplier scriptReaderSupplier) {
        this.scriptReaderSupplier = Objects.requireNonNull(scriptReaderSupplier);
    }

    public void setStatementFilter(Predicate<String> statementFiler) {
        this.statementFiler = statementFiler;
    }

    public void execute(StatementCallback statementCallback) throws SQLException {
        try (StatementReader statementReader = new StatementReader(scriptReaderSupplier.get())) {
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
