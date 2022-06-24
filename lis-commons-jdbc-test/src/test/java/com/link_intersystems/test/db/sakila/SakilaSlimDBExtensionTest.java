package com.link_intersystems.test.db.sakila;

import com.link_intersystems.jdbc.test.db.sakila.SakilaDB;
import com.link_intersystems.jdbc.test.db.sakila.SakilaSlimDB;
import com.link_intersystems.jdbc.test.db.sakila.SakilaSlimTestDBExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(SakilaSlimTestDBExtension.class)
public class SakilaSlimDBExtensionTest extends AbstractDBExtensionTest {

    @Test
    void actor() throws SQLException {
        dbAssertions.assertRowCount("actor", 200);
    }

    @Test
    void film_actor() throws SQLException {
        dbAssertions.assertRowCount("film_actor", 5462);
    }

    @Test
    void film() throws SQLException {
        dbAssertions.assertRowCount("film", 1000);
    }

    @Test
    void language() throws SQLException {
        dbAssertions.assertRowCount("language", 6);
    }

    @Test
    void film_category() throws SQLException {
        dbAssertions.assertRowCount("film_category", 1000);
    }

    @Test
    void category() throws SQLException {
        dbAssertions.assertRowCount("category", 16);
    }

    public static Stream<String> otherTables() {
        List<String> allTables = new SakilaDB().getTableNames();
        List<String> slimTables = new SakilaSlimDB().getTableNames();

        allTables.removeAll(slimTables);

        return allTables.stream();
    }

    @ParameterizedTest
    @MethodSource("otherTables")
    void otherTablesEmpty(String tableName) throws SQLException {
        dbAssertions.assertRowCount(tableName, 0);
    }
}

