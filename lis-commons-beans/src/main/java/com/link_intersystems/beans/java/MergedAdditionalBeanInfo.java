package com.link_intersystems.beans.java;

import java.awt.*;
import java.beans.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MergedAdditionalBeanInfo extends SimpleBeanInfo {

    private BeanInfo beanInfo;

    public MergedAdditionalBeanInfo(BeanInfo beanInfo) {
        this.beanInfo = requireNonNull(beanInfo);
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getDescriptors(BeanInfo::getEventSetDescriptors, EventSetDescriptor.class);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getDescriptors(BeanInfo::getPropertyDescriptors, PropertyDescriptor.class);
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getDescriptors(BeanInfo::getMethodDescriptors, MethodDescriptor.class);
    }


    @SuppressWarnings("unchecked")
    private <T> T[] getDescriptors(Function<BeanInfo, T[]> descriptorGetter, Class<T> descriptorType) {
        return getAdditionalBeanInfos().stream()
                .map(descriptorGetter)
                .map(Arrays::asList)
                .flatMap(List::stream)
                .toArray(length -> (T[]) Array.newInstance(descriptorType, length));
    }

    private List<BeanInfo> getAdditionalBeanInfos() {
        List<BeanInfo> allBeanInfos = new ArrayList<>();

        BeanInfo[] additionalBeanInfo = getAdditionalBeanInfo();
        if (additionalBeanInfo != null) {
            allBeanInfos.addAll(Arrays.asList(additionalBeanInfo));
        }

        return allBeanInfos;
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return beanInfo.getBeanDescriptor();
    }

    @Override
    public int getDefaultEventIndex() {
        return beanInfo.getDefaultEventIndex();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return beanInfo.getDefaultPropertyIndex();
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        return beanInfo.getAdditionalBeanInfo();
    }

    @Override
    public Image getIcon(int iconKind) {
        return beanInfo.getIcon(iconKind);
    }
}
