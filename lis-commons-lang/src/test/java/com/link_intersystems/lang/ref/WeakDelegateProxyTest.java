package com.link_intersystems.lang.ref;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WeakDelegateProxyTest  {

	private Consumer<Appendable> onDelegateRemovedCallback;
	private StringBuilder delegate;
	private Appendable appendableProxy;
	private WeakDelegateProxy<Appendable> weakDelegateProxy;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setup() {
		delegate = new StringBuilder();
		onDelegateRemovedCallback = Mockito.mock(Consumer.class);
		weakDelegateProxy = new WeakDelegateProxy<>(Appendable.class, delegate, onDelegateRemovedCallback);
		appendableProxy = weakDelegateProxy.createProxy();
	}

	@Test
	void proxyShouldDelegateToTarget() throws IOException {
		appendableProxy.append("test");
		assertEquals("test", delegate.toString());
	}

	@Test
	void proxyShouldNotDelegateToTarget() throws IOException, InterruptedException {
		deleteDelegate();
		appendableProxy.append("test");
		verify(onDelegateRemovedCallback).accept(appendableProxy);
	}

	private void deleteDelegate() throws InterruptedException {
		int i = 1000;
		delegate = null;
		while (i > 0 && weakDelegateProxy.getDelegate().isPresent()) {
			System.gc();
			Thread.sleep(1);
			i--;
		}
	}

	@Test
	void runnableEnabled() throws IOException, InterruptedException {
		weakDelegateProxy.setRunnableProxyEnabled(true);
		appendableProxy = weakDelegateProxy.createProxy();

		deleteDelegate();

		assertTrue(appendableProxy instanceof Runnable);
		Runnable runnable = (Runnable) appendableProxy;
		runnable.run();

		verify(onDelegateRemovedCallback).accept(appendableProxy);
	}

}
