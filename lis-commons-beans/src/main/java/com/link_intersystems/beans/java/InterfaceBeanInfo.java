package com.link_intersystems.beans.java;

import java.awt.*;
import java.beans.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An {@link InterfaceBeanInfo} is a {@link BeanInfo} whose {@link #getAdditionalBeanInfo()} method
 * returns the {@link BeanInfo}s of the main {@link BeanInfo}'s class interfaces.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class InterfaceBeanInfo extends SimpleBeanInfo {


    private final List<Class<?>> filterInterfaces;
    private final BeanInfo mainBeanInfo;
    private BeanInfo[] additionalBeanInfo;

    public <T, B extends T> InterfaceBeanInfo(Class<T> beanInterface, Class<?>... filterInterfaces) throws IntrospectionException {
        mainBeanInfo = Introspector.getBeanInfo(beanInterface);
        this.filterInterfaces = Arrays.asList(filterInterfaces);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return mainBeanInfo.getBeanDescriptor();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return mainBeanInfo.getEventSetDescriptors();
    }

    @Override
    public int getDefaultEventIndex() {
        return mainBeanInfo.getDefaultEventIndex();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return mainBeanInfo.getPropertyDescriptors();
    }

    @Override
    public int getDefaultPropertyIndex() {
        return mainBeanInfo.getDefaultPropertyIndex();
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return mainBeanInfo.getMethodDescriptors();
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        if (additionalBeanInfo == null) {
            additionalBeanInfo = doGetAdditionalBeanInfo();
        }
        return additionalBeanInfo;
    }

    private BeanInfo[] doGetAdditionalBeanInfo() {
        BeanDescriptor beanDescriptor = mainBeanInfo.getBeanDescriptor();
        Class<?> mainBeanClass = beanDescriptor.getBeanClass();
        List<BeanInfo> interfacesAdditionalBeanInfo = getInterfacesAdditionalBeanInfo(mainBeanClass);
        return interfacesAdditionalBeanInfo.toArray(new BeanInfo[0]);
    }

    private List<BeanInfo> getInterfacesAdditionalBeanInfo(Class<?> beanClass) {
        List<BeanInfo> additionalBeanInfo = new ArrayList<>();

        Class<?>[] interfaces = beanClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (!filterInterfaces.contains(anInterface)) {
                List<BeanInfo> superInterfaceBeanInfo = getAdditionalBeanInfo(anInterface);
                additionalBeanInfo.addAll(superInterfaceBeanInfo);
            }
        }

        return additionalBeanInfo;
    }

    private List<BeanInfo> getAdditionalBeanInfo(Class<?> beanClass) {
        List<BeanInfo> additionalBeanInfo = new ArrayList<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            additionalBeanInfo.add(beanInfo);

            List<BeanInfo> interfacesAdditionalBeanInfo = getInterfacesAdditionalBeanInfo(beanClass);
            additionalBeanInfo.addAll(interfacesAdditionalBeanInfo);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        return additionalBeanInfo;
    }

    @Override
    public Image getIcon(int iconKind) {
        return mainBeanInfo.getIcon(iconKind);
    }
}
