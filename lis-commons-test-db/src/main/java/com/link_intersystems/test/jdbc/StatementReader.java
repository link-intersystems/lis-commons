package com.link_intersystems.test.jdbc;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Predicate;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class StatementReader implements Closeable {

    private Reader reader;
    private Predicate<String> statementFiler = s -> true;

    private String nextStatement;

    public StatementReader(Reader reader) {
        this.reader = reader;
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

        int commentCharCount = 0;
        int whitespaceCount = 0;

        while (true) {
            int read = reader.read();
            if (read == '\n') {
                commentCharCount = 0;
                continue;
            }
            if (read == '-' && commentCharCount < 2) {
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
                if(read != -1 && sb.length() == 0){
                    continue;
                }
                if (statementFiler.test(sb.toString())) {
                    break;
                }
                commentCharCount = 0;
                whitespaceCount = 0;
                sb = new StringBuilder(255);
            }

            if (Character.isWhitespace(read)) {
                whitespaceCount++;
            } else {
                if (whitespaceCount > 0) {
                    sb.append(' ');
                    whitespaceCount = 0;
                }
                sb.append((char) read);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
