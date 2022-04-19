package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
@FunctionalInterface
public interface PropertyDescFilter {

    public boolean accept(PropertyDesc propertyDesc);
}
