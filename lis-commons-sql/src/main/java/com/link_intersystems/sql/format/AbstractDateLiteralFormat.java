package com.link_intersystems.sql.format;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractDateLiteralFormat extends AbstractLiteralFormat {

    private QuotedStringLiteralFormat stringLiteralFormat = new QuotedStringLiteralFormat();

    private DateTimeFormatter dateTimeFormatter;
    private Supplier<ZoneId> zoneIdSupplier;


    public AbstractDateLiteralFormat(DateTimeFormatter dateTimeFormatter, Supplier<ZoneId> zoneIdSupplier) {
        this.dateTimeFormatter = Objects.requireNonNull(dateTimeFormatter);
        this.zoneIdSupplier = Objects.requireNonNull(zoneIdSupplier);
    }

    @Override
    public String doFormat(Object value) throws Exception {
        LocalDateTime localDateTime = toLocalDateTime(value);
        String formattedDate = dateTimeFormatter.format(localDateTime);
        return stringLiteralFormat.doFormat(formattedDate);
    }

    protected LocalDateTime toLocalDateTime(Object value) {
        ZoneId zoneId = zoneIdSupplier.get();
        Instant instant = toInstant(value, zoneId);
        if (instant == null) {
            String msg = MessageFormat.format("Value {0} can not be formatted as date," +
                            " because it could not be converted to an {1}, by this {2}",
                    value.getClass(),
                    Instant.class.getName(),
                    getClass().getName()
            );
            throw new IllegalArgumentException(msg);
        }
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime;
    }

    protected abstract Instant toInstant(Object value, ZoneId zoneId);
}
