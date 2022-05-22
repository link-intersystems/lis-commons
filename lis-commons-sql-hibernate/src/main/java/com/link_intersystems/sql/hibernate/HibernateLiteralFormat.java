package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.LiteralType;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateLiteralFormat implements LiteralFormat {

    private LiteralType literalType;
    private final Dialect dialect;

    public HibernateLiteralFormat(LiteralType literalType, Dialect dialect) {
        this.literalType = literalType;
        this.dialect = dialect;
    }

    @Override
    public String format(Object value) throws Exception {
        return literalType.objectToSQLString(value, dialect);
    }
}
