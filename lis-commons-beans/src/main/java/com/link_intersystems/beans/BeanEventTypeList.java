package com.link_intersystems.beans;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanEventTypeList extends AbstractList<BeanEventType> {

    public static final BeanEventTypeList EMPTY = new BeanEventTypeList(Collections.emptyList());

    private List<BeanEventType> beanEventTypes = new ArrayList<>();
    private Map<String, BeanEventType> byName;

    public BeanEventTypeList(List<? extends BeanEventType> beanEventTypes) {
        this.beanEventTypes.addAll(beanEventTypes);
    }

    public BeanEventType getByName(String eventName) {
        if (byName == null) {
            byName = stream().collect(toMap(BeanEventType::getName, identity()));
        }
        return byName.get(eventName);
    }

    public boolean isListenerSupported(Class<? extends EventListener> listenerClass) {
        return stream()
                .anyMatch(be -> be.isApplicable(listenerClass));
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
