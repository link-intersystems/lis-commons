package com.link_intersystems.sql.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class VarcharLiteralFormat extends AbstractLiteralFormat {

    @Override
    protected String doFormat(Object value) {
        String valueAsString = String.valueOf(value);
        return "'" + valueAsString + "'";
    }
}
