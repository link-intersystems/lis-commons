package com.link_intersystems.swing.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.event.ActionEvent.*;
import static org.junit.jupiter.api.Assertions.*;

class ActionTriggerTest implements ActionListener {

    private ActionEvent actionEvent;
    private ActionTrigger actionTrigger;

    @BeforeEach
    void setUp() {
        actionEvent = null;
        actionTrigger = new ActionTrigger(this);
    }

    @Test
    void performAction() {
        actionTrigger.performAction(this);

        assertEquals(this, actionEvent.getSource());
        assertEquals(ACTION_PERFORMED, actionEvent.getID());
        assertEquals("", actionEvent.getActionCommand());
    }

    @Test
    void performActionWithCommand() {
        actionTrigger = new ActionTrigger(this, "performActionWithCommand");

        actionTrigger.performAction(this);

        assertEquals(this, actionEvent.getSource());
        assertEquals(ACTION_PERFORMED, actionEvent.getID());
        assertEquals("performActionWithCommand", actionEvent.getActionCommand());
    }

    @Test
    void asRunnable() {
        actionTrigger = new ActionTrigger(this);
        Runnable runnable = actionTrigger.asRunnable(this);

        runnable.run();

        assertEquals(this, actionEvent.getSource());
        assertEquals(ACTION_PERFORMED, actionEvent.getID());
    }

    @Test
    void staticPerformAction() {
        ActionTrigger.performAction(this, this);

        assertEquals(this, actionEvent.getSource());
        assertEquals(ACTION_PERFORMED, actionEvent.getID());
        assertEquals("", actionEvent.getActionCommand());
    }

    @Test
    void staticPerformActionWithCommand() {
        ActionTrigger.performAction(this, this, "performActionWithCommand");

        assertEquals(this, actionEvent.getSource());
        assertEquals(ACTION_PERFORMED, actionEvent.getID());
        assertEquals("performActionWithCommand", actionEvent.getActionCommand());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionEvent = e;
    }
}