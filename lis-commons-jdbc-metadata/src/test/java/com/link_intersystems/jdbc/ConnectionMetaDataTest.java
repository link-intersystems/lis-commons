package com.link_intersystems.jdbc;

import com.link_intersystems.jdbc.test.db.h2.H2Config;
import com.link_intersystems.jdbc.test.db.h2.H2Extension;
import com.link_intersystems.jdbc.test.db.sakila.SakilaEmptyDatabaseFactory;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(H2Extension.class)
@H2Config(databaseFactory = SakilaEmptyDatabaseFactory.class)
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

        assertEquals("test", actor.getCatalogName());
        assertEquals("sakila", actor.getSchemaName());
        assertEquals("actor", actor.getTableName());
        assertEquals("BASE TABLE", actor.getTableType());
        assertEquals(null, actor.getTypeSchama());
        assertEquals(null, actor.getTypeCatalog());
        assertEquals(null, actor.getTypeName());
        assertEquals(null, actor.getSelfReferencingColumnName());
        assertEquals(null, actor.getRefGeneration());
        assertEquals(null, actor.getRemarks());
    }

    @Test
    void primaryKey() throws SQLException {
        PrimaryKey primaryKey = metaDataRepository.getPrimaryKey("film_actor");

        assertEquals(2, primaryKey.size());

        assertEquals("actor_id", primaryKey.get(0).getColumnName());
        assertEquals("film_id", primaryKey.get(1).getColumnName());

        assertEquals("pk_film_actor", primaryKey.getName());

    }

    @Test
    void columnMetaData() throws SQLException {
        ColumnMetaDataList columnMetaDataList = metaDataRepository.getColumnMetaDataList("film");

        assertEquals(13, columnMetaDataList.size());

        ColumnMetaData id = columnMetaDataList.getByName("film_id");
        assertEquals("NO", id.getIsAutoincrement());
        assertEquals("NO", id.getIsGeneratedColumn());

        ColumnMetaData title = columnMetaDataList.getByName("title");
        assertEquals(Types.VARCHAR, title.getDataType());
        assertEquals(255, title.getColumnSize());
        assertEquals("NO", title.getIsNullable());
        assertEquals(DatabaseMetaData.columnNoNulls, title.getNullable());
        assertEquals("test", title.getCatalogName());
        assertEquals("sakila", title.getSchemaName());
        assertEquals("film", title.getTableName());

        ColumnMetaData replacementCost = columnMetaDataList.getByName("replacement_cost");
        assertEquals(2, replacementCost.getDecimalDigits());

        ColumnMetaData lastUpdate = columnMetaDataList.getByName("last_update");
        assertEquals("CURRENT_TIMESTAMP", lastUpdate.getColumnDefaultValue());
    }


    @Test
    void foreignKey() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        assertEquals(2, foreignKeys.size());
        ForeignKey fk_film_actor_actor = foreignKeys.getByName("fk_film_actor_actor");

        assertEquals(1, fk_film_actor_actor.size());
        ForeignKeyEntry foreignKeyEntry = fk_film_actor_actor.get(0);
        assertEquals("actor_id", foreignKeyEntry.getFkColumnName());
        assertEquals("film_actor", foreignKeyEntry.getFkTableName());
        assertEquals("fk_film_actor_actor", foreignKeyEntry.getFkName());
        assertEquals("actor_id", foreignKeyEntry.getPkColumnName());
        assertEquals("actor", foreignKeyEntry.getPkTableName());
        assertEquals("pk_actor", foreignKeyEntry.getPkName());
    }

    @Test
    void foreignKeyByColumnDescription() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        ColumnMetaDataList filmActorColumns = metaDataRepository.getColumnMetaDataList("film_actor");
        ForeignKey foreignKey = foreignKeys.getByFkColumnDescription(filmActorColumns.getByName("actor_id"));
        assertNotNull(foreignKey);
        assertEquals("fk_film_actor_actor", foreignKey.getName());

        ColumnMetaDataList actorColumns = metaDataRepository.getColumnMetaDataList("actor");
        ForeignKeyList foreignKeyList = foreignKeys.getByPkColumnDescription(actorColumns.getByName("actor_id"));
        assertNotNull(foreignKeyList);
        assertEquals(1, foreignKeyList.size());
        assertNotNull(foreignKeyList.getByName("fk_film_actor_actor"));
    }

    @Test
    void importedForeignKeys() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getImportedKeys("film_actor");

        assertEquals(2, foreignKeys.size());

        ColumnMetaDataList columnMetaDataList = metaDataRepository.getColumnMetaDataList("film_actor");
        ColumnMetaData actorIdColumn = columnMetaDataList.getByName("actor_id");

        ForeignKey foreignKey = foreignKeys.getByFkColumnDescription(actorIdColumn);
        assertNotNull(foreignKey);
        assertEquals("fk_film_actor_actor", foreignKey.getName());
    }

    @Test
    void exportedForeignKeys() throws SQLException {
        ForeignKeyList foreignKeys = metaDataRepository.getExportedKeys("actor");

        assertEquals(2, foreignKeys.size());

        ColumnMetaDataList columnMetaDataList = metaDataRepository.getColumnMetaDataList("actor");
        ColumnMetaData actorIdColumn = columnMetaDataList.getByName("actor_id");

        ForeignKeyList foreignKeyList = foreignKeys.getByPkColumnDescription(actorIdColumn);
        assertNotNull(foreignKeyList);
        assertEquals(2, foreignKeyList.size());
        assertNotNull(foreignKeyList.getByName("fk_film_actor_actor"));
        assertNotNull(foreignKeyList.getByName("fk_actor_genre_actor"));
    }


    @Test
    void outgoingReferences() throws SQLException {
        TableReferenceList outgoingReferences = metaDataRepository.getOutgoingReferences("film_actor");

        assertEquals(2, outgoingReferences.size());
        assertNotNull(outgoingReferences.getByName("fk_film_actor_actor"));
        TableReference fkFilmActorFilm = outgoingReferences.getByName("fk_film_actor_film");
        assertNotNull(fkFilmActorFilm);

        TableReference.Edge sourceEdge = fkFilmActorFilm.getSourceEdge();
        assertEquals("film_actor", sourceEdge.getTableName());
        assertEquals(1, sourceEdge.getColumns().size());
        assertEquals("film_id", sourceEdge.getColumns().get(0));

        TableReference.Edge targetEdge = fkFilmActorFilm.getTargetEdge();
        assertEquals("film", targetEdge.getTableName());
        assertEquals(1, targetEdge.getColumns().size());
        assertEquals("film_id", targetEdge.getColumns().get(0));
    }

    @Test
    void incomingReferences() throws SQLException {
        TableReferenceList incomingReferences = metaDataRepository.getIncomingReferences("actor");

        assertEquals(2, incomingReferences.size());
        TableReference fkFilmActorActor = incomingReferences.getByName("fk_film_actor_actor");
        assertNotNull(fkFilmActorActor);
        assertNotNull(incomingReferences.getByName("fk_actor_genre_actor"));

        TableReference.Edge sourceEdge = fkFilmActorActor.getSourceEdge();
        assertEquals("film_actor", sourceEdge.getTableName());
        assertEquals(1, sourceEdge.getColumns().size());
        assertEquals("actor_id", sourceEdge.getColumns().get(0));

        TableReference.Edge targetEdge = fkFilmActorActor.getTargetEdge();
        assertEquals("actor", targetEdge.getTableName());
        assertEquals(1, targetEdge.getColumns().size());
        assertEquals("actor_id", targetEdge.getColumns().get(0));
    }

    @Test
    void tableReferenceEqual() throws SQLException {
        TableReferenceList incomingReferences = metaDataRepository.getIncomingReferences("actor");
        TableReferenceList outgoingReferences = metaDataRepository.getOutgoingReferences("film_actor");

        assertEquals(outgoingReferences.getByName("fk_film_actor_actor"), incomingReferences.getByName("fk_film_actor_actor"));
    }

    @Test
    void tableReferenceToString() throws SQLException {
        TableReferenceList outgoingReferences = metaDataRepository.getOutgoingReferences("film_actor");

        assertEquals("fk_film_actor_actor<film_actor([actor_id]) -> actor([actor_id])>", outgoingReferences.getByName("fk_film_actor_actor").toString());
    }
}