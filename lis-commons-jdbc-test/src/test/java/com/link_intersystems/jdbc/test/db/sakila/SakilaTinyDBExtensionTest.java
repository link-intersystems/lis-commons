package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.db.H2Config;
import com.link_intersystems.jdbc.test.db.H2Extension;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@ExtendWith(H2Extension.class)
@H2Config(databaseFactory = SakilaTinyDatabaseFactory.class)
@UnitTest
public class SakilaTinyDBExtensionTest extends AbstractDBExtensionTest {

    @Test
    void actor() throws SQLException {
        dbAssertions.assertRowCount("actor", 2);
    }

    @Test
    void film_actor() throws SQLException {
        dbAssertions.assertRowCount("film_actor", 44);
    }

    @Test
    void film() throws SQLException {
        dbAssertions.assertRowCount("film", 44);
    }

    @Test
    void language() throws SQLException {
        dbAssertions.assertRowCount("language", 1);
    }

    @Test
    void film_category() throws SQLException {
        dbAssertions.assertRowCount("film_category", 0);
    }

    @Test
    void category() throws SQLException {
        dbAssertions.assertRowCount("category", 0);
    }

    @Test
    void inventory() throws SQLException {
        dbAssertions.assertRowCount("inventory", 199);
    }

    @Test
    void store() throws SQLException {
        dbAssertions.assertRowCount("store", 2);
    }

    @Test
    void rental() throws SQLException {
        dbAssertions.assertRowCount("rental", 692);
    }

    @Test
    void staff() throws SQLException {
        dbAssertions.assertRowCount("staff", 2);
    }

    @Test
    void payment() throws SQLException {
        dbAssertions.assertRowCount("payment", 692);
    }

    @Test
    void customer() throws SQLException {
        dbAssertions.assertRowCount("customer", 410);
    }

    @Test
    void address() throws SQLException {
        dbAssertions.assertRowCount("address", 412);
    }

    @Test
    void city() throws SQLException {
        dbAssertions.assertRowCount("city", 412);
    }

    @Test
    void country() throws SQLException {
        dbAssertions.assertRowCount("country", 102);
    }
}

