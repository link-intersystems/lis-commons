package com.link_intersystems.beans.mockito;

import com.link_intersystems.beans.BeansFactory;

public class BeanMatchers {

    public static <T> T propertiesEqual(Object aBean, String... excludeProperties) {
        return BeanMatcher.DEFAULT.propertiesEqual(aBean, excludeProperties);
    }

    public static <T> T propertiesEqual(BeansFactory beansFactory, Object aBean, String... excludeProperties) {
        BeanMatcher beanMatcher = new BeanMatcher(beansFactory);
        beanMatcher.propertiesEqual(aBean, excludeProperties);
        return null;
    }

}
