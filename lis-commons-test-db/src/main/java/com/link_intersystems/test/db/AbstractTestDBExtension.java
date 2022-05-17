package com.link_intersystems.test.db;

import com.link_intersystems.test.db.H2DatabaseFactory;
import com.link_intersystems.test.db.H2DatabaseStore;
import com.link_intersystems.test.jdbc.H2Database;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractTestDBExtension implements ParameterResolver, AfterTestExecutionCallback, LifecycleMethodExecutionExceptionHandler {

    private H2DatabaseStore h2DatabaseStore;

    public AbstractTestDBExtension(H2DatabaseFactory h2DatabaseFactory) {
        this.h2DatabaseStore = new H2DatabaseStore(h2DatabaseFactory);
    }

    @Override
    public void handleAfterAllMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        h2DatabaseStore.removeDB(context);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        h2DatabaseStore.clearDB(extensionContext);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        Class<?> type = parameter.getType();
        return Connection.class.equals(type) || H2Database.class.equals(type);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (supportsParameter(parameterContext, extensionContext)) {
            try {
                H2Database sakilaDB = h2DatabaseStore.getDB(extensionContext);
                Parameter parameter = parameterContext.getParameter();
                Class<?> type = parameter.getType();
                if (type.equals(Connection.class)) {
                    return sakilaDB.getConnection();
                } else {
                    return sakilaDB;
                }
            } catch (SQLException | IOException e) {
                throw new ParameterResolutionException("Unable to open sakila in-memory connection", e);
            }
        }

        return null;
    }
}
