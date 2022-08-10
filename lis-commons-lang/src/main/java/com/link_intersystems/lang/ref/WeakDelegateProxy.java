package com.link_intersystems.lang.ref;

import com.link_intersystems.lang.Primitives;
import com.link_intersystems.lang.reflect.AbstractInvocationHandler;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Creates a proxy that delegates all invocations to a delegate as long as it is
 * not garbage collected. If the target has been garbage collected the proxy
 * will return <code>null</code> for all non-primitive types and the
 * {@link Primitives#getDefaultValue(Class)} in case of a primitive return type.
 *
 * <p/>
 *
 * A {@link WeakDelegateProxy} is most likely used for interfaces that do not
 * return values like listeners. You can then implement an automatic listener
 * deregistration.
 *
 * <pre>
 * public class SomeObservable implements {@link ChangeListener} {

	private List<ChangeListener> listeners = new ArrayList<>();

	private String aPropery;

	private void notifyChanged() {
	    // make a copy, because the original list might be changed while iterating, because a listener might get removed.
		List<ChangeListener> toNotify = new ArrayList<>(listeners);
		ChangeEvent event = new {@link ChangeEvent}(this);

		// listeners that do not have a target anymore will be removed automatically
		toNotify.forEach(o -> o.stateChanged(event));
	}

	&#64;Override
	public void addChangeListener({@link ChangeListener} listener) {
		WeakDelegateProxy<ChangeListener> listenerProxy = new WeakDelegateProxy<>(ChangeListener.class, listener, listeners::remove);
		listeners.add(listenerProxy);
	}
}
 * </pre>
 *
 * You can also use the {@link Runnable} interface to clean up, if the proxies
 * are created with that interface. See
 * {@link #setRunnableProxyEnabled(boolean)}.
 *
 * <pre>
 * private List<ChangeListener> listeners = new ArrayList<>();
 *
	private void notifyChanged() {
	    for(ChangeListener listenerProxy = listeners){
	       if(listenerProxy instanceof Runnable){
	           Runnable runnable = (Runnable) runnable;
	           runnable.run();
	       }
	    }
		ChangeEvent event = new {@link ChangeEvent}(this);
		listeners.forEach(o -> o.stateChanged(event));
	}
 * </pre>
 *
 * <b>note:</b> The {@link Runnable} interface was choosen, because it is always
 * visible by the {@link ClassLoader} the proxy is created with.
 *
 * @author René Link
 *         <a href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @param <T> the reference object's referent type.
 *
 * @since 1.2.0;
 */
public class WeakDelegateProxy<T> extends AbstractInvocationHandler<T> {

	private boolean runnableProxyEnabled = false;

	private WeakReference<T> delegateReference;
	private Consumer<T> onDelegateRemoved;

	public WeakDelegateProxy(Class<T> type, T delegate, Consumer<T> onDelegateRemoved) {
		super(type);

		if (onDelegateRemoved == null) {
			onDelegateRemoved = o -> {
			};
		}

		this.onDelegateRemoved = onDelegateRemoved;
		delegateReference = new WeakReference<T>(requireNonNull(delegate));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Runnable.class.equals(method.getDeclaringClass())) {
			checkReferent(proxy);
			return null;
		}

		T t = checkReferent(proxy);

		if (t == null) {
			Class<?> returnType = method.getReturnType();
			if (Primitives.isPrimitive(returnType)) {
				return Primitives.getDefaultValue(returnType);
			}
			return null;
		} else {
			return method.invoke(t, args);
		}
	}

	@SuppressWarnings("unchecked")
	private T checkReferent(Object proxy) {
		T t = delegateReference.get();

		if (t == null) {
			onDelegateRemoved.accept((T) proxy);
		}

		return t;
	}

	public Optional<T> getDelegate() {
		return Optional.ofNullable(delegateReference.get());
	}

	/**
	 * Enable or disable the {@link WeakDelegateProxy} interface when creating proxies.
	 * Defaults to <code>false</code>.
	 *
	 * @param delegateProxyEnabled
	 */
	public void setRunnableProxyEnabled(boolean delegateProxyEnabled) {
		this.runnableProxyEnabled = delegateProxyEnabled;
	}

	public boolean isRunnableProxyEnabled() {
		return runnableProxyEnabled;
	}

	@Override
	protected List<Class<?>> getProxyInterfaces() {
		List<Class<?>> proxyInterfaces = super.getProxyInterfaces();
		if (isRunnableProxyEnabled()) {
			proxyInterfaces.add(Runnable.class);
		}
		return proxyInterfaces;
	}
}