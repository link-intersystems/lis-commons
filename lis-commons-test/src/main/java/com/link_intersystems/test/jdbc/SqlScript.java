package com.link_intersystems.test.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SqlScript implements Iterator<String> {

    private Reader scriptReader;
    private Integer lookahead;

    public SqlScript(InputStream scriptInputStream) throws IOException, SQLException {
        this(scriptInputStream, StandardCharsets.UTF_8);
    }

    public SqlScript(InputStream scriptInputStream, Charset charset) throws IOException, SQLException {
        this(new InputStreamReader(scriptInputStream, charset));
    }

    public SqlScript(Reader scriptReader) {
        this.scriptReader = scriptReader;
    }

    @Override
    public boolean hasNext() {
        try {
            if (lookahead == null) {

                lookahead = scriptReader.read();

            }
            if (lookahead == -1) {
                scriptReader.close();
                lookahead = -2;
            }
            return lookahead > -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String next() {
        StringBuilder sb = new StringBuilder();

        int commentCharCount = 0;
        while (true) {
            int read = nextChar();
            if (read == '\n') {
                commentCharCount = 0;
                continue;
            }
            if (read == '-') {
                commentCharCount++;
                continue;
            }
            if (commentCharCount == 2) {
                continue;
            }
            if (commentCharCount == 1) {
                sb.append('-');
                commentCharCount = 0;
            }
            if (read == -1 || read == ';') {
                break;
            }
            sb.append((char) read);
        }
        return sb.toString();
    }

    private int nextChar() {
        if (lookahead != null) {
            try {
                return lookahead;
            } finally {
                lookahead = null;
            }
        }
        try {
            return scriptReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            while (hasNext()) {
                String nextSql = next();
                String executableSql = removeWhitespaces(nextSql);
                stmt.execute(executableSql);
            }
        }
    }

    private String removeWhitespaces(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }
}
