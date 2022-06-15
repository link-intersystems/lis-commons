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

    private BeanInfo mainBeanInfo;
    private boolean includeMainBeanInfo = true;

    public MergedAdditionalBeanInfo(BeanInfo mainBeanInfo) {
        this.mainBeanInfo = requireNonNull(mainBeanInfo);
    }

    public void setIncludeMainBeanInfo(boolean includeMainBeanInfo) {
        this.includeMainBeanInfo = includeMainBeanInfo;
    }

    public boolean isIncludeMainBeanInfo() {
        return includeMainBeanInfo;
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
        return getBeanInfos().stream()
                .map(descriptorGetter)
                .map(Arrays::asList)
                .flatMap(List::stream)
                .toArray(length -> (T[]) Array.newInstance(descriptorType, length));
    }

    private List<BeanInfo> getBeanInfos() {
        List<BeanInfo> allBeanInfos = new ArrayList<>();

        if (includeMainBeanInfo) {
            allBeanInfos.add(mainBeanInfo);
        }

        BeanInfo[] additionalBeanInfo = getAdditionalBeanInfo();
        if (additionalBeanInfo != null) {
            allBeanInfos.addAll(Arrays.asList(additionalBeanInfo));
        }

        return allBeanInfos;
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return mainBeanInfo.getBeanDescriptor();
    }

    @Override
    public int getDefaultEventIndex() {
        return mainBeanInfo.getDefaultEventIndex();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return mainBeanInfo.getDefaultPropertyIndex();
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        return mainBeanInfo.getAdditionalBeanInfo();
    }

    @Override
    public Image getIcon(int iconKind) {
        return mainBeanInfo.getIcon(iconKind);
    }
}
