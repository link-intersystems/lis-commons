package com.link_intersystems.jdbc.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BigDecimalLiteralFormat extends AbstractLiteralFormat {

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

    public BigDecimalLiteralFormat() {
        this(DEFAULT_PRECISION);
    }

    public BigDecimalLiteralFormat(int precision) {
        this(precision, defaultFormatSymbols());
    }

    public BigDecimalLiteralFormat(int precision, DecimalFormatSymbols symbols) {
        this(generatePattern(precision), symbols);
    }

    public BigDecimalLiteralFormat(String pattern, DecimalFormatSymbols symbols) {
        this(new DecimalFormat(pattern, symbols));
    }

    public BigDecimalLiteralFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = Objects.requireNonNull(decimalFormat);
    }

    @Override
    protected String doFormat(Object value) throws Exception {
        if (value instanceof BigDecimal) {
            return decimalFormat.format(value);
        }
        return null;
    }
}
