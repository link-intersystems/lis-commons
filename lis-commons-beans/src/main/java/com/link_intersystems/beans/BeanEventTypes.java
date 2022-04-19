package com.link_intersystems.beans;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanEventTypes extends AbstractList<BeanEventType> {

    private List<BeanEventType> beanEventTypes = new ArrayList<>();
    private Map<String, BeanEventType> byName;

    public BeanEventTypes(List<? extends BeanEventType> beanEventTypes) {
        this.beanEventTypes.addAll(beanEventTypes);
    }

    public BeanEventType getByName(String eventName) {
        if (byName == null) {
            byName = stream().collect(toMap(BeanEventType::getName, identity()));
        }
        return byName.get(eventName);
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
