package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ItemEventMethod extends EventMethod<ItemListener, ItemEvent> {

    public static final String STATE_CHANGED_NAME = "itemStateChanged";

    public static final ItemEventMethod STATE_CHANGED = new ItemEventMethod(STATE_CHANGED_NAME);

    public ItemEventMethod(String... methodNames) {
        super(methodNames);
    }
}
