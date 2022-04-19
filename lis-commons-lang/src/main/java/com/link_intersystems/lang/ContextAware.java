/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang;

import com.link_intersystems.lang.reflect.Class2;
import com.link_intersystems.lang.reflect.Invokable;
import com.link_intersystems.lang.reflect.Method2;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Base class for handling of {@link Runnable}, {@link Callable} and method
 * invocation to be executed within a specific context. The concrete context is
 * defined by subclasses. The {@link ContextAware} only provides the template
 * method that ensures that the concrete context is activated by calling
 * {@link #activateContext()} before a {@link Runnable}, {@link Callable} or
 * method invocation is done and that the context gets de-activated after the
 * call (even in case of an exception). Also support for creating java proxies
 * that ensure that each call to the proxied interface(s) is context aware.
 *
 * <h2>Runnable context aware example</h2>
 *
 * <pre>
 * Client                     ContextAware                   toRun:Runnable
 *
 *  +-----------------------> runInContext(toRun)
 *  |                          |
 *  |                          +------+
 *  |                          |      |
 *  |                          |<-----+ activateContext()
 *  |                          |
 *  |                          +---------------------------> run()
 *  |                          |                              |
 *  |                          |<-----------------------------+
 *  |                          |
 *  |                          +------+
 *  |                          |      |
 *  |                          |<-----+ deactivateContext()
 *  |                          |
 *  |<-------------------------+
 * </pre>
 *
 * <h2>Proxy context aware example</h2>
 *
 * <pre>
 * Client                     ContextAware                                  proxy:ContextAwareProxy               someInstance:SomeInterface
 *  +-----------------------> createContextProxy(someInstance)
 *  |                          |
 *  |                          +-------------------------------------------> new
 *  |                          |
 *  |<--- proxy ---------------+
 *  |
 *  +---------------------------------------------------------------------> someMethod()
 *  |                                                                        |
 *  |                         runInContext(method...) <----------------------+
 *  |                          |
 *  |                          +------+
 *  |                          |      |
 *  |                          |<-----+ activateContext()
 *  |                          |
 *  |                          +--------------------------------------------------------------------------> someMethod()
 *  |                          |                                                                             |
 *  |                          |<----------------------------------------------------------------------------+
 *  |                          |
 *  |                          +------+
 *  |                          |      |
 *  |                          |<-----+ deactivateContext()
 *  |                          |
 *  |                          +-------------------------------------------->+
 *  |                                                                        |
 *  +<-----------------------------------------------------------------------+
 * </pre>
 * <p>
 * If a context is managed by a third library class that uses a callback method
 * itself, e.g. TransactionManager.callInTransaction(TransactionCallback) the
 * {@link ContextProvider} type can be used as CONTEXT_TYPE. The
 * {@link ContextProvider} let you implement the adaption of a third party
 * library class easily. If {@link #activateContext()} returns an object of
 * instance {@link ContextProvider} the context creation is delegated to
 * {@link ContextProvider#provideContext(RunInContext)} and the
 * {@link ContextProvider#provideContext(RunInContext)} must call
 * {@link RunInContext#proceed()} to proceed with the {@link Runnable} or
 * {@link Callable} that should be run in the context, e.g.
 * {@link #runInContext(Callable)} or {@link #runInContext(Runnable)}.
 *
 * <pre>
 * public class TransactionContextAware extends ContextAware&lt;ContextProvider&gt;(){
 *
 *      private TransactionManagerCallbackAdapter tmca;
 *
 *      public TransactionContextAware(TransactionManager tm){
 *           this.tmca = new TransactionManagerCallbackAdapter(tm);
 *      }
 *
 *      protected ContextProvider activateContext(){
 *           return tmca;
 *      }
 *
 *      protected void deactivateContext(ContextProvider contextProvider){
 *      }
 *
 *      private static class TransactionManagerCallbackAdapter implements ContextProvider {
 *
 *           public void provideContext(RunInContext runInContext) throws Exception {
 *                TransactionCallback tc = new TransactionCallbackAdapter(runInContext);
 *                tm.callInTransaction(tc);
 *           }
 *
 *      }
 *
 *      private static class TransactionCallbackAdapter implements TransactionCallback {
 *
 *           private RunInContext ric;
 *
 *           public TransactionCallbackAdapter(RunInContext ric){
 *                this.ric = ric;
 *           }
 *
 *           public void doInTransaction(){
 *           	  ric.proceed();
 *           }
 *
 *      }
 *
 * }
 *
 * TransactionManager tm = ...;
 * TransactionContextAware transactionContextAware = new TransactionContextAware(tm);
 *
 * Callable&lt;Object&gt; callable = ...;
 *
 * transactionContextAware.runInContext(callable); // ensures that the callable is run within a transaction
 *
 * </pre>
 *
 * @param <CONTEXT_TYPE>
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
public abstract class ContextAware<CONTEXT_TYPE> {

    private final ThreadLocal<Method2> invocationMethod = new ThreadLocal<Method2>();

    private Collection<ContextListener<CONTEXT_TYPE>> contextListeners = new ArrayList<ContextListener<CONTEXT_TYPE>>();
    private ContextProvider thisContextProviderAdapter;

    /**
     * The method of the context proxy that is invoked in case that
     * {@link #getInvocationMethod()} is called within a proxy call to a proxy
     * that was created with {@link #createContextProxy(Object)} otherwise
     * <code>null</code>. If {@link #getInvocationMethod()} is invoked by
     * {@link Runnable} or {@link Callable} implementations that were passed to
     * {@link #runInContext(Runnable)} or {@link #runInContext(Callable)} the
     * {@link #getInvocationMethod()} always returns <code>null</code>.
     *
     * @return the method of the context proxy that is invoked.
     * @since 1.2.0;
     */
    public Method2 getInvocationMethod() {
        return invocationMethod.get();
    }

    public ContextAware() {
        thisContextProviderAdapter = new ContextAwareContextProvider<CONTEXT_TYPE>(
                this);
    }

    /**
     * Creates a java proxy that executes every call to the target object within
     * the context that this {@link ContextAware} defines (defined by
     * subclasses).
     *
     * @param targetObject the target object to create a proxy for.
     * @return a java proxy that executes every call to the target object's
     * interfaces in this {@link ClassLoaderContextAware}.
     */
    @SuppressWarnings("unchecked")
    public <T> T createContextProxy(T targetObject) {
        Assert.notNull("targetObject", targetObject);

        Class<T> targetObjectClass = (Class<T>) targetObject.getClass();
        List<Class<?>> allInterfaces = getAllInterfaces(targetObjectClass);
        if (allInterfaces.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unable to create java proxy, because target object's "
                            + targetObjectClass
                            + " does not implement any interface.");
        }
        Class<?>[] allInterfacesAsArray = (Class<?>[]) allInterfaces
                .toArray(new Class<?>[allInterfaces.size()]);

        ClassLoader classLoader = targetObject.getClass().getClassLoader();
        ContextAwareInvocationHandler classLoaderContextInvocationHandler = new ContextAwareInvocationHandler(
                this, targetObject);

        T classLoaderContextAwareProxy = (T) Proxy.newProxyInstance(
                classLoader, allInterfacesAsArray,
                classLoaderContextInvocationHandler);

        return classLoaderContextAwareProxy;
    }

    private List<Class<?>> getAllInterfaces(Class<?> targetObjectClass) {
        class AllInterfacesIterator implements Iterator<Class<?>> {

            private Queue<Class<?>> types = new LinkedList<>();

            public AllInterfacesIterator(Class<?> type) {
                if (type.isInterface()) {
                    types.add(type);
                } else {
                    Class<?>[] interfaces = type.getInterfaces();
                    types.addAll(Arrays.asList(interfaces));
                }
            }

            @Override
            public boolean hasNext() {
                return !types.isEmpty();
            }

            @Override
            public Class<?> next() {
                Class<?> nextType = types.poll();

                Class<?>[] interfaces = nextType.getInterfaces();
                types.addAll(Arrays.asList(interfaces));

                return nextType;
            }
        }

        AllInterfacesIterator allInterfacesIterator = new AllInterfacesIterator(targetObjectClass);
        List<Class<?>> allInterfaces = new ArrayList<>();
        while (allInterfacesIterator.hasNext()) {
            Class<?> next =  allInterfacesIterator.next();
            allInterfaces.add(next);

        }
        return allInterfaces;
    }

    /**
     * Runs the {@link Runnable} in this {@link ContextAware}'s context (defined
     * by subclasses).
     *
     * @param runnable the runnable that should be run with this {@link ContextAware}
     *                 's context (defined by subclasses).
     * @since 1.2.0;
     */
    public void runInContext(Runnable runnable) {
        RunnableRunInContext runInContext = new RunnableRunInContext(runnable);
        try {
            thisContextProviderAdapter.provideContext(runInContext);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Calls the {@link Callable#call()} method within this {@link ContextAware}
     * 's context (defined by subclasses).
     *
     * @param callable the {@link Callable} that should be called within this
     *                 {@link ContextAware}'s context (defined by subclasses).
     * @return the result of the {@link Callable#call()} method.
     * @throws Exception the exception that is thrown by the {@link Callable#call()}
     *                   method.
     * @since 1.2.0;
     */
    public <RESULT> RESULT runInContext(Callable<RESULT> callable)
            throws Exception {
        CallableRunInContext<RESULT> callableRunInContext = new CallableRunInContext<RESULT>(
                callable);
        thisContextProviderAdapter.provideContext(callableRunInContext);
        RESULT result = callableRunInContext.getResult();
        return result;
    }

    /**
     * Adds the {@link ContextListener} to this {@link ContextAware}.
     *
     * @since 1.2.0;
     */
    public void addContextListener(ContextListener<CONTEXT_TYPE> contextListener) {
        Assert.notNull("contextListener", contextListener);
        contextListeners.add(contextListener);
    }

    /**
     * Removes the {@link ContextListener} from this {@link ContextAware}.
     *
     * @since 1.2.0;
     */
    public void removeContextListener(
            ContextListener<CONTEXT_TYPE> contextListener) {
        Assert.notNull("contextListener", contextListener);
        contextListeners.remove(contextListener);
    }

    /**
     * Subclasses must override this method to implement the activation of the
     * context that they handle.
     *
     * @return a context object that will be passed to the
     * {@link #deactivateContext(Object)} method on de-activation.
     * @since 1.2.0;
     */
    protected abstract CONTEXT_TYPE activateContext();

    /**
     * Subclasses must override this method to implement the de-activation of
     * the context they handle.
     *
     * @param context the object that was returned by the {@link #activateContext()}
     *                call.
     * @since 1.2.0;
     */
    protected abstract void deactivateContext(CONTEXT_TYPE context);

    /**
     * Subclasses might override this method to implement the de-activation of
     * the context they handle in case of an exception. This default
     * implementation just delegates to the {@link #deactivateContext(Object)}
     * method. When an {@link Exception} occurs the
     * {@link #deactivateContext(Object, Exception)} method is invoked first and
     * after that {@link ContextListener}s get notified. At the end the
     * exception is thrown further.
     *
     * @param context   the object that was returned by the {@link #activateContext()}
     *                  call.
     * @param exception the exception that occurred while executing within the
     *                  context.
     * @since 1.2.0;
     */
    protected void deactivateContext(CONTEXT_TYPE context, Exception exception) {
        deactivateContext(context);
    }

    /**
     * Same semantic as {@link #runInContext(Callable)}, but uses
     * {@link Class2#getApplicableMethod(String, Object...)} to determine the
     * method that should be invoked.
     *
     * @param target
     * @param method
     * @param params
     * @return the invoked method's return value
     * @throws Exception if the invoked method throws an exception.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public <EXPECTED_TYPE> EXPECTED_TYPE invokeInContext(Object target,
                                                         String method, Object... params) throws Exception {
        Assert.notNull("target", target);
        Class<?> targetClass = target.getClass();
        Class2<?> targetClass2 = Class2.get(targetClass);
        Method2 applicableMethod = targetClass2.getApplicableMethod(method,
                params);
        Invokable invokable = applicableMethod.getInvokable(target);
        Callable<EXPECTED_TYPE> callable = invokable.asCallable(params);

        Object methodInvokationResult = runInContext(callable);
        EXPECTED_TYPE expectedType = (EXPECTED_TYPE) methodInvokationResult;
        return expectedType;
    }

    protected void fireContextActivated(CONTEXT_TYPE contextObject) {
        for (ContextListener<CONTEXT_TYPE> contextListener : contextListeners) {
            contextListener.contextActivated(contextObject);
        }
    }

    protected void fireContextDeactivated(CONTEXT_TYPE contextObject) {
        for (ContextListener<CONTEXT_TYPE> contextListener : contextListeners) {
            contextListener.contextDeactivated(contextObject);
        }
    }

    protected void fireContextDeactivated(CONTEXT_TYPE contextObject,
                                          Exception exception) {
        for (ContextListener<CONTEXT_TYPE> contextListener : contextListeners) {
            contextListener.contextDeactivated(contextObject, exception);
        }
    }

    /**
     * Same semantic as {@link #invokeInContext(Object, String, Object...)}, but
     * searches for a static applicable method to invoke.
     *
     * @param target the {@link Class} that holds the static method.
     * @param method the method name of the static method that should be invoked.
     * @param params the invokation parameters.
     * @return the invoked method's return value
     * @throws Exception if the invoked method throws an exception.
     * @since 1.2.0;
     */
    @SuppressWarnings("unchecked")
    public <EXPECTED_TYPE> EXPECTED_TYPE invokeStaticInContext(Class<?> target,
                                                               String method, Object... params) throws Exception {
        Class2<?> targetClass2 = Class2.get(target);
        Method2 applicableMethod = targetClass2.getApplicableMethod(method,
                params);
        if (applicableMethod == null
                || !Modifier.isStatic(applicableMethod.getModifiers())) {
            throw new IllegalArgumentException(
                    "target class doesn't declare a static applicable method called "
                            + method);
        }
        Invokable invokable = applicableMethod.getInvokable(target);
        Callable<EXPECTED_TYPE> callable = invokable.asCallable(params);

        Object methodInvokationResult = runInContext(callable);
        EXPECTED_TYPE expectedType = (EXPECTED_TYPE) methodInvokationResult;
        return expectedType;
    }

    /**
     * A {@link ContextProvider} can be used as a {@link ContextAware}'s
     * CONTEXT_TYPE to let the {@link ContextProvider} implementation control
     * the context creation. See the javadoc of {@link ContextAware} for
     * details.
     *
     * @author René Link <a
     * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     * intersystems.com]</a>
     * @since 1.2.0;
     */
    public static interface ContextProvider {

        /**
         * The {@link ContextProvider} implementation must do whatever is needed
         * to create a context, than proceed with object that should be run
         * within the context and finally remove the context if necessary.
         *
         * @param runInContext the object to proceed the invocation with when the context
         *                     is established by calling {@link RunInContext#proceed()}.
         * @throws Exception
         * @since 1.2.0;
         */
        public void provideContext(RunInContext runInContext) throws Exception;
    }

    /**
     * Represents the object that should be run within a context.
     *
     * @author René Link <a
     * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
     * intersystems.com]</a>
     * @since 1.2.0;
     */
    public static interface RunInContext {

        /**
         * Proceeds the execution of an object that should be run within the
         * context.
         *
         * @throws Exception
         * @since 1.2.0;
         */
        public void proceed() throws Exception;
    }

    private static class ContextAwareInvocationHandler implements
            InvocationHandler {

        private final ContextAware<?> contextAware;
        private final Object target;

        public ContextAwareInvocationHandler(ContextAware<?> contextAware,
                                             Object target) {
            this.contextAware = contextAware;
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if (args == null) {
                args = new Object[0];
            }
            Method2 method2 = Method2.forMethod(method);
            Invokable invokable = method2.getInvokable(target);
            Callable<Object> callable = invokable.asCallable(args);
            try {
                contextAware.invocationMethod.set(method2);
                Object result = contextAware.runInContext(callable);
                return result;
            } finally {
                contextAware.invocationMethod.remove();
            }
        }

    }

    private static class RunnableRunInContext implements RunInContext {

        private Runnable runnable;

        public RunnableRunInContext(Runnable runnable) {
            this.runnable = runnable;
        }

        public void proceed() {
            runnable.run();
        }

    }

    private static class CallableRunInContext<T> implements RunInContext {

        private Callable<T> callable;
        private T result;

        public CallableRunInContext(Callable<T> callable) {
            this.callable = callable;
        }

        public T getResult() {
            return result;
        }

        public void proceed() throws Exception {
            result = callable.call();
        }

    }

    private static class ContextAwareContextProvider<CONTEXT_TYPE> implements
            ContextProvider {

        private ContextAware<CONTEXT_TYPE> contextAware;

        public ContextAwareContextProvider(
                ContextAware<CONTEXT_TYPE> contextAware) {
            this.contextAware = contextAware;
        }

        public void provideContext(RunInContext runInContext) throws Exception {
            CONTEXT_TYPE context = contextAware.activateContext();
            contextAware.fireContextActivated(context);
            Exception exception = null;
            try {
                if (context instanceof ContextProvider) {
                    ContextProvider contextProvider = (ContextProvider) context;
                    contextProvider.provideContext(runInContext);
                } else {
                    runInContext.proceed();
                }
            } catch (Exception e) {
                exception = e;
            } finally {
                if (exception == null) {
                    contextAware.deactivateContext(context);
                    contextAware.fireContextDeactivated(context);
                } else {
                    contextAware.deactivateContext(context, exception);
                    contextAware.fireContextDeactivated(context, exception);
                    throw exception;
                }
            }
        }

    }

}
