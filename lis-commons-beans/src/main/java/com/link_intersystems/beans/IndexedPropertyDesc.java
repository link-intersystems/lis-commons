package com.link_intersystems.beans;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface IndexedPropertyDesc extends PropertyDesc {

    public boolean isIndexedReadable();

    public boolean isIndexedWritable();

    public <T> T getPropertyValue(Object bean, int index) throws PropertyReadException;

    public void setPropertyValue(Object bean, int index, Object value);
}
