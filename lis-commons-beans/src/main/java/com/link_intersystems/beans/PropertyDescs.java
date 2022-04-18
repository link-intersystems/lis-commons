package com.link_intersystems.beans;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyDescs<T extends PropertyDesc> extends AbstractList<T> {

    private List<T> descriptors = new ArrayList<>();

    public PropertyDescs(List<T> descriptors) {
        this.descriptors.addAll(descriptors);
    }

    public List<String> getAllPropertyNames() {
        return stream().map(PropertyDesc::getName).collect(Collectors.toList());
    }

    /**
     * @return all properties that are not indexed properties.
     */
    public List<String> getPropertyNames() {
        return stream().filter(pd -> !(pd instanceof IndexedPropertyDesc)).map(PropertyDesc::getName).collect(Collectors.toList());
    }

    public List<String> getIndexedPropertyNames() {
        return stream().filter(IndexedPropertyDesc.class::isInstance).map(PropertyDesc::getName).collect(Collectors.toList());
    }

    public PropertyDesc getByName(String propertyName) {
        return stream().filter(pd -> pd.getName().equals(propertyName)).findFirst().orElse(null);
    }


    @Override
    public T get(int index) {
        return descriptors.get(index);
    }

    @Override
    public int size() {
        return descriptors.size();
    }

}
