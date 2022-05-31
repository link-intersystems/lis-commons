package com.link_intersystems.test.db;

import com.link_intersystems.test.jdbc.H2Database;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public abstract class AbstractTestDBExtension implements ParameterResolver, AfterTestExecutionCallback {

    private H2DatabaseStore h2DatabaseStore;
    private DBSetup dbSetup;

    public AbstractTestDBExtension(DBSetup dbSetup) {
        this.h2DatabaseStore = new H2DatabaseStore(new H2DatabaseFactory(dbSetup));
        this.dbSetup = dbSetup;
    }


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        h2DatabaseStore.removeDB(extensionContext);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        Class<?> type = parameter.getType();
        return Connection.class.equals(type) || H2Database.class.equals(type) || DBSetup.class.equals(type);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (supportsParameter(parameterContext, extensionContext)) {
            Parameter parameter = parameterContext.getParameter();
            Class<?> type = parameter.getType();
            if (DBSetup.class.equals(type)) {
                return dbSetup;
            }

            try {
                H2Database h2Database = h2DatabaseStore.getDB(extensionContext);

                if (type.equals(Connection.class)) {
                    return h2Database.getConnection();
                } else {
                    return h2Database;
                }
            } catch (SQLException | IOException e) {
                throw new ParameterResolutionException("Unable to open database connection", e);
            }
        }

        return null;
    }
}
