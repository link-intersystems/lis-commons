package com.link_intersystems.jdbc.test.db;

import com.link_intersystems.jdbc.test.H2Database;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link GenericTestDBExtention} can be used to configure custom test databases on the fly.
 *
 * <pre>
 * {@literal @}RegisterExtension
 * static GenericTestDBExtension dbExtensions = new GenericTestDBExtention(new MyH2DatabaseFactory());
 * </pre>
 *
 * or create factory methods
 *
 * <pre>
 * {@literal @}RegisterExtension
 * static GenericTestDBExtension dbExtensions = MyDatabaseExtensions.createMySpecialDatabase();
 * </pre>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class GenericTestDBExtention implements ParameterResolver, AfterTestExecutionCallback {

    private H2DatabaseFactory h2DatabaseFactory;
    private H2DatabaseCache h2DatabaseStore;

    public GenericTestDBExtention(H2DatabaseFactory h2DatabaseFactory) {
        this.h2DatabaseFactory = h2DatabaseFactory;
    }

    protected H2DatabaseFactory getH2DatabaseFactory() {
        return h2DatabaseFactory;
    }

    private H2DatabaseCache getH2DatabaseStore() {
        if (h2DatabaseStore == null) {
            H2DatabaseFactory h2DatabaseFactory = getH2DatabaseFactory();
            h2DatabaseStore = new H2DatabaseCache(h2DatabaseFactory);
        }
        return h2DatabaseStore;
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        getH2DatabaseStore().removeDB(extensionContext);
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
            Parameter parameter = parameterContext.getParameter();
            Class<?> type = parameter.getType();

            try {
                H2Database h2Database = getH2DatabaseStore().getDB(extensionContext);

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
