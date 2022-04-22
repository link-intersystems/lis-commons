package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class MenuEventMethod extends EventMethod<MenuListener, MenuEvent> {

    public static final String SELECTED_NAME = "menuSelected";
    public static final String DESELECTED_NAME = "menuDeselected";
    public static final String CANCELED_NAME = "menuCanceled";

    public static final MenuEventMethod SELECTED = new MenuEventMethod(SELECTED_NAME);
    public static final MenuEventMethod DESELECTED = new MenuEventMethod(DESELECTED_NAME);
    public static final MenuEventMethod CANCELED = new MenuEventMethod(CANCELED_NAME);

    public MenuEventMethod(String... methodNames) {
        super(methodNames);
    }
}
