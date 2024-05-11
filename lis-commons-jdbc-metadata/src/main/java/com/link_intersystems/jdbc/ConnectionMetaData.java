package com.link_intersystems.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.link_intersystems.jdbc.ResultSetMappers.*;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * {@link ConnectionMetaData} provides convenient api to access the meta-data of a jdbc connection.
 *
 * <pre>
 * Connection jdbcConnection = ...;
 * ConnectionMetaData metaData = new ConnectionMetaData(jdbcConnection);
 *
 * ForeignKeyList foreignKeys = metaData.getImportedKeys("film_actor");
 *
 * ColumnMetaDataList filmActorColumns = metaData.getColumnMetaDataList("film_actor");
 * ForeignKey foreignKey = foreignKeys.getByFkColumnDescription(filmActorColumns.getByName("actor_id"));
 * assertNotNull(foreignKey);
 * assertEquals("fk_film_actor_actor", foreignKey.getName());
 *
 * ColumnMetaDataList actorColumns = metaData.getColumnMetaDataList("actor");
 * foreignKey = foreignKeys.getByPkColumnDescription(actorColumns.getByName("actor_id"));
 * assertNotNull(foreignKey);
 * assertEquals("fk_film_actor_actor", foreignKey.getName());
 * </pre>
 * <img src="doc-files/sakila.png" alt="Sakila Database Diagram"/>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ConnectionMetaData implements TableReferenceMetaData {

    private TableMetaDataList tableMetaDataList;
    private Map<String, ColumnMetaDataList> columnMetaDataListByTableName = new HashMap<>();
    private Map<String, PrimaryKey> primaryKeysByTableName = new HashMap<>();
    private Map<String, ForeignKeyList> exportedForeignKeysByTableName = new HashMap<>();
    private Map<String, ForeignKeyList> importedForeignKeysByTableName = new HashMap<>();
    private Map<String, TableReferenceList> outgoingReferences = new HashMap<>();
    private Map<String, TableReferenceList> incommingReferences = new HashMap<>();

    private Connection connection;
    private String[] tableTypes;

    private JdbcContext context;

    public ConnectionMetaData(Connection connection) {
        this(connection, new String[]{"TABLE"});
    }

    public ConnectionMetaData(Connection connection, String... tableTypes) {
        this(connection, null, tableTypes);
    }

    public ConnectionMetaData(Connection connection, JdbcContext context) {
        this(connection, context, "TABLE");
    }

    public ConnectionMetaData(Connection connection, JdbcContext context, String... tableTypes) {
        this.connection = requireNonNull(connection);
        this.context = context;
        this.tableTypes = requireNonNull(tableTypes);
    }

    /**
     * Convenience method for {@link #getTableMetaDataList()}.getByName(tableName).
     *
     * @param tableName
     * @return
     */
    public TableMetaData getTableMetaData(String tableName) throws SQLException {
        return getTableMetaDataList().getByName(tableName);
    }

    public TableMetaDataList getTableMetaDataList() throws SQLException {
        if (tableMetaDataList == null) {
            ScopedDatabaseMetaData metaData = getScopedDatabaseMetaData();
            ResultSet tablesResultSet = metaData.getTables("%", tableTypes);
            tableMetaDataList = new TableMetaDataList(tablesResultSet);
        }
        return tableMetaDataList;
    }

    protected ScopedDatabaseMetaData getScopedDatabaseMetaData() throws SQLException {
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

    public PrimaryKey getPrimaryKey(String tableName) throws SQLException {
        if (!primaryKeysByTableName.containsKey(tableName)) {
            ColumnMetaDataList columnMetaDataList = getColumnMetaDataList(tableName);
            ScopedDatabaseMetaData metaData = getScopedDatabaseMetaData();

            ResultSet resultSet = metaData.getPrimaryKeys(tableName);
            List<PrimaryKeyColumn> primaryKeyColumns = PRIMARY_KEY_MAPPER.map(resultSet);

            PrimaryKey primaryKey = createPrimaryKey(columnMetaDataList, primaryKeyColumns);

            primaryKeysByTableName.put(tableName, primaryKey);
        }
        return primaryKeysByTableName.get(tableName);
    }

    protected PrimaryKey createPrimaryKey(ColumnMetaDataList columnMetaDataList, List<PrimaryKeyColumn> primaryKeyColumns) {
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

        return primaryKey;
    }

    public List<ColumnMetaData> getColumnMetaDataList(TableMetaData jdbcTableMetaData) throws SQLException {
        return getColumnMetaDataList(jdbcTableMetaData.getTableName());
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

        return COLUMN_META_DATA_MAPPER.map(columnsMetaDataResultSet);
    }


    public ForeignKeyList getExportedKeys(String tableName) throws SQLException {
        if (!exportedForeignKeysByTableName.containsKey(tableName)) {
            ForeignKeyList foreignKeys = createForeignKeys(tableName);
            exportedForeignKeysByTableName.put(tableName, foreignKeys);
        }

        return exportedForeignKeysByTableName.get(tableName);
    }

    private ForeignKeyList createForeignKeys(String tableName) throws SQLException {
        ScopedDatabaseMetaData scopedDatabaseMetaData = getScopedDatabaseMetaData();
        ResultSet resultSet = scopedDatabaseMetaData.getExportedKeys(tableName);
        List<ForeignKeyEntry> jdbcForeignKeyEntries = FOREIGN_KEY_MAPPER.map(resultSet);

        return new ForeignKeyList(mapToForeignKeys(jdbcForeignKeyEntries));
    }

    private List<ForeignKey> mapToForeignKeys(List<ForeignKeyEntry> jdbcForeignKeyEntries) {
        Map<String, List<ForeignKeyEntry>> foreignKeys = jdbcForeignKeyEntries.stream().collect(Collectors.groupingBy(ForeignKeyEntry::getFkName));
        return foreignKeys.values().stream().map(ForeignKey::new).collect(toList());
    }


    public ForeignKeyList getImportedKeys(String tableName) throws SQLException {
        if (!importedForeignKeysByTableName.containsKey(tableName)) {
            ForeignKeyList foreignKeys = createImportedKeys(tableName);
            importedForeignKeysByTableName.put(tableName, foreignKeys);
        }

        return importedForeignKeysByTableName.get(tableName);
    }

    private ForeignKeyList createImportedKeys(String tableName) throws SQLException {
        ScopedDatabaseMetaData scopedDatabaseMetaData = getScopedDatabaseMetaData();
        ResultSet resultSet = scopedDatabaseMetaData.getImportedKeys(tableName);
        List<ForeignKeyEntry> jdbcForeignKeyEntries = FOREIGN_KEY_MAPPER.map(resultSet);
        return new ForeignKeyList(mapToForeignKeys(jdbcForeignKeyEntries));
    }

    @Override
    public TableReferenceList getOutgoingReferences(String tableName) throws SQLException {
        if (!outgoingReferences.containsKey(tableName)) {
            ForeignKeyList importedKeys = getImportedKeys(tableName);
            List<TableReference> outgoing = importedKeys.stream().map(TableReference::of).collect(toList());
            this.outgoingReferences.put(tableName, new TableReferenceList(outgoing));
        }
        return outgoingReferences.get(tableName);
    }

    @Override
    public TableReferenceList getIncomingReferences(String tableName) throws SQLException {
        if (!incommingReferences.containsKey(tableName)) {
            ForeignKeyList exportedKeys = getExportedKeys(tableName);
            List<TableReference> incoming = exportedKeys.stream().map(TableReference::of).collect(toList());
            this.incommingReferences.put(tableName, new TableReferenceList(incoming));
        }
        return incommingReferences.get(tableName);
    }


}
