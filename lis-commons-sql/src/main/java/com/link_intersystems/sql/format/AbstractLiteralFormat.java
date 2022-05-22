package com.link_intersystems.sql.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractLiteralFormat implements LiteralFormat {
    @Override
    public String format(Object value) throws Exception {
        if (value == null) {
            return NULL_LITERAL;
        }

        return doFormat(value);
    }

    protected abstract String doFormat(Object value) throws Exception;
}
