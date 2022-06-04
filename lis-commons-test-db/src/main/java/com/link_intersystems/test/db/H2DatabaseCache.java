package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.H2Database;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class H2DatabaseCache {

    private ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(H2DatabaseCache.class);
    private H2DatabaseFactory h2DatabaseFactory;

    public H2DatabaseCache(H2DatabaseFactory h2DatabaseFactory) {
        this.h2DatabaseFactory = Objects.requireNonNull(h2DatabaseFactory);
    }

    public H2Database getDB(ExtensionContext extensionContext) throws SQLException, IOException {
        ExtensionContext.Store store = extensionContext.getStore(namespace);
        H2Database h2Database = (H2Database) store.get(h2DatabaseFactory);

        if (h2Database == null && h2DatabaseFactory != null) {
            h2Database = h2DatabaseFactory.create();
            store.put(h2DatabaseFactory, h2Database);
        }

        return h2Database;
    }

    public void removeDB(ExtensionContext extensionContext) throws SQLException, IOException {
        clearDB(extensionContext);

        H2Database h2Database = getDB(extensionContext);
        if (h2Database != null) {
            h2Database.close();

            ExtensionContext.Store store = extensionContext.getStore(namespace);
            store.remove(h2DatabaseFactory);
        }
    }

    public void clearDB(ExtensionContext extensionContext) throws SQLException, IOException {
        H2Database h2Database = getDB(extensionContext);
        if (h2Database != null) {
            h2Database.clear();
        }
    }

}
