package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.java.JavaBean;
import com.link_intersystems.beans.java.JavaBeanClass;

public class RecordBean<T> extends JavaBean<T> {

    protected RecordBean(JavaBeanClass<T> beanClass, T bean) {
        super(beanClass, bean);
    }

}
