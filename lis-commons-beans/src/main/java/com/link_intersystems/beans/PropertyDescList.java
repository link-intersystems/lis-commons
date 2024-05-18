package com.link_intersystems.beans;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyDescList extends AbstractList<PropertyDesc> {

    private List<PropertyDesc> descriptors = new ArrayList<>();

    public PropertyDescList(List<? extends PropertyDesc> descriptors) {
        this.descriptors.addAll(descriptors);
    }

    public List<String> getPropertyNames() {
        return stream().map(PropertyDesc::getName).collect(Collectors.toList());
    }

    public PropertyDesc getByName(String propertyName) {
        return getByName(propertyName, Objects::equals);
    }

    public PropertyDesc getByName(String propertyName, BiFunction<String, String, Boolean> nameEquality) {
        return stream().filter(pd -> nameEquality.apply(propertyName, pd.getName())).findFirst().orElse(null);
    }

    public boolean containsProperty(String propertyName) {
        return getByName(propertyName) != null;
    }

    @Override
    public PropertyDesc get(int index) {
        return descriptors.get(index);
    }

    @Override
    public int size() {
        return descriptors.size();
    }

}
