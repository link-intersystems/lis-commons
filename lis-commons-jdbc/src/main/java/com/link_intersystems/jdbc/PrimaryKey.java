package com.link_intersystems.jdbc;

import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PrimaryKey extends ColumnMetaDataList {

    private String name;

    public PrimaryKey(String name, List<ColumnMetaData> columnMetaDataList) {
        super(columnMetaDataList);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
