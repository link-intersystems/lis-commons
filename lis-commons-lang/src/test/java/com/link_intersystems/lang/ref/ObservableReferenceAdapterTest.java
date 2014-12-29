package com.link_intersystems.lang.ref;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObservableReferenceAdapterTest {

	private ObservableReference<String> observableReference;
	private PropertyChangeListener propertyChangeListenerMock;

	@Before
	public void setup() {
		DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>(
				"hello");
		observableReference = new ObservableReferenceAdapter<String>(
				defaultMutableReference);
		propertyChangeListenerMock = EasyMock
				.createNiceMock(PropertyChangeListener.class);
	}

	@Test
	public void propertyChangeEvent() {
		propertyChangeListenerMock.propertyChange(EasyMock
				.anyObject(PropertyChangeEvent.class));
		EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {

			public Void answer() throws Throwable {
				Object[] currentArguments = EasyMock.getCurrentArguments();
				PropertyChangeEvent event = (PropertyChangeEvent) currentArguments[0];

				String propertyName = event.getPropertyName();
				Assert.assertEquals(ObservableReference.PROPERTY_REFERENT,
						propertyName);

				Object oldValue = event.getOldValue();
				Assert.assertEquals("hello", oldValue);

				Object newObject = event.getNewValue();
				Assert.assertEquals("hello world", newObject);
				
				Object source = event.getSource();
				Assert.assertEquals(observableReference, source);

				return null;
			}
		});
		EasyMock.replay(propertyChangeListenerMock);

		observableReference
				.addPropertyChangeListener(propertyChangeListenerMock);
		
		observableReference.set("hello world");
		
		EasyMock.verify(propertyChangeListenerMock);
	}
	
	@Test
	public void setAndGet() {
		Assert.assertEquals("hello", observableReference.get());
		observableReference.set("hello world");
		Assert.assertEquals("hello world", observableReference.get());
	}
	
	@Test
	public void propertyChangeEventAfterRemove() {
		EasyMock.replay(propertyChangeListenerMock);
		observableReference
				.addPropertyChangeListener(propertyChangeListenerMock);
		observableReference.removePropertyChangeListener(propertyChangeListenerMock);
		observableReference.set("hello world");
		
		EasyMock.verify(propertyChangeListenerMock);
	}
}
