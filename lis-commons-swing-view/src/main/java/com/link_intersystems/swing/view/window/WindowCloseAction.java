package com.link_intersystems.swing.view.window;

import com.link_intersystems.swing.action.ActionTrigger;
import com.link_intersystems.swing.view.View;

import javax.swing.*;
import java.awt.event.WindowEvent;

public interface WindowCloseAction {

    public static final WindowCloseAction DEFAULT = new WindowCloseAction() {
    };

    public static WindowCloseAction actionAdapter(Action action) {
        return new WindowCloseAction() {
            @Override
            public void actionPerformed(WindowEvent windowEvent, View view) {
                ActionTrigger.performAction(windowEvent.getSource(), action);
                WindowCloseAction.super.actionPerformed(windowEvent, view);
            }
        };
    }

    default
    public void actionPerformed(WindowEvent windowEvent, View view) {
        view.uninstall();
    }
}
