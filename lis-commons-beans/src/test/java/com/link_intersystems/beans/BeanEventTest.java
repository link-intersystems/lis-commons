package com.link_intersystems.beans;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BeanEventTest {

	private ChangeListener changeListener;
	private DefaultButtonModel defaultButtonModel;
	private BeanEvent beanEvent;

	@Before
	public void setup() throws IntrospectionException {
		changeListener = Mockito.mock(ChangeListener.class);
		defaultButtonModel = new DefaultButtonModel();
		Bean<DefaultButtonModel> bean = new Bean<>(defaultButtonModel);

		BeanInfo beanInfo = Introspector.getBeanInfo(DefaultButtonModel.class);
		EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
		for (EventSetDescriptor eventSetDescriptor : eventSetDescriptors) {
			if ("change".equals(eventSetDescriptor.getName())) {
				beanEvent = new BeanEvent(bean, eventSetDescriptor);
			}
		}

		Assert.assertNotNull(beanEvent);
	}

	@Test
	public void addListener() {
		beanEvent.addListener(changeListener);

		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());

		Mockito.verify(changeListener, Mockito.times(1)).stateChanged(Mockito.any(ChangeEvent.class));
	}

	@Test
	public void removeListener() {
		beanEvent.addListener(changeListener);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.times(1)).stateChanged(Mockito.any(ChangeEvent.class));

		Mockito.reset(changeListener);

		beanEvent.removeListener(changeListener);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.never()).stateChanged(Mockito.any(ChangeEvent.class));
	}

}
