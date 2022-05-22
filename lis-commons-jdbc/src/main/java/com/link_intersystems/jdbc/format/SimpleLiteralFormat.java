package com.link_intersystems.jdbc.format;

/**
 * Formats an objects to either it's {@link Object#toString()} representation or to {@link LiteralFormat#NULL_LITERAL}
 * if it is null;
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SimpleLiteralFormat extends AbstractLiteralFormat {
    @Override
    public String doFormat(Object value) {
        return String.valueOf(value);
    }
}
