package com.link_intersystems.sql.hibernate;

import com.link_intersystems.sql.format.LiteralFormat;
import com.link_intersystems.sql.format.TimestampLiteralFormat;
import org.hibernate.dialect.Dialect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HibernateLiteralFormatOverride {

    private Map<String, LiteralFormat> overrides = new HashMap<>();

    public HibernateLiteralFormatOverride() {
        overrides.put("timestamp", new TimestampLiteralFormat());
    }

    public LiteralFormat getLiteralType(Dialect dialect, String hibernateType) {
        return overrides.get(hibernateType);
    }
}
