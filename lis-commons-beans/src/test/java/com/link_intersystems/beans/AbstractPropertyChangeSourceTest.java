package com.link_intersystems.beans;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AbstractPropertyChangeSourceTest extends AbstractPropertyChangeSource {

	@SuppressWarnings("unchecked")
	@Test
	public void onPropertyChanged() {
		Consumer<String> consumer = Mockito.mock(Consumer.class);

		PropertyObservation propertyObservation = onPropertyChange("testProperty", consumer);

		firePropertyChange("testProperty", "oldValue", "newValue");

		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
		verify(consumer).accept(valueCaptor.capture());
		String value = valueCaptor.getValue();
		assertEquals("newValue", value);

		propertyObservation.deactivate();
		reset(consumer);
		firePropertyChange("testProperty", "oldValue", "newValue");
		verify(consumer, never()).accept(Mockito.anyString());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void onPropertyChangedEvent() {
		Consumer<PropertyChangeEvent> consumer = Mockito.mock(Consumer.class);

		PropertyObservation propertyObservation = onPropertyChangeEvent("testProperty", consumer);

		firePropertyChange("testProperty", "oldValue", "newValue");

		ArgumentCaptor<PropertyChangeEvent> valueCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
		verify(consumer).accept(valueCaptor.capture());
		PropertyChangeEvent evt = valueCaptor.getValue();
		assertEquals("testProperty", evt.getPropertyName());
		assertEquals("oldValue", evt.getOldValue());
		assertEquals("newValue", evt.getNewValue());

		propertyObservation.deactivate();
		reset(consumer);
		firePropertyChange("testProperty", "oldValue", "newValue");
		verify(consumer, never()).accept(Mockito.any(PropertyChangeEvent.class));
	}

	@Test
	public void onPropertyChangedRunnable() {
		Runnable runnable = Mockito.mock(Runnable.class);

		PropertyObservation propertyObservation = onPropertyChange("testProperty", runnable);
		firePropertyChange("testProperty", "oldValue", "newValue");
		verify(runnable).run();

		propertyObservation.deactivate();
		reset(runnable);
		firePropertyChange("testProperty", "oldValue", "newValue");
		verify(runnable, never()).run();
	}

}
