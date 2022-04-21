package com.link_intersystems.events.awt;

import com.link_intersystems.events.EventMethod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class KeyEventMethod extends EventMethod<KeyListener, KeyEvent> {

    public static final String TYPED_NAME = "keyTyped";
    public static final String PRESSED_NAME = "keyPressed";
    public static final String RELEASED_NAME = "keyReleased";

    public static final KeyEventMethod TYPED = new KeyEventMethod(TYPED_NAME);
    public static final KeyEventMethod PRESSED = new KeyEventMethod(PRESSED_NAME);
    public static final KeyEventMethod RELEASED = new KeyEventMethod(RELEASED_NAME);

    public KeyEventMethod(String... methodNames) {
        super(methodNames);
    }
}
