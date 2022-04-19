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
public class BeanEventTypes extends AbstractList<BeanEventType> {

    private List<BeanEventType> beanEventTypes = new ArrayList<>();
    private Map<String, BeanEventType> beanEventTypesByName;

    public BeanEventTypes(List<? extends BeanEventType> beanEventTypes) {
        this.beanEventTypes.addAll(beanEventTypes);
    }

    public BeanEventType getByName(String eventName) {
        return getBeanEventTypesByName().get(eventName);
    }

    private Map<String, BeanEventType> getBeanEventTypesByName() {
        if (beanEventTypesByName == null) {
            Collector<BeanEventType, ?, Map<String, BeanEventType>> mapByName = toMap(com.link_intersystems.beans.BeanEventType::getName, identity());
            beanEventTypesByName = this.stream().collect(mapByName);
        }
        return beanEventTypesByName;
    }

    @Override
    public BeanEventType get(int index) {
        return beanEventTypes.get(index);
    }

    @Override
    public int size() {
        return beanEventTypes.size();
    }
}
