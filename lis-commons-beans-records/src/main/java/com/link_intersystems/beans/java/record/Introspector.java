package com.link_intersystems.beans.java.record;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

public class Introspector {

    public static BeanInfo getBeanInfo(Class<?> recordType) throws IntrospectionException {
        return new RecordBeanInfo(recordType);
    }
}
