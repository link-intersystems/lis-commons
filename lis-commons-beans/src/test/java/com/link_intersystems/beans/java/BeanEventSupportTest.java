package com.link_intersystems.beans.java;

import com.link_intersystems.beans.BeanClassException;
import com.link_intersystems.beans.BeanEventSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(JavaBeansExtension.class)
class BeanEventSupportTest {

    private ChangeListener changeListener;
    private DefaultButtonModel defaultButtonModel;
    private BeanEventSupport<ButtonModel, ChangeListener> beanEventSupport;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws BeanClassException {
        changeListener = Mockito.mock(ChangeListener.class);
        defaultButtonModel = new DefaultButtonModel();
        beanEventSupport = new BeanEventSupport<>();
        beanEventSupport.setBean(beansFactory.createBean(defaultButtonModel));
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
        beanEventSupport.setEventEnabled(false);

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
