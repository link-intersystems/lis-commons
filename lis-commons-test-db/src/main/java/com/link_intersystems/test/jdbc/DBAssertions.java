package com.link_intersystems.test.jdbc;

import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DBAssertions {

    private Connection connection;

    public DBAssertions(Connection connection) {
        assertNotNull(connection, "connection");
        this.connection = connection;
    }

    public void assertTableExists(String tableName) throws SQLException {
        ResultSet tables = queryTableMetaData(tableName);
        assertTrue(tables.next(), tableName + " exists");
    }

    protected ResultSet queryTableMetaData(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        return metaData.getTables(null, "%", tableName, new String[]{"TABLE"});
    }

    public void assertTableNotExists(String tableName) throws SQLException {
        ResultSet tables = queryTableMetaData(tableName);
        assertFalse(tables.next(), tableName + " exists");
    }

    public void assertSchemaNotExists(String schema) throws SQLException {
        ResultSet schemas = querySchemaMetaData(schema);
        assertFalse(schemas.next(), schema + " exists");
    }

    protected ResultSet querySchemaMetaData(String schema) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        return metaData.getSchemas(null, schema);
    }

    public void assertRowCount(String tableName, int expectedRowCount) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SELECT count(*) FROM " + tableName);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int rowCount = resultSet.getInt(1);
            assertEquals(expectedRowCount, rowCount, tableName + " row count");
        }
    }

    /**
     * @param tableName
     * @param id        the id of the row as defined by the primary key definition of the database. Multiple values are allowed
     *                  in case of a composite id.
     * @return
     * @throws SQLException
     */
    public RowAssertions assertRowExists(String tableName, Object... id) throws SQLException {

        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(tableName);
        sb.append(" where ");

        List<String> primaryKeyColumns = getPrimaryKeyColumns(tableName);

        sb.append("(");
        sb.append(primaryKeyColumns.stream().collect(Collectors.joining(", ")));
        sb.append(") = (");
        sb.append(Collections.nCopies(id.length, "?").stream().collect(Collectors.joining(", ")));
        sb.append(")");
        try (PreparedStatement ps = connection.prepareStatement(sb.toString())) {
            int i = 1;
            for (Object compositeIdValue : id) {
                ps.setObject(i++, compositeIdValue);
            }

            if (ps.execute()) {
                ResultSet resultSet = ps.getResultSet();
                assertTrue(resultSet.next(), "Table " + tableName + " row with id " + Arrays.asList(id) + " exists");
                return new RowAssertions(resultSet);
            }
        }
        fail("No row with id " + Arrays.asList(id) + " exists for fable " + tableName);
        return null;
    }


    private List<String> getPrimaryKeyColumns(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        String schema = getSchema(tableName);
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, schema, tableName);
        List<String> primaryKeyColumns = new ArrayList<>();
        List<Short> primaryKeyColumnSeq = new ArrayList<>();
        while (primaryKeys.next()) {
            String columnName = primaryKeys.getString("COLUMN_NAME");
            primaryKeyColumns.add(columnName);

            Short keySequenceNumber = primaryKeys.getShort("KEY_SEQ");
            primaryKeyColumnSeq.add(keySequenceNumber);
        }

        for (int index = 0; index < primaryKeyColumnSeq.size(); index++) {
            Short seqAtIndex = primaryKeyColumnSeq.get(index);
            String columnAtIndex = primaryKeyColumns.get(index);

            String previousColumn = primaryKeyColumns.set(seqAtIndex.intValue() - 1, columnAtIndex);
            primaryKeyColumns.set(index, previousColumn);
        }

        return primaryKeyColumns;
    }

    private String getSchema(String tableName) {
        int dotIndex = tableName.indexOf('.');
        if (dotIndex > -1) {
            return tableName.substring(0, dotIndex);
        }
        return null;
    }
}
