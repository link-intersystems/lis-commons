package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.H2Database;
import com.link_intersystems.jdbc.test.db.setup.DBSetup;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class H2DatabaseCache {

    private Class<?> testClass;

    @H2DatabaseConfig()
    private static class DefaultH2DatabaseConfig {
    }

    private ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(H2DatabaseCache.class);

    public H2DatabaseCache(Class<?> testClass) {
        this.testClass = testClass;
    }

    public H2Database getDB(ExtensionContext extensionContext) throws Exception {
        ExtensionContext.Store store = extensionContext.getStore(namespace);
        H2Database h2Database = (H2Database) store.get(testClass);

        if (h2Database == null) {
            H2DatabaseConfig config = findH2DatabaseConfig(testClass);
            h2Database = createH2Database(config);
            store.put(testClass, h2Database);
        }

        return h2Database;
    }

    protected H2Database createH2Database(H2DatabaseConfig config) throws InstantiationException, IllegalAccessException, SQLException {
        Class<? extends H2DatabaseFactory> databaseFactory = config.databaseFactory();
        H2DatabaseFactory h2DatabaseFactory = databaseFactory.newInstance();
        H2Database h2Database = h2DatabaseFactory.create();

        Class<? extends DBSetup> databaseSetupClass = config.databaseSetup();
        DBSetup dbSetup = databaseSetupClass.newInstance();
        try (Connection connection = h2Database.getConnection()) {
            dbSetup.setupSchema(connection);
            dbSetup.setupDdl(connection);
            dbSetup.setupData(connection);
        }

        return h2Database;
    }

    private H2DatabaseConfig findH2DatabaseConfig(Class<?> declaringClass) {
        if (declaringClass == null) {
            return findH2DatabaseConfig(DefaultH2DatabaseConfig.class);
        }

        H2DatabaseConfig config = declaringClass.getAnnotation(H2DatabaseConfig.class);
        if (config != null) {
            return config;
        }

        Class<?> superclass = declaringClass.getSuperclass();
        if (superclass == null) {
            return findH2DatabaseConfig(DefaultH2DatabaseConfig.class);
        }

        return findH2DatabaseConfig(superclass);
    }

    public void removeDB(ExtensionContext extensionContext) throws Exception {
        clearDB(extensionContext);

        H2Database h2Database = getDB(extensionContext);
        if (h2Database != null) {
            h2Database.close();

            ExtensionContext.Store store = extensionContext.getStore(namespace);
            store.remove(testClass);
        }
    }

    public void clearDB(ExtensionContext extensionContext) throws Exception {
        H2Database h2Database = getDB(extensionContext);
        if (h2Database != null) {
            h2Database.clear();
        }
    }

}

