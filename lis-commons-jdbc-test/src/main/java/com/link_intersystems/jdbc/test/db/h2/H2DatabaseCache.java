package com.link_intersystems.jdbc.test.db.h2;

import com.link_intersystems.jdbc.test.db.setup.DBSetup;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class H2DatabaseCache {

    private Class<?> testClass;

    @H2Config()
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
            H2ConfigProperties configProperties = getConfigProperties();
            h2Database = createH2Database(configProperties);
            store.put(testClass, h2Database);
        }

        return h2Database;
    }

    protected H2ConfigProperties getConfigProperties() throws Exception {
        H2Config h2DatabaseConfig = findH2DatabaseConfig(testClass);
        if (h2DatabaseConfig == null) {
            return new DefaultH2ConfigProperties();
        }

        return new AnnotationH2ConfigProperties(h2DatabaseConfig);
    }

    protected H2Database createH2Database(H2ConfigProperties configProperties) throws SQLException {
        H2Factory h2DatabaseFactory = configProperties.getH2Factory();
        String databaseName = configProperties.getDatabaseName();
        H2Database h2Database = h2DatabaseFactory.create(databaseName);

        DBSetup dbSetup = configProperties.getDBSetup();
        try (Connection connection = h2Database.getConnection()) {
            dbSetup.setupSchema(connection);
            dbSetup.setupDdl(connection);
            dbSetup.setupData(connection);
        }

        return h2Database;
    }

    private H2Config findH2DatabaseConfig(Class<?> declaringClass) {
        if (declaringClass == null) {
            return findH2DatabaseConfig(DefaultH2DatabaseConfig.class);
        }

        H2Config config = declaringClass.getAnnotation(H2Config.class);
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

