package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.jdbc.H2InMemoryDB;
import com.link_intersystems.test.jdbc.SqlScript;
import org.dbunit.DatabaseUnitException;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTestTBExtension implements ParameterResolver, AfterTestExecutionCallback, LifecycleMethodExecutionExceptionHandler {

    private ExtensionContext.Namespace sakilaTestDBNamespace = ExtensionContext.Namespace.create(SakilaTestTBExtension.class);

    public H2InMemoryDB getSakilaDB() throws SQLException, IOException, DatabaseUnitException {
        H2InMemoryDB h2InMemoryDB = new H2InMemoryDB();
        h2InMemoryDB.execute("CREATE SCHEMA IF NOT EXISTS sakila");
        h2InMemoryDB.setSchema("sakila");

        InputStream sakilaDdlInputStream = SakilaTestTBExtension.class.getResourceAsStream("sakila-ddl.sql");
        SqlScript ddlScript = new SqlScript(sakilaDdlInputStream);
        h2InMemoryDB.executeScript(ddlScript);


        SakilaDataSetLoader sakilaDataSetLoader = new SakilaDataSetLoader();
        h2InMemoryDB.doWithConnection(sakilaDataSetLoader::execute);

        return h2InMemoryDB;
    }


    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        ExtensionContext.Store store = context.getStore(sakilaTestDBNamespace);
        H2InMemoryDB db = (H2InMemoryDB) store.get("db");
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        ExtensionContext.Store store = extensionContext.getStore(sakilaTestDBNamespace);
        H2InMemoryDB db = (H2InMemoryDB) store.get("db");
        if (db != null) {
            Connection connection = db.getConnection();
            connection.rollback();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        Class<?> type = parameter.getType();
        return Connection.class.equals(type);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (supportsParameter(parameterContext, extensionContext)) {
            ExtensionContext.Store store = extensionContext.getStore(sakilaTestDBNamespace);
            Connection connection = (Connection) store.get("connection");
            if (connection == null) {
                try {
                    H2InMemoryDB sakilaDB = getSakilaDB();
                    store.put("db", sakilaDB);
                    return sakilaDB.getConnection();
                } catch (SQLException | IOException | DatabaseUnitException e) {
                    throw new ParameterResolutionException("Unable to open sakila in-memory connection", e);
                }
            }

            return connection;
        }
        return null;
    }
}
