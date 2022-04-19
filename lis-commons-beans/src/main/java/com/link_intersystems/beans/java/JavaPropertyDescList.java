package com.link_intersystems.beans.java;

import com.link_intersystems.beans.PropertyDescList;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaPropertyDescList extends PropertyDescList {

    private List<? extends JavaPropertyDesc> descriptors;

    public JavaPropertyDescList(List<? extends JavaPropertyDesc> descriptors) {
        super(descriptors);
        this.descriptors = descriptors;
    }

    public JavaPropertyDesc getByMethod(Method method) {
        return descriptors.stream().filter(jpd -> jpd.hasMethod(method)).findFirst().orElse(null);
    }
}
