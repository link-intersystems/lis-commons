package com.link_intersystems.mockito.beans;

import com.link_intersystems.beans.AbstractBean;
import com.link_intersystems.beans.BeansFactory;
import org.mockito.ArgumentMatcher;

public class PropertiesMatcher<T> implements ArgumentMatcher<T> {

    private final AbstractBean<T> expectedBean;

    public PropertiesMatcher(T expectedBean) {
        this(expectedBean, BeansFactory.getDefault());
    }

    public PropertiesMatcher(T expectedBean, BeansFactory beansFactory) {
        this.expectedBean = beansFactory.createBean(expectedBean, Object.class);
    }

    @Override
    public boolean matches(T argument) {
        BeansFactory factory = BeansFactory.getDefault();
        AbstractBean<T> argumentBean = factory.createBean(argument, Object.class);
        return propertiesEqual(expectedBean, argumentBean);
    }

    private boolean propertiesEqual(AbstractBean<T> bean, AbstractBean<T> otherBean) {
        return bean.getProperties().equals(otherBean.getProperties());
    }
}
