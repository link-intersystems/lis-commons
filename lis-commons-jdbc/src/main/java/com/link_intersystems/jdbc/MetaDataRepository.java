package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MetaDataRepository {

    private List<TableMetaData> tableMetaDataList;
    private Map<String, ColumnMetaDataList> columnMetaDataListByTableName = new HashMap<>();
    private Map<String, PrimaryKey> primaryKeysByTableName = new HashMap<>();

    private Connection connection;
    private String[] tableTypes;

    private JdbcContext context;


    public MetaDataRepository(Connection connection) {
        this(connection, new String[]{"TABLE"});
    }

    public MetaDataRepository(Connection connection, String... tableTypes) {
        this(connection, null, tableTypes);
    }

    public MetaDataRepository(Connection connection, JdbcContext context) {
        this(connection, context, "TABLE");
    }

    public MetaDataRepository(Connection connection, JdbcContext context, String... tableTypes) {
        this.connection = connection;
        this.context = context;
        this.tableTypes = tableTypes;
    }

    public List<TableMetaData> getTableMetaDataList() throws SQLException {
        if (tableMetaDataList == null) {
            ScopedDatabaseMetaData metaData = getScopedDatabaseMetaData();
            ResultSet tablesResultSet = metaData.getTables("%", tableTypes);
            tableMetaDataList = mapResultSet(tablesResultSet, TableMetaData::new);
        }
        return tableMetaDataList;
    }

    private ScopedDatabaseMetaData getScopedDatabaseMetaData() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        JdbcContext context = getContext();
        return new ScopedDatabaseMetaData(metaData, context.getCatalog(), context.getSchema());
    }

    private JdbcContext getContext() throws SQLException {
        if (context == null) {
            JdbcContext.Builder builder = new JdbcContext.Builder();
            builder.setCatalog(connection.getCatalog());
            builder.setSchema(connection.getSchema());
            context = builder.build();
        }
        return context;
    }

    public TableMetaData getTableMetaData(String tableName) throws SQLException {
        return getTableMetaDataList().stream().filter(jtmd -> jtmd.getTableName().equals(tableName)).findFirst().orElse(null);
    }

    public List<ColumnMetaData> getColumnMetaDataList(TableMetaData jdbcTableMetaData) throws SQLException {
        return getColumnMetaDataList(jdbcTableMetaData.getTableName());
    }

    public PrimaryKey getPrimaryKey(String tableName) throws SQLException {
        if (!primaryKeysByTableName.containsKey(tableName)) {
            ColumnMetaDataList columnMetaDataList = getColumnMetaDataList(tableName);
            ScopedDatabaseMetaData metaData = getScopedDatabaseMetaData();
            ResultSet resultSet = metaData.getPrimaryKeys(tableName);
            List<PrimaryKeyColumn> primaryKeyColumns = mapResultSet(resultSet, PrimaryKeyColumn::new);
            PrimaryKey primaryKey = null;

            if (primaryKeyColumns.size() > 0) {
                Collections.sort(primaryKeyColumns);
                List<ColumnMetaData> pkColumnMetaData = new ArrayList<>();
                for (PrimaryKeyColumn primaryKeyColumn : primaryKeyColumns) {
                    ColumnMetaData pkColumn = columnMetaDataList.getByName(primaryKeyColumn.getColumnName());
                    pkColumnMetaData.add(pkColumn);
                }
                PrimaryKeyColumn primaryKeyColumn = primaryKeyColumns.get(0);
                String primaryKeyName = primaryKeyColumn.getPrimaryKeyName();
                primaryKey = new PrimaryKey(primaryKeyName, new ColumnMetaDataList(pkColumnMetaData));
            }

            primaryKeysByTableName.put(tableName, primaryKey);
        }
        return primaryKeysByTableName.get(tableName);
    }

    public ColumnMetaDataList getColumnMetaDataList(String tableName) throws SQLException {
        if (!columnMetaDataListByTableName.containsKey(tableName)) {
            List<ColumnMetaData> columnMetaDatas = createColumnMetaData(tableName);
            columnMetaDataListByTableName.put(tableName, new ColumnMetaDataList(columnMetaDatas));
        }
        return columnMetaDataListByTableName.get(tableName);

    }

    private List<ColumnMetaData> createColumnMetaData(String tableName) throws SQLException {
        ScopedDatabaseMetaData scopedDatabaseMetaData = getScopedDatabaseMetaData();
        ResultSet columnsMetaDataResultSet = scopedDatabaseMetaData.getColumns(tableName);

        return mapResultSet(columnsMetaDataResultSet, ColumnMetaData::new);
    }


    public ForeignKeyList getExportedKeys(String tableName) throws SQLException {
        ScopedDatabaseMetaData scopedDatabaseMetaData = getScopedDatabaseMetaData();
        ResultSet resultSet = scopedDatabaseMetaData.getExportedKeys(tableName);
        List<ForeignKeyEntry> jdbcForeignKeyEntries = mapResultSet(resultSet, ForeignKeyEntry::new);

        return new ForeignKeyList(mapToForeignKeys(jdbcForeignKeyEntries));
    }

    private List<ForeignKey> mapToForeignKeys(List<ForeignKeyEntry> jdbcForeignKeyEntries) {
        Map<String, List<ForeignKeyEntry>> foreignKeys = jdbcForeignKeyEntries.stream().collect(Collectors.groupingBy(ForeignKeyEntry::getFkName));
        return foreignKeys.values().stream().map(ForeignKey::new).collect(Collectors.toList());
    }


    public ForeignKeyList getImportedKeys(String tableName) throws SQLException {
        ScopedDatabaseMetaData scopedDatabaseMetaData = getScopedDatabaseMetaData();
        ResultSet resultSet = scopedDatabaseMetaData.getImportedKeys(tableName);
        List<ForeignKeyEntry> jdbcForeignKeyEntries = mapResultSet(resultSet, ForeignKeyEntry::new);
        return new ForeignKeyList(mapToForeignKeys(jdbcForeignKeyEntries));
    }


    @FunctionalInterface
    private static interface ElementFactory<T> {

        public T apply(ResultSet resultSet) throws SQLException;
    }

    private <T> List<T> mapResultSet(ResultSet resultSet, ElementFactory<T> elementFactory) throws SQLException {
        List<T> mappedResultSet = new ArrayList<>();

        while (resultSet.next()) {
            T element = elementFactory.apply(resultSet);
            mappedResultSet.add(element);
        }

        return mappedResultSet;
    }
}
