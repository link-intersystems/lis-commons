package com.link_intersystems.jdbc.meta;

import com.link_intersystems.jdbc.meta.*;
import com.link_intersystems.test.UnitTest;
import com.link_intersystems.test.db.sakila.SakilaTestDBExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaTestDBExtension.class)
@UnitTest
class ConnectionMetaDataTest {

    private ConnectionMetaData metaDataRepository;

    @BeforeEach
    void setUp(Connection connection) {
        JdbcContext.Builder builder = new JdbcContext.Builder();
        builder.setSchema("sakila");
        JdbcContext jdbcContext = builder.build();
        metaDataRepository = new ConnectionMetaData(connection, jdbcContext);
    }

    @Test
    void tableMetaData() throws SQLException {
        TableMetaData actor = metaDataRepository.getTableMetaData("actor");

        assertEquals("sakila", actor.getSchemaName());
        assertEquals("actor", actor.getTableName());
    }

    @Test
    void primaryKey() throws SQLException {
        PrimaryKey primaryKey = metaDataRepository.getPrimaryKey("film_actor");

        assertEquals(2, primaryKey.size());

        assertEquals("actor_id", primaryKey.get(0).getColumnName());
        assertEquals("film_id", primaryKey.get(1).getColumnName());

    }

    @Test
    void columnMetaData() throws SQLException {
        ColumnMetaDataList actorColumnMetaData = metaDataRepository.getColumnMetaDataList("actor");

        assertEquals(4, actorColumnMetaData.size());

        ColumnMetaData firstNameColumnMetaData = actorColumnMetaData.getByName("first_name");
        assertEquals(Types.VARCHAR, firstNameColumnMetaData.getDataType());
        assertEquals(45, firstNameColumnMetaData.getColumnSize());
        assertEquals("NO", firstNameColumnMetaData.getIsNullable());
        assertEquals(DatabaseMetaData.columnNoNulls, firstNameColumnMetaData.getNullable());
    }


    @Test
    void foreignKey() throws SQLException {
        List<ForeignKey> foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        assertEquals(2, foreignKeys.size());
        ForeignKey fk_film_actor_actor = foreignKeys.stream().filter(fk -> fk.getName().equals("fk_film_actor_actor")).findFirst().orElse(null);

        assertEquals(1, fk_film_actor_actor.size());
        ForeignKeyEntry foreignKeyEntry = fk_film_actor_actor.get(0);
        assertEquals("actor_id", foreignKeyEntry.getFkColumnName());
        assertEquals("film_actor", foreignKeyEntry.getFkTableName());
        assertEquals("actor_id", foreignKeyEntry.getPkColumnName());
        assertEquals("actor", foreignKeyEntry.getPkTableName());
    }

    @Test
    void foreignKeyByColumnDescription() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        ColumnMetaDataList filmActorColumns = metaDataRepository.getColumnMetaDataList("film_actor");
        ForeignKey foreignKey = foreignKeys.getByFkColumnDescription(filmActorColumns.getByName("actor_id"));
        assertNotNull(foreignKey);
        assertEquals("fk_film_actor_actor", foreignKey.getName());

        ColumnMetaDataList actorColumns = metaDataRepository.getColumnMetaDataList("actor");
        foreignKey = foreignKeys.getByPkColumnDescription(actorColumns.getByName("actor_id"));
        assertNotNull(foreignKey);
        assertEquals("fk_film_actor_actor", foreignKey.getName());
    }

    @Test
    void foreignKeys() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        assertEquals(2, foreignKeys.size());

        ColumnMetaDataList columnMetaDataList = metaDataRepository.getColumnMetaDataList("actor");
        ColumnMetaData actorIdColumn = columnMetaDataList.getByName("actor_id");

        ForeignKey foreignKey = foreignKeys.getByPkColumnDescription(actorIdColumn);
        assertNotNull(foreignKey);
        assertEquals("fk_film_actor_actor", foreignKey.getName());
    }
}