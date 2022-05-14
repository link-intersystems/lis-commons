package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.jdbc.H2InMemoryDB;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaDBStore {


    public static interface SakilaDBFactory {
        public H2InMemoryDB getSakilaDB() throws SQLException, IOException;
    }

    private ExtensionContext.Namespace sakilaTestDBNamespace = ExtensionContext.Namespace.create(SakilaTestDBExtension.class);

    public H2InMemoryDB getSakilaDB(ExtensionContext extensionContext) throws SQLException, IOException {
        return getSakilaDB(extensionContext, null);
    }

    public H2InMemoryDB getSakilaDB(ExtensionContext extensionContext, SakilaDBFactory sakilaDBFactory) throws SQLException, IOException {
        ExtensionContext.Store store = extensionContext.getStore(sakilaTestDBNamespace);
        H2InMemoryDB sakilaDB = (H2InMemoryDB) store.get("db");

        if (sakilaDB == null && sakilaDBFactory != null) {
            sakilaDB = sakilaDBFactory.getSakilaDB();
            store.put("db", sakilaDB);
        }

        return sakilaDB;
    }

    public void removeSakilaDB(ExtensionContext extensionContext) throws SQLException, IOException {
        clearSakilaDB(extensionContext);

        H2InMemoryDB sakilaDB = getSakilaDB(extensionContext);
        if (sakilaDB != null) {
            sakilaDB.close();

            ExtensionContext.Store store = extensionContext.getStore(sakilaTestDBNamespace);
            store.remove("db");
        }
    }

    public void clearSakilaDB(ExtensionContext extensionContext) throws SQLException, IOException {
        H2InMemoryDB sakilaDB = getSakilaDB(extensionContext);
        if (sakilaDB != null) {
            sakilaDB.dropAllObjects();
        }
    }

}
