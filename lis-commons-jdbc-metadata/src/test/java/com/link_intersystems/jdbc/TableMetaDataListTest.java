package com.link_intersystems.jdbc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TableMetaDataListTest {

    @Test
    void ambigiousTableName() {
        TableMetaData personTableInSchemaPublic = mock(TableMetaData.class);
        TableMetaData personTableInSchemaPrivate = mock(TableMetaData.class);

        when(personTableInSchemaPublic.getTableName()).thenReturn("person");
        when(personTableInSchemaPublic.getSchemaName()).thenReturn("public");
        when(personTableInSchemaPublic.matches(any())).thenCallRealMethod();

        when(personTableInSchemaPrivate.getTableName()).thenReturn("person");
        when(personTableInSchemaPrivate.getSchemaName()).thenReturn("private");
        when(personTableInSchemaPrivate.matches(any())).thenCallRealMethod();

        TableMetaDataList tableMetaDataList = new TableMetaDataList(Arrays.asList(personTableInSchemaPublic, personTableInSchemaPrivate));

        assertThrows(AmbiguousTableNameException.class, () -> tableMetaDataList.getByName("person"));
    }

    @Test
    void getTableMetaData() {
        TableMetaData personTableMetaData = mock(TableMetaData.class);

        when(personTableMetaData.getTableName()).thenReturn("person");
        when(personTableMetaData.getSchemaName()).thenReturn("public");
        when(personTableMetaData.matches(any())).thenCallRealMethod();

        TableMetaDataList tableMetaDataList = new TableMetaDataList(Arrays.asList(personTableMetaData));

        TableMetaData metaDataByName = tableMetaDataList.getByName("person");

        assertSame(personTableMetaData, metaDataByName);
    }
}