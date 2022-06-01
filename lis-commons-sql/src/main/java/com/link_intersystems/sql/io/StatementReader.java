package com.link_intersystems.sql.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class StatementReader implements Closeable {

    private Reader reader;
    private Predicate<String> statementFiler = s -> true;

    private String nextStatement;
    private int lookaheadChar = -1;

    public StatementReader(Reader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    public void setStatementFilter(Predicate<String> statementFiler) {
        this.statementFiler = statementFiler;
    }

    public boolean hasNext() throws IOException {
        if (nextStatement == null) {
            nextStatement = readNextStatement();
        }
        return nextStatement != null;
    }

    public String next() {
        String next = nextStatement;
        nextStatement = null;
        return next;
    }

    private String readNextStatement() throws IOException {
        StringBuilder sb = new StringBuilder(255);

        boolean comment = false;
        int read = -1;

        while ((read = read()) != -1) {
            char c = (char) read;

            if (c == '\n') {
                comment = false;
            }

            if (Character.isWhitespace(c)) {
                int lookahead = lookahead();
                if (lookahead != -1) {
                    if (Character.isWhitespace(lookahead)) {
                        continue;
                    }
                }

                // skip trailing whitespaces
                if (sb.length() == 0) {
                    continue;
                }
            }

            if (c == '-') {
                int lookahead = lookahead();
                if (lookahead != -1) {
                    if (lookahead == '-') {
                        comment = true;
                    }
                }
            }

            if (comment) {
                continue;
            }

            if (c == ';') {
                if (statementFiler.test(sb.toString())) {
                    break;
                }
                sb = new StringBuilder(255);
                comment = false;
                continue;
            }

            if (Character.isWhitespace(c)) {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }

        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    private int read() throws IOException {
        int result = -1;

        if (lookaheadChar != -1) {
            result = lookaheadChar;
            lookaheadChar = -1;
        } else {
            result = reader.read();
        }

        return result;
    }

    private int lookahead() throws IOException {
        if (lookaheadChar == -1) {
            lookaheadChar = reader.read();
        }
        return lookaheadChar;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
