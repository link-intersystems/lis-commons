package com.link_intersystems.sql.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface LiteralFormat {

    public static final String NULL_LITERAL = "null";

    public String format(Object value) throws Exception;
}
