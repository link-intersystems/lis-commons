package com.link_intersystems.jdbc.format;

import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SqlFormatSettings {

    private String statementDelimiter = ";";

    private SqlFormatter sqlFormatter = s -> s;
    private String statementSeparator;

    public SqlFormatSettings() {
        statementSeparator = System.lineSeparator();
    }

    public void setStatementDelimiter(String statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    public String getStatementDelimiter() {
        return statementDelimiter;
    }

    public SqlFormatter getSqlFormatter() {
        return sqlFormatter;
    }

    public void setSqlFormatter(SqlFormatter sqlFormatter) {
        this.sqlFormatter = Objects.requireNonNull(sqlFormatter);
    }

    public String getStatementSeparator() {
        return statementSeparator;
    }

    public void setStatementSeparator(String statementSeparator) {
        this.statementSeparator = Objects.requireNonNull(statementSeparator);
    }
}
