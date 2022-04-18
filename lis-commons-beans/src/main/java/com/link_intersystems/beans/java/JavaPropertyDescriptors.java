package com.link_intersystems.beans.java;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyDescriptors extends AbstractList<PropertyDescriptor> {

    private List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

    public JavaPropertyDescriptors(BeanInfo beanInfo) {
        this(beanInfo.getPropertyDescriptors());
    }

    public JavaPropertyDescriptors(List<PropertyDescriptor> propertyDescriptors) {
        this.propertyDescriptors.addAll(propertyDescriptors);
    }

    public JavaPropertyDescriptors(PropertyDescriptor[] propertyDescriptors) {
        this(Arrays.asList(propertyDescriptors));
    }

    public PropertyDescriptor getByName(String propertyName) {
        return stream().filter(pd -> pd.getName().equals(propertyName)).findFirst().orElse(null);
    }

    @Override
    public PropertyDescriptor get(int index) {
        return propertyDescriptors.get(index);
    }

    @Override
    public int size() {
        return propertyDescriptors.size();
    }
}
