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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(JavaBeansExtension.class)
class BeanEventSupportTest {

    private ChangeListener changeListener;
    private DefaultButtonModel defaultButtonModel;
    private BeanEventSupport<ButtonModel, ChangeListener> beanEventSupport;
    private JavaBean<DefaultButtonModel> buttonModelBean;

    @BeforeEach
    public void setup(TestBeansFactory beansFactory) throws BeanClassException {
        changeListener = Mockito.mock(ChangeListener.class);
        defaultButtonModel = new DefaultButtonModel();
        buttonModelBean = beansFactory.createBean(defaultButtonModel);
        beanEventSupport = new BeanEventSupport<>(buttonModelBean);
    }

    @Test
    void defaultConstructorAndConfigLater() {
        BeanEventSupport<ButtonModel, Object> beanEventSupport = new BeanEventSupport<>();
        beanEventSupport.setBean(buttonModelBean);
        beanEventSupport.setListener(changeListener);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, times(1)).stateChanged(any(ChangeEvent.class));
    }

    @Test
    void setListener() {
        beanEventSupport.setListener(changeListener);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, times(1)).stateChanged(any(ChangeEvent.class));
    }

    @Test
    void getListener() {
        beanEventSupport.setListener(changeListener);

        assertSame(changeListener, beanEventSupport.getListener());
    }

    @Test
    void setListenerNull() {
        beanEventSupport.setListener(changeListener);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, times(1)).stateChanged(any(ChangeEvent.class));

        Mockito.reset(changeListener);

        beanEventSupport.setListener(null);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, Mockito.never()).stateChanged(any(ChangeEvent.class));
    }

    @Test
    void setBeanNull() {
        beanEventSupport.setListener(changeListener);
        beanEventSupport.setBean(null);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, Mockito.never()).stateChanged(any(ChangeEvent.class));
    }

    @Test
    void disableEvents() {
        beanEventSupport.setListener(changeListener);

        assertTrue(beanEventSupport.isEventEnabled());
        beanEventSupport.setEventEnabled(false);
        assertFalse(beanEventSupport.isEventEnabled());

        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, Mockito.never()).stateChanged(any(ChangeEvent.class));
    }

    @Test
    void reenableEvents() {
        beanEventSupport.setListener(changeListener);
        beanEventSupport.setEventEnabled(false);
        beanEventSupport.setEventEnabled(true);

        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, times(1)).stateChanged(any(ChangeEvent.class));
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
