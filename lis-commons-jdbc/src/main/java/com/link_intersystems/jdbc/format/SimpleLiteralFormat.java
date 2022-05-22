package com.link_intersystems.jdbc.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SimpleLiteralFormat extends AbstractLiteralFormat {
    @Override
    public String doFormat(Object value) {
        return String.valueOf(value);
    }
}
