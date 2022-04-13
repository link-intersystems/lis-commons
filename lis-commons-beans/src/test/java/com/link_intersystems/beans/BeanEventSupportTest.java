package com.link_intersystems.beans;

import java.beans.IntrospectionException;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BeanEventSupportTest  {

	private ChangeListener changeListener;
	private DefaultButtonModel defaultButtonModel;
	private BeanEventSupport<ButtonModel, ChangeListener> beanEventSupport;

	@BeforeEach
	public void setup() throws IntrospectionException {
		changeListener = Mockito.mock(ChangeListener.class);
		defaultButtonModel = new DefaultButtonModel();
		beanEventSupport = new BeanEventSupport<ButtonModel, ChangeListener>();
		beanEventSupport.setBean(defaultButtonModel);
	}

	@Test
	void setListener() {
		beanEventSupport.setListener(changeListener);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.times(1)).stateChanged(Mockito.any(ChangeEvent.class));
	}

	@Test
	void setListenerNull() {
		beanEventSupport.setListener(changeListener);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.times(1)).stateChanged(Mockito.any(ChangeEvent.class));

		Mockito.reset(changeListener);

		beanEventSupport.setListener(null);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.never()).stateChanged(Mockito.any(ChangeEvent.class));
	}

	@Test
	void setBeanNull() {
		beanEventSupport.setListener(changeListener);
		beanEventSupport.setBean(null);
		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.never()).stateChanged(Mockito.any(ChangeEvent.class));
	}

	@Test
	void disableEvents() {
		beanEventSupport.setListener(changeListener);
		beanEventSupport.setEventDisabled(true);

		defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
		Mockito.verify(changeListener, Mockito.never()).stateChanged(Mockito.any(ChangeEvent.class));
	}

	@Test
	void getBean() {
		ButtonModel buttonModel = beanEventSupport.getBean();
		assertSame(defaultButtonModel, buttonModel);
	}

	@Test
	void getBeanNull() {
		beanEventSupport.setBean(null);
		ButtonModel buttonModel = beanEventSupport.getBean();
		assertNull(buttonModel);
	}

}
