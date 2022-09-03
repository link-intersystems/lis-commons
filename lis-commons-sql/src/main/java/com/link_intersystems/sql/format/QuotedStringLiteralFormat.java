package com.link_intersystems.sql.format;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class QuotedStringLiteralFormat extends AbstractLiteralFormat {

    private String quote;

    public QuotedStringLiteralFormat() {
        this("'");
    }

    public QuotedStringLiteralFormat(String quote) {
        this.quote = requireNonNull(quote);
    }

    @Override
    protected String doFormat(Object value) {
        String valueAsString = String.valueOf(value);
        return quote + valueAsString + quote;
    }
}
