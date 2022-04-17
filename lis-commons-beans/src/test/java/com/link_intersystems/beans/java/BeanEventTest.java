package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BeanEventTest  {

    private ChangeListener changeListener;
    private DefaultButtonModel defaultButtonModel;
    private JavaBeanEvent beanEvent;

    @BeforeEach
    public void setup() throws IntrospectionException {
        changeListener = mock(ChangeListener.class);
        defaultButtonModel = new DefaultButtonModel();
        JavaBean<DefaultButtonModel> bean = new JavaBean<>(defaultButtonModel);

        BeanInfo beanInfo = Introspector.getBeanInfo(DefaultButtonModel.class);
        EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
        for (EventSetDescriptor eventSetDescriptor : eventSetDescriptors) {
            if ("change".equals(eventSetDescriptor.getName())) {
                beanEvent = new JavaBeanEvent(bean, new JavaBeanEventType(eventSetDescriptor));
            }
        }

        assertNotNull(beanEvent);
    }

    @Test
    void addListener() {
        beanEvent.addListener(changeListener);

        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());

        verify(changeListener, times(1)).stateChanged(Mockito.any(ChangeEvent.class));
    }

    @Test
    void removeListener() {
        beanEvent.addListener(changeListener);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, times(1)).stateChanged(Mockito.any(ChangeEvent.class));

        reset(changeListener);

        beanEvent.removeListener(changeListener);
        defaultButtonModel.setEnabled(!defaultButtonModel.isEnabled());
        verify(changeListener, Mockito.never()).stateChanged(Mockito.any(ChangeEvent.class));
    }

}
