package com.link_intersystems.beans;

import com.link_intersystems.beans.java.*;
import com.link_intersystems.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
@ExtendWith(JavaBeansExtension.class)
class FilteredBeanClassTest {

    private SomeBeanFixture someBeanFixture;
    private JavaBeanClass<SomeBean> someBeanClass;
    private FilteredBeanClass<SomeBean> filteredBeanClass;

    @BeforeEach
    void setUp(TestBeansFactory beansFactory) throws IntrospectionException, BeanClassException {
        someBeanFixture = new SomeBeanFixture(beansFactory);
        someBeanClass = someBeanFixture.bean.getBeanClass();

        filteredBeanClass = new FilteredBeanClass<>(someBeanClass, p -> p.getName().equals("stringProperty"));
    }


    @Test
    void getProperties() {
        assertEquals(1, filteredBeanClass.getProperties().size());
    }

    @Test
    void getName() {
        assertEquals(someBeanClass.getName(), filteredBeanClass.getName());
    }

    @Test
    void getType() {
        assertEquals(someBeanClass.getType(), filteredBeanClass.getType());
    }

    @Test
    void getBeanProperties() {
        assertEquals(1, filteredBeanClass.getBean(someBeanFixture.someBean).getProperties().size());
    }


    @Test
    void getBeanBeanClass() {
        assertEquals(filteredBeanClass, filteredBeanClass.getBean(someBeanFixture.someBean).getBeanClass());
    }

    @Test
    void getBeanObject() {
        assertEquals(someBeanFixture.someBean, filteredBeanClass.getBean(someBeanFixture.someBean).getObject());
    }

    @Test
    void beanAddListener() {
        PropertyChangeListener pcl = Mockito.mock(PropertyChangeListener.class);

        filteredBeanClass.getBean(someBeanFixture.someBean).addListener(pcl);

        someBeanFixture.someBean.setStringProperty("TEST");

        ArgumentCaptor<PropertyChangeEvent> propertyChangeEventArgumentCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(pcl, Mockito.times(1)).propertyChange(propertyChangeEventArgumentCaptor.capture());

        PropertyChangeEvent value = propertyChangeEventArgumentCaptor.getValue();

        assertEquals(someBeanFixture.someBean, value.getSource());
        assertEquals("stringProperty", value.getPropertyName());
        assertEquals(null, value.getOldValue());
        assertEquals("TEST", value.getNewValue());
    }

    @Test
    void beanRemoveListener() {
        PropertyChangeListener pcl = Mockito.mock(PropertyChangeListener.class);

        Bean<SomeBean> bean = filteredBeanClass.getBean(someBeanFixture.someBean);

        bean.addListener(pcl);
        bean.removeListener(pcl);

        someBeanFixture.someBean.setStringProperty("TEST");

        ArgumentCaptor<PropertyChangeEvent> propertyChangeEventArgumentCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(pcl, Mockito.times(0)).propertyChange(propertyChangeEventArgumentCaptor.capture());
    }


    @Test
    void newInstance() {
        SomeBean someBean = filteredBeanClass.newInstance();
        assertNotNull(someBean);
    }
}