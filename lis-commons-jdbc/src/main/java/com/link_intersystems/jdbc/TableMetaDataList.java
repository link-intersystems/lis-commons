package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.link_intersystems.jdbc.ResultSetMappers.TABLE_META_DATA_MAPPER;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TableMetaDataList extends AbstractList<TableMetaData> {

    private static class QualifiedTableNameMatch {
        private QualifiedTableName qualifiedTableName;

        public QualifiedTableNameMatch(QualifiedTableName qualifiedTableName) {
            this.qualifiedTableName = qualifiedTableName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QualifiedTableNameMatch that = (QualifiedTableNameMatch) o;
            return Objects.equals(qualifiedTableName, that.qualifiedTableName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(qualifiedTableName.getTableName());
        }
    }

    private List<TableMetaData> tableMetaDataList;

    private Map<String, Collection<TableMetaData>> tableMetaDatasByName;

    public TableMetaDataList(ResultSet tablesResultSet) throws SQLException {
        this.tableMetaDataList = TABLE_META_DATA_MAPPER.map(tablesResultSet);
    }

    public TableMetaDataList(List<TableMetaData> tableMetaData) {
        this.tableMetaDataList = new ArrayList<>(tableMetaData);
    }

    public TableMetaData getByName(String tableName) {
        return getByName(null, null, tableName);
    }

    public TableMetaData getByName(String schema, String tableName) {
        return getByName(null, schema, tableName);
    }

    public TableMetaData getByName(String catalog, String schema, String tableName) {
        return getByName(new QualifiedTableName(catalog, schema, tableName));
    }

    public TableMetaData getByName(QualifiedTableName qualifiedTableName) {
        List<TableMetaData> matchingTableMetaDatas = findMatchingTableMetaDatas(qualifiedTableName);

        if (matchingTableMetaDatas.isEmpty()) {
            return null;
        }

        if (matchingTableMetaDatas.size() > 1) {
            throw new AmbiguousTableNameException(qualifiedTableName.getTableName());
        }

        return matchingTableMetaDatas.get(0);
    }

    private List<TableMetaData> findMatchingTableMetaDatas(QualifiedTableName qualifiedTableName) {
        Map<String, Collection<TableMetaData>> tableMetasDataByName = getTableMetasDataByName();

        List<TableMetaData> matchingTableMetaDatas = new ArrayList<>();

        String tableName = qualifiedTableName.getTableName();
        Collection<TableMetaData> tableMetaDatas = tableMetasDataByName.get(tableName);

        for (TableMetaData tableMetaData : tableMetaDatas) {
            if (tableMetaData.matches(qualifiedTableName)) {
                matchingTableMetaDatas.add(tableMetaData);
            }
        }

        return matchingTableMetaDatas;
    }

    public Map<String, Collection<TableMetaData>> getTableMetasDataByName() {
        if (tableMetaDatasByName == null) {
            tableMetaDatasByName = new HashMap<>();

            forEach(tm -> {
                Collection<TableMetaData> tableMetaDatas = tableMetaDatasByName.computeIfAbsent(tm.getTableName(), k -> new ArrayList<>());
                tableMetaDatas.add(tm);
            });
        }
        return tableMetaDatasByName;
    }

    @Override
    public TableMetaData get(int index) {
        return tableMetaDataList.get(index);
    }

    @Override
    public int size() {
        return tableMetaDataList.size();
    }
}
