package com.link_intersystems.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.event.ActionEvent.*;
import static java.util.Objects.*;

public class ActionTrigger {

    public static final String DEFAULT_COMMAND = "";

    public static void performAction(Object actionEventSource, ActionListener actionListener) {
        performAction(actionEventSource, actionListener, "");
    }

    public static void performAction(Object actionEventSource, ActionListener actionListener, String command) {
        performAction(actionEventSource, actionListener, command, ACTION_PERFORMED);
    }

    public static void performAction(Object actionEventSource, ActionListener actionListener, String command, int actionId) {
        ActionEvent actionEvent = new ActionEvent(actionEventSource, actionId, command);
        actionListener.actionPerformed(actionEvent);
    }

    private Object actionEventSource;
    private int actionId;
    private String command;

    /**
     * Constructs an {@link ActionTrigger} with the {@link #DEFAULT_COMMAND} and the #ACTION_PERFORMED actionId.
     *
     * @param actionEventSource
     */
    public ActionTrigger(Object actionEventSource) {
        this(actionEventSource, DEFAULT_COMMAND);
    }

    /**
     * Constructs an {@link ActionTrigger} with the given command and the #ACTION_PERFORMED actionId.
     *
     * @param actionEventSource
     * @param command
     */
    public ActionTrigger(Object actionEventSource, String command) {
        this(actionEventSource, command, ACTION_PERFORMED);
    }

    /**
     * Constructs an {@link ActionTrigger} with the given command and the given actionId.
     *
     * @param actionEventSource
     * @param command
     * @param actionId
     */
    public ActionTrigger(Object actionEventSource, String command, int actionId) {
        this.actionEventSource = requireNonNull(actionEventSource);
        this.command = requireNonNull(command);
        this.actionId = actionId;
    }

    /**
     * Invokes the given actionListener with an {@link ActionEvent} that has this
     * {@link ActionTrigger}'s actionEventSource as it's {@link ActionEvent#getSource()},
     * this {@link ActionTrigger}'s command as it's {@link ActionEvent#getActionCommand()}
     * and this {@link ActionTrigger}'s actionId as it's {@link ActionEvent#getID()} ()}
     * The {@link ActionEvent#getID()}.
     * <p>
     * You can use one of the other methods to perform an action with another command or actionId then this action.
     *
     * @param actionListener
     */
    public void performAction(ActionListener actionListener) {
        performAction(actionEventSource, actionListener, command, actionId);
    }

    public Runnable asRunnable(ActionListener actionListener) {
        return () -> performAction(actionListener);
    }

}
