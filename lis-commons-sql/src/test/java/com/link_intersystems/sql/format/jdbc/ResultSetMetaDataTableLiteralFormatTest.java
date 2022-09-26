package com.link_intersystems.sql.format.jdbc;

import com.link_intersystems.sql.format.DecimalLiteralFormat;
import com.link_intersystems.sql.format.LiteralFormatRegistry;
import com.link_intersystems.sql.statement.ColumnValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ResultSetMetaDataTableLiteralFormatTest {

    private ResultSetMetaData resultSetMetaData;
    private ResultSetMetaDataTableLiteralFormat literalFormat;

    @BeforeEach
    void setUp() {
        resultSetMetaData = mock(ResultSetMetaData.class);
        literalFormat = new ResultSetMetaDataTableLiteralFormat(resultSetMetaData);
    }

    @Test
    void formatUnknownColumn() throws Exception {
        ColumnValue columnValue = new ColumnValue("amount", new BigDecimal("123.456"));
        assertThrows(IllegalArgumentException.class, () -> literalFormat.format(columnValue));

    }


    @Test
    void format() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnName(1)).thenReturn("amount");
        when(resultSetMetaData.getColumnType(1)).thenReturn(Types.DECIMAL);

        LiteralFormatRegistry<Integer> literalRegistry = new JdbcLiteralFormatRegistry();
        literalFormat.setLiteralFormatRegistry(literalRegistry);

        ColumnValue columnValue = new ColumnValue("amount", new BigDecimal("123.456"));
        String formatted = literalFormat.format(columnValue);

        assertEquals("123.456", formatted);
    }

    @Test
    void defaultFormat() throws Exception {
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnName(1)).thenReturn("amount");
        when(resultSetMetaData.getColumnType(1)).thenReturn(Types.DECIMAL);

        literalFormat.setLiteralFormatRegistry(mock(LiteralFormatRegistry.class));
        literalFormat.setDefaultLiteralFormat(new DecimalLiteralFormat());

        ColumnValue columnValue = new ColumnValue("amount", new BigDecimal("123.456"));
        String formatted = literalFormat.format(columnValue);

        assertEquals("123.456", formatted);
    }

}