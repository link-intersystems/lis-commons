package com.link_intersystems.jdbc.format;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DateLiteralFormat extends AbstractDateLiteralFormat {


    public DateLiteralFormat() {
        this(DateTimeFormatter.ofPattern("yyyy-MM-dd"), ZoneId.systemDefault());
    }

    public DateLiteralFormat(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this(dateTimeFormatter, () -> zoneId);
    }

    public DateLiteralFormat(DateTimeFormatter dateTimeFormatter, Supplier<ZoneId> zoneIdSupplier) {
        super(dateTimeFormatter, zoneIdSupplier);
    }

    @Override
    protected Instant toInstant(Object value, ZoneId zoneId) {
        if (value instanceof java.sql.Date) {
            java.sql.Date sqlDate = (java.sql.Date) value;
            return sqlDate.toLocalDate().atStartOfDay(zoneId).toInstant();
        } else if (value instanceof Date) {
            Date date = (Date) value;
            return date.toInstant().atZone(zoneId).toInstant();
        }
        return null;
    }
}
