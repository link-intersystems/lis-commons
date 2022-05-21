package com.link_intersystems.jdbc.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ToStringLiteralFormat extends AbstractLiteralFormat {
    @Override
    public String doFormat(Object value) throws Exception {
        return String.valueOf(value);
    }
}
