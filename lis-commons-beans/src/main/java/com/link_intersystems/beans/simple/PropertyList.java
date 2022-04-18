package com.link_intersystems.beans.simple;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author - René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PropertyList extends AbstractList<SimpleProperty> {

    private List<SimpleProperty> properties = new ArrayList<>();

    public PropertyList(List<SimpleProperty> properties) {

        this.properties.addAll(properties);
    }

    public SimpleProperty getByName(String name) {
        return getByName(name, Objects::equals);
    }

    public SimpleProperty getByName(String name, Equality<String> nameEquality) {
        return stream().filter(pd -> nameEquality.isEqual(name, pd.getName())).findFirst().orElse(null);
    }

    @Override
    public SimpleProperty get(int index) {
        return properties.get(index);
    }

    @Override
    public int size() {
        return properties.size();
    }

    public SimpleProperty[] asArray() {
        return toArray(new SimpleProperty[size()]);
    }
}
