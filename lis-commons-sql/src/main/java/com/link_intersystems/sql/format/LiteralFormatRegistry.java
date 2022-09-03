package com.link_intersystems.sql.format;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface LiteralFormatRegistry<T> {

    public LiteralFormat getLiteralFormat(T typeDescriptor);
}
