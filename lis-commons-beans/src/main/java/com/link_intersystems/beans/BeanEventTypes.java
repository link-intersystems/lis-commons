package com.link_intersystems.beans;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanEventTypes<T extends BeanEventType> extends AbstractList<T> {

    private List<T> beanEventTypes = new ArrayList<>();
    private Map<String, T> beanEventTypesByName;

    public BeanEventTypes(List<T> beanEventTypes) {
        this.beanEventTypes.addAll(beanEventTypes);
    }

    public T getByName(String eventName) {
        return getBeanEventTypesByName().get(eventName);
    }

    private Map<String, T> getBeanEventTypesByName() {
        if (beanEventTypesByName == null) {
            Collector<T, ?, Map<String, T>> mapByName = toMap(BeanEventType::getName, identity());
            beanEventTypesByName = this.stream().collect(mapByName);
        }
        return beanEventTypesByName;
    }

    @Override
    public T get(int index) {
        return beanEventTypes.get(index);
    }

    @Override
    public int size() {
        return beanEventTypes.size();
    }
}
