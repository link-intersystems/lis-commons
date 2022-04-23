package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PropertyChangeSourceSupportTest {

    private PropertyChangeListener propertyChangeListener;
    private PropertyChangeSourceSupport<NamedBean> changeSourceSupport;
    private NamedBean namedBean;

    @BeforeEach
    void setUp() {
        propertyChangeListener = mock(PropertyChangeListener.class);
        changeSourceSupport = new PropertyChangeSourceSupport<>(propertyChangeListener);
        namedBean = new NamedBean();
    }

    @Test
    void setReferent() {
        namedBean.setName("Name");
        verify(propertyChangeListener, never()).propertyChange(any());

        changeSourceSupport.setReferent(namedBean);

        namedBean.setName("OtherName");

        verify(propertyChangeListener, times(1)).propertyChange(any());
    }

    @Test
    void setReferentNull() {
        changeSourceSupport.setReferent(namedBean);
        changeSourceSupport.setReferent(null);

        verify(propertyChangeListener, never()).propertyChange(any());

        namedBean.setName("Name");

        verify(propertyChangeListener, never()).propertyChange(any());
    }

    @Test
    void setPropertyChangeEventsEnabled() {
        changeSourceSupport.setReferent(namedBean);
        changeSourceSupport.setPropertyChangeEventsEnabled(false);

        namedBean.setName("Name");

        verify(propertyChangeListener, never()).propertyChange(any());

        changeSourceSupport.setPropertyChangeEventsEnabled(true);
        namedBean.setName("OtherName");

        verify(propertyChangeListener, times(1)).propertyChange(any());
    }

    @Test
    void get() {
        assertNull(changeSourceSupport.get());

        changeSourceSupport.setReferent(namedBean);

        assertEquals(namedBean, changeSourceSupport.get());
    }

    private static class NamedBean {

        private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        private String name;

        public void setName(String name) {
            propertyChangeSupport.firePropertyChange("name", this.name, this.name = name);
        }

        public String getName() {
            return name;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
        }

        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
        }
    }
}