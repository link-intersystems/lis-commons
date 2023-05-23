package com.link_intersystems.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

public interface ActionJoinPoint {

    /**
     * Proceed with the caller's action event.
     */
    default public void proceed() {
        proceed(() -> {
        });
    }

    /**
     * Proceed with the caller's action event.
     */
    public void proceed(Runnable finallyRunnable);

    /**
     * Proceed with the given action event.
     *
     * @param actionEvent the action event to proceed with.
     */
    default public void proceed(ActionEvent actionEvent) {
        proceed(actionEvent, () -> {
        });
    }

    /**
     * Proceed with the given action event.
     *
     * @param actionEvent the action event to proceed with.
     */
    public void proceed(ActionEvent actionEvent, Runnable finallyRunnable);

    /**
     * The target action. Calling {@link Action#actionPerformed(ActionEvent)} on the
     * given action will have the same result als calling {@link #proceed(ActionEvent)}.
     *
     * @return
     */
    public Action getAction();
}
