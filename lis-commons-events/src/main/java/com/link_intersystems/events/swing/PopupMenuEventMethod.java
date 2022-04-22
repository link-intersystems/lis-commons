package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PopupMenuEventMethod extends EventMethod<PopupMenuListener, PopupMenuEvent> {

    public static final String WILL_BECOME_VISIBLE_NAME = "popupMenuWillBecomeVisible";
    public static final String WILL_BECOME_INVISIBLE_NAME = "popupMenuWillBecomeInvisible";
    public static final String CANCELED_NAME = "popupMenuCanceled";

    public static final PopupMenuEventMethod WILL_BECOME_VISIBLE = new PopupMenuEventMethod(WILL_BECOME_VISIBLE_NAME);
    public static final PopupMenuEventMethod WILL_BECOME_INVISIBLE = new PopupMenuEventMethod(WILL_BECOME_INVISIBLE_NAME);
    public static final PopupMenuEventMethod CANCELED = new PopupMenuEventMethod(CANCELED_NAME);

    public PopupMenuEventMethod(String... methodNames) {
        super(methodNames);
    }
}
