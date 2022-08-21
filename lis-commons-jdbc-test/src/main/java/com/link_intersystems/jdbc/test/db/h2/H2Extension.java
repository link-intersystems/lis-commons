package com.link_intersystems.jdbc.test.db.h2;

import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.Optional;

/**
 * A JUnit 5 extensions that provides a {@link H2Database} or a {@link Connection} to it as test method arguments.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class H2Extension implements ParameterResolver, AfterTestExecutionCallback {

    private H2DatabaseCache h2DatabaseStore;

    private H2DatabaseCache getH2DatabaseCache(ExtensionContext context) {
        if (h2DatabaseStore == null) {
            Optional<Class<?>> testClass = context.getTestClass();
            h2DatabaseStore = testClass.map(c -> new H2DatabaseCache(new AnnotationH2ConfigPropertiesSource(c))).orElseThrow(() -> new IllegalStateException("No test class available"));
        }
        return h2DatabaseStore;
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        JUnitExtensionH2DatabaseStore databaseStore = new JUnitExtensionH2DatabaseStore(extensionContext);
        getH2DatabaseCache(extensionContext).removeDB(databaseStore);
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
                H2DatabaseCache h2DatabaseCache = getH2DatabaseCache(extensionContext);
                JUnitExtensionH2DatabaseStore databaseStore = new JUnitExtensionH2DatabaseStore(extensionContext);
                H2Database h2Database = h2DatabaseCache.getDB(databaseStore);

                if (type.equals(Connection.class)) {
                    return h2Database.getConnection();
                } else {
                    return h2Database;
                }
            } catch (Exception e) {
                throw new ParameterResolutionException("Unable to open database connection", e);
            }
        }

        return null;
    }
}
