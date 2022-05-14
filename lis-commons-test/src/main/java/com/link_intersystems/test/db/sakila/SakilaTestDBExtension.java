package com.link_intersystems.test.db.sakila;

import com.link_intersystems.test.jdbc.H2InMemoryDB;
import com.link_intersystems.test.jdbc.SqlScript;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link ParameterResolver} that provides a {@link Connection} to a H2 in memory database initialized with the sakila
 * sample database provided by mysql.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class SakilaTestDBExtension implements ParameterResolver, AfterTestExecutionCallback, LifecycleMethodExecutionExceptionHandler {

    private SakilaDBStore sakilaDBStore = new SakilaDBStore();

    private H2InMemoryDB createSakilaDB() throws SQLException, IOException {
        H2InMemoryDB h2InMemoryDB;
        h2InMemoryDB = new H2InMemoryDB();
        h2InMemoryDB.execute("CREATE SCHEMA IF NOT EXISTS sakila");


        InputStream sakilaDDLInputStream = SakilaTestDBExtension.class.getResourceAsStream("sakila-ddl.sql");
        SqlScript ddlScript = new SqlScript(sakilaDDLInputStream);
        h2InMemoryDB.executeScript(ddlScript);
        h2InMemoryDB.setSchema("sakila");

        SakilaDataSetLoader sakilaDataSetLoader = new SakilaDataSetLoader();
        h2InMemoryDB.doWithConnection(sakilaDataSetLoader::execute);
        return h2InMemoryDB;
    }


    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        sakilaDBStore.removeSakilaDB(context);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        sakilaDBStore.clearSakilaDB(extensionContext);
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
            try {
                H2InMemoryDB sakilaDB = sakilaDBStore.getSakilaDB(extensionContext, this::createSakilaDB);
                return sakilaDB.getConnection();
            } catch (SQLException | IOException e) {
                throw new ParameterResolutionException("Unable to open sakila in-memory connection", e);
            }
        }

        return null;
    }
}
