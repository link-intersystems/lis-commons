package com.link_intersystems.sql.format.jdbc;

import com.link_intersystems.sql.format.LiteralFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class JdbcLiteralFormatRegistryTest {

    private JdbcLiteralFormatRegistry literalFormatRegistry;

    @BeforeEach
    void setUp() {
        literalFormatRegistry = new JdbcLiteralFormatRegistry();
    }

    @Test
    void varchar() throws Exception {
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(Types.VARCHAR);

        assertEquals("'Test'", literalFormat.format("Test"));
    }

    @Test
    void date() throws Exception {
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(Types.DATE);

        assertEquals("'2022-06-03'", literalFormat.format(new Date(122, 5, 3, 15, 22, 12)));
    }

    @Test
    void timestamp() throws Exception {
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(Types.TIMESTAMP);

        assertEquals("'2022-06-03 15:22:12'", literalFormat.format(new Timestamp(122, 5, 3, 15, 22, 12, 321)));
    }

    @Test
    void integer() throws Exception {
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(Types.BIGINT);

        assertEquals("123456789", literalFormat.format(new BigInteger("123456789")));
    }

    @Test
    void decimal() throws Exception {
        LiteralFormat literalFormat = literalFormatRegistry.getLiteralFormat(Types.DECIMAL);

        assertEquals("12345.6789", literalFormat.format(new BigDecimal("12345.6789")));
    }
}