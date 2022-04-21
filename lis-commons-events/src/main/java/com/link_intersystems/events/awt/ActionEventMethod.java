package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ActionEventMethod extends EventMethod<ActionListener, ActionEvent> {

    public static final String PERFORMED_NAME = "actionPerformed";

    public static final ActionEventMethod PERFORMED = new ActionEventMethod(PERFORMED_NAME);

    public ActionEventMethod(String... methodNames) {
        super(methodNames);
    }
}
