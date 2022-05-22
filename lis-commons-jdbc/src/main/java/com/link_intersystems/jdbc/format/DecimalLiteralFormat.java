package com.link_intersystems.jdbc.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DecimalLiteralFormat extends AbstractLiteralFormat {

    public static final int DEFAULT_PRECISION = 10;

    private DecimalFormat decimalFormat;

    public static DecimalFormatSymbols defaultFormatSymbols() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        return symbols;
    }

    public static String generatePattern(int precision) {
        char[] decimalPlaces = new char[precision];
        Arrays.fill(decimalPlaces, '#');
        StringBuilder sb = new StringBuilder("###.");
        sb.append(decimalPlaces);
        return sb.toString();
    }

    public DecimalLiteralFormat() {
        this(DEFAULT_PRECISION);
    }

    public DecimalLiteralFormat(int precision) {
        this(precision, defaultFormatSymbols());
    }

    public DecimalLiteralFormat(int precision, DecimalFormatSymbols symbols) {
        this(generatePattern(precision), symbols);
    }

    public DecimalLiteralFormat(String pattern, DecimalFormatSymbols symbols) {
        this(new DecimalFormat(pattern, symbols));
    }

    public DecimalLiteralFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = Objects.requireNonNull(decimalFormat);
    }

    @Override
    protected String doFormat(Object value) {
        if (value instanceof BigDecimal) {
            return decimalFormat.format(value);
        } else if (value instanceof Double) {
            Double doubleValue = (Double) value;
            return Double.toString(doubleValue);
        } else if (value instanceof Float) {
            Float floatValue = (Float) value;
            return Float.toString(floatValue);
        }
        return null;
    }
}
