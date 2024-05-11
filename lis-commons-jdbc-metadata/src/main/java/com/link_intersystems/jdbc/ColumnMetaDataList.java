package com.link_intersystems.jdbc;

import java.util.AbstractList;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ColumnMetaDataList extends AbstractList<ColumnMetaData> {

    private List<ColumnMetaData> columnMetaDataList;

    public ColumnMetaDataList(List<ColumnMetaData> columnMetaDataList) {
        this.columnMetaDataList = columnMetaDataList;
    }

    public ColumnMetaData getByName(String columnName) {
        return stream().filter(c -> c.getColumnName().equals(columnName)).findFirst().orElse(null);
    }

    public ColumnMetaData getByDescription(ColumnDescription columnDescription) {
        ColumnDescriptionEquality equality = new ColumnDescriptionEquality(columnDescription);
        return stream().filter(equality::equalsDescription).findFirst().orElse(null);
    }

    @Override
    public ColumnMetaData get(int index) {
        return columnMetaDataList.get(index);
    }

    @Override
    public int size() {
        return columnMetaDataList.size();
    }
}
