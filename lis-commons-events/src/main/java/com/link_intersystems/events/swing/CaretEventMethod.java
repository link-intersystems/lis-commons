package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class CaretEventMethod extends EventMethod<CaretListener, CaretEvent> {

    public static final String UPDATE_NAME = "caretUpdate";

    public static final CaretEventMethod UPDATE = new CaretEventMethod(UPDATE_NAME);

    public CaretEventMethod(String... methodNames) {
        super(methodNames);
    }
}
