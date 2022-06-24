package com.link_intersystems.net.http.test.junit;

import com.link_intersystems.net.http.test.HttpMockServer;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Parameter;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class HttpMockServerExtension implements ParameterResolver, AfterTestExecutionCallback {

    private ExtensionContext.Namespace httpMockServerExtensionNamespace = ExtensionContext.Namespace.create(HttpMockServerExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return isHttpMockServerParameter(parameterContext);
    }

    private boolean isHttpMockServerParameter(ParameterContext parameterContext) {
        Parameter parameter = parameterContext.getParameter();
        return HttpMockServer.class.equals(parameter.getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (isHttpMockServerParameter(parameterContext)) {
            ExtensionContext.Store store = getStore(extensionContext);

            HttpMockServer httpMockServer = (HttpMockServer) store.get(HttpMockServer.class);
            if (httpMockServer == null) {
                httpMockServer = new HttpMockServer();
                try {
                    httpMockServer.start();
                    store.put(HttpMockServer.class, httpMockServer);
                } catch (IOException e) {
                    ParameterResolutionException exception = new ParameterResolutionException("Unable to start HttpMockServer");
                    exception.initCause(e);
                    throw exception;
                }
            }

            return httpMockServer;
        }

        return null;
    }

    private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        return extensionContext.getStore(httpMockServerExtensionNamespace);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        HttpMockServer httpMockServer = (HttpMockServer) store.get(HttpMockServer.class);

        if (httpMockServer != null) {
            httpMockServer.stop();
            store.remove(HttpMockServer.class);
        }
    }
}
