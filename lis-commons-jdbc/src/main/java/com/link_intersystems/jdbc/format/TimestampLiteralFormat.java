package com.link_intersystems.jdbc.format;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TimestampLiteralFormat extends AbstractDateLiteralFormat {

    private DateLiteralFormat dateLiteralFormat = new DateLiteralFormat();

    public TimestampLiteralFormat() {
        this(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), ZoneId.systemDefault());
    }

    public TimestampLiteralFormat(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        this(dateTimeFormatter, () -> zoneId);
    }

    public TimestampLiteralFormat(DateTimeFormatter dateTimeFormatter, Supplier<ZoneId> zoneIdSupplier) {
        super(dateTimeFormatter, zoneIdSupplier);
    }

    @Override
    public String doFormat(Object value) throws Exception {
        if (value instanceof java.sql.Date) {
            return dateLiteralFormat.format(value);
        }

        return super.doFormat(value);
    }

    @Override
    protected Instant toInstant(Object value, ZoneId zoneId) {
        if (value instanceof Date) {
            Date date = (Date) value;
            return date.toInstant().atZone(zoneId).toInstant();
        }
        return null;
    }
}
