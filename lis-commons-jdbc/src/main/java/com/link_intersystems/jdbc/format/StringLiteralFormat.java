package com.link_intersystems.jdbc.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class StringLiteralFormat extends AbstractLiteralFormat {

    private ToStringLiteralFormat toStringLiteralFormat = new ToStringLiteralFormat();

    @Override
    protected String doFormat(Object value) throws Exception {
        String formatted = toStringLiteralFormat.doFormat(value);
        return "'" + formatted + "'";
    }
}
