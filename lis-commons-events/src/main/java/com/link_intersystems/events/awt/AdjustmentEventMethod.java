package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class AdjustmentEventMethod extends EventMethod<AdjustmentListener, AdjustmentEvent> {

    public static final String VALUE_CHANGED_NAME = "adjustmentValueChanged";

    public static final AdjustmentEventMethod VALUE_CHANGED = new AdjustmentEventMethod(VALUE_CHANGED_NAME);

    public AdjustmentEventMethod(String... methodNames) {
        super(methodNames);
    }
}
