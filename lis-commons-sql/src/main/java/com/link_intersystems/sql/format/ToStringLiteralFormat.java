package com.link_intersystems.sql.format;

/**
 * Formats an objects to either it's {@link Object#toString()} representation or to {@link LiteralFormat#NULL_LITERAL}
 * if it is null;
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ToStringLiteralFormat extends AbstractLiteralFormat {
    public static final ToStringLiteralFormat INSTANCE = new ToStringLiteralFormat();

    @Override
    public String doFormat(Object value) {
        return String.valueOf(value);
    }
}
