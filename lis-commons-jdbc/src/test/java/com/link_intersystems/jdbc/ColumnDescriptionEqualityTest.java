package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.ColumnDescription;
import com.link_intersystems.jdbc.ColumnDescriptionEquality;
import com.link_intersystems.jdbc.DefaultColumnDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ColumnDescriptionEqualityTest {

    private ColumnDescription desc1;
    private ColumnDescription desc2;
    private ColumnDescription notEqualDesc;

    @BeforeEach
    void setUp() {
        desc1 = new DefaultColumnDescription.Builder()
                .setCatalogName("catalogName")
                .setSchemaName("schemaName")
                .setTableName("tableName")
                .setColumnName("columnName")
                .build();

        desc2 = new DefaultColumnDescription.Builder()
                .setCatalogName("catalogName")
                .setSchemaName("schemaName")
                .setTableName("tableName")
                .setColumnName("columnName")
                .build();

        notEqualDesc = new DefaultColumnDescription.Builder()
                .setCatalogName("catalog")
                .setSchemaName("schema")
                .setTableName("table")
                .setColumnName("column")
                .build();


    }

    @Test
    void testStaticEquals() {
        assertTrue(ColumnDescriptionEquality.equals(desc1, desc1));
        assertTrue(ColumnDescriptionEquality.equals(desc1, desc2));
        assertTrue(ColumnDescriptionEquality.equals(desc2, desc1));
        assertTrue(ColumnDescriptionEquality.equals(desc2, desc2));

        assertFalse(ColumnDescriptionEquality.equals(desc1, notEqualDesc));
        assertFalse(ColumnDescriptionEquality.equals(notEqualDesc, desc1));
    }

    @Test
    void testEquals() {
        ColumnDescriptionEquality desc1Equality = new ColumnDescriptionEquality(desc1);

        assertTrue(desc1Equality.equalsDescription(desc2));

        assertFalse(desc1Equality.equalsDescription(notEqualDesc));
    }

}
