package com.link_intersystems.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.event.ActionEvent.*;
import static java.util.Objects.*;

public class ActionTrigger {

    public static void performAction(Object actionEventSource, ActionListener actionListener) {
        performAction(actionEventSource, actionListener, "");
    }

    public static void performAction(Object actionEventSource, ActionListener actionListener, String command) {
        actionListener.actionPerformed(new ActionEvent(actionEventSource, ACTION_PERFORMED, command));
    }

    private Object actionEventSource;

    public ActionTrigger(Object actionEventSource) {
        this.actionEventSource = requireNonNull(actionEventSource);
    }

    public void performAction(ActionListener actionListener) {
        performAction(actionListener, "");
    }

    public void performAction(ActionListener actionListener, String command) {
        performAction(actionEventSource, actionListener, command);
    }
}
