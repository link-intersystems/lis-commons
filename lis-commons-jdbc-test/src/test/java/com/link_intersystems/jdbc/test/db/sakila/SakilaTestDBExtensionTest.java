package com.link_intersystems.jdbc.test.db.sakila;

import com.link_intersystems.jdbc.test.RowAssertions;
import com.link_intersystems.jdbc.test.db.h2.H2Extension;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
@SakilaSlimExtension
public class SakilaTestDBExtensionTest extends AbstractDBExtensionTest {

    @Test
    void languageCount() throws SQLException {
        dbAssertions.assertRowCount("language", 6);
    }

    @Test
    void actorTest() throws SQLException {
        RowAssertions rowAssertions = dbAssertions.assertRowExists("actor", 1);

        rowAssertions.assertColumnValue("actor_id", 1);
        rowAssertions.assertColumnValue("first_name", "PENELOPE");
        rowAssertions.assertColumnValue("last_name", "GUINESS");
    }

}

