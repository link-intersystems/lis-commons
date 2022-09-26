package com.link_intersystems.sql.format;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class DecimalLiteralFormatTest {

    private DecimalLiteralFormat literalFormat;
    private DecimalFormat decimalFormat;

    @BeforeEach
    void setUp() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0##";

        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        literalFormat = new DecimalLiteralFormat();
    }

    @Test
    void formatBigDecimal() throws Exception {
        BigDecimal bigDecimal = parse("10,692,467,440,017.1201234230");

        String formatted = literalFormat.doFormat(bigDecimal);

        assertEquals("10692467440017.120123423", formatted);
    }

    private BigDecimal parse(String text) throws ParseException {
        return (BigDecimal) decimalFormat.parse(text);
    }

    @Test
    void formatDouble() throws Exception {
        String formatted = literalFormat.doFormat(23.456);

        assertEquals("23.456", formatted);
    }

    @Test
    void formatFloat() throws Exception {
        String formatted = literalFormat.doFormat(23.456f);

        assertEquals("23.456", formatted);
    }

    @Test
    void formatUnknown() throws Exception {
        String formatted = literalFormat.doFormat("");

        assertNull(formatted);
    }
}