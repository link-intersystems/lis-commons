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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@UnitTest
@ExtendWith(JavaBeansExtension.class)
class FilteredBeanClassTest {

    private SomeBeanFixture someBeanFixture;
    private BeanClass<SomeBean> someBeanClass;
    private FilteredBeanClass<SomeBean> filteredBeanClass;

    @BeforeEach
    void setUp(TestBeansFactory beansFactory) throws IntrospectionException, BeanClassException {
        someBeanFixture = new SomeBeanFixture(beansFactory);
        someBeanClass = someBeanFixture.bean.getBeanClass();

        filteredBeanClass = new FilteredBeanClass<>(someBeanClass, p -> p.getName().equals("stringProperty"));
    }


    @Test
    void getSingleProperties() {
        assertEquals(1, filteredBeanClass.getProperties().filter(PropertyDesc.PREDICATE).size());
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
        assertEquals(1, filteredBeanClass.getBeanFromInstance(someBeanFixture.someBean).getSingleProperties().size());
    }


    @Test
    void getBeanBeanClass() {
        assertEquals(filteredBeanClass, filteredBeanClass.getBeanFromInstance(someBeanFixture.someBean).getBeanClass());
    }

    @Test
    void getBeanObject() {
        assertEquals(someBeanFixture.someBean, filteredBeanClass.getBeanFromInstance(someBeanFixture.someBean).getBeanObject());
    }

    @Test
    void beanAddListener() {
        PropertyChangeListener pcl = Mockito.mock(PropertyChangeListener.class);

        filteredBeanClass.getBeanFromInstance(someBeanFixture.someBean).addListener(pcl);

        someBeanFixture.someBean.setStringProperty("TEST");

        ArgumentCaptor<PropertyChangeEvent> propertyChangeEventArgumentCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(pcl, Mockito.times(1)).propertyChange(propertyChangeEventArgumentCaptor.capture());

        PropertyChangeEvent value = propertyChangeEventArgumentCaptor.getValue();

        assertEquals(someBeanFixture.someBean, value.getSource());
        assertEquals("stringProperty", value.getPropertyName());
        assertNull(value.getOldValue());
        assertEquals("TEST", value.getNewValue());
    }

    @Test
    void beanRemoveListener() {
        PropertyChangeListener pcl = Mockito.mock(PropertyChangeListener.class);

        Bean<SomeBean> bean = filteredBeanClass.getBeanFromInstance(someBeanFixture.someBean);

        bean.addListener(pcl);
        bean.removeListener(pcl);

        someBeanFixture.someBean.setStringProperty("TEST");

        ArgumentCaptor<PropertyChangeEvent> propertyChangeEventArgumentCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(pcl, Mockito.times(0)).propertyChange(propertyChangeEventArgumentCaptor.capture());
    }

}