package com.link_intersystems.jdbc.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class BigDecimalLiteralFormatTest {

    private BigDecimalLiteralFormat literalFormat;
    private DecimalFormat decimalFormat;

    @BeforeEach
    void setUp() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0##";

        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        literalFormat = new BigDecimalLiteralFormat();
    }

    @Test
    void doFormat() throws Exception {
        BigDecimal bigDecimal = parse("10,692,467,440,017.1201234230");

        String formatted = literalFormat.doFormat(bigDecimal);

        assertEquals("10692467440017.120123423", formatted);
    }

    private BigDecimal parse(String text) throws ParseException {
        return (BigDecimal) decimalFormat.parse(text);
    }
}