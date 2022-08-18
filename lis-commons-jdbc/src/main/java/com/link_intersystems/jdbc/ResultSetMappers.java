package com.link_intersystems.jdbc;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface ResultSetMappers {

    public static final ResultSetMapper<TableMetaData> TABLE_META_DATA_MAPPER = new ResultSetMapper<>(TableMetaData::new);
    public static final ResultSetMapper<ColumnMetaData> COLUMN_META_DATA_MAPPER = new ResultSetMapper<>(ColumnMetaData::new);
    public static final ResultSetMapper<PrimaryKeyColumn> PRIMARY_KEY_MAPPER = new ResultSetMapper<>(PrimaryKeyColumn::new);
    public static final ResultSetMapper<ForeignKeyEntry> FOREIGN_KEY_MAPPER = new ResultSetMapper<>(ForeignKeyEntry::new);
}
