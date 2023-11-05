package com.link_intersystems.beans;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class BeanInstanceFactoryTest {

    @Test
    void newBeanInstance() {
        BeanInstanceFactory beanInstanceFactory = spy(BeanInstanceFactory.class);

        beanInstanceFactory.newBeanInstance();

        verify(beanInstanceFactory, times(1)).newBeanInstance(ArgumentResolver.NULL_INSTANCE);
    }
}