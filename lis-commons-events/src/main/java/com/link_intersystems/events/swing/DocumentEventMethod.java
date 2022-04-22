package com.link_intersystems.events.swing;

import com.link_intersystems.events.EventMethod;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DocumentEventMethod extends EventMethod<DocumentListener, DocumentEvent> {

    public static final String INSERT_UPDATE_NAME = "insertUpdate";
    public static final String REMOVE_UPDATE_NAME = "removeUpdate";
    public static final String CHANGED_UPDATE_NAME = "changedUpdate";

    public static final DocumentEventMethod INSERT_UPDATE = new DocumentEventMethod(INSERT_UPDATE_NAME);
    public static final DocumentEventMethod REMOVE_UPDATE = new DocumentEventMethod(REMOVE_UPDATE_NAME);
    public static final DocumentEventMethod CHANGED_UPDATE = new DocumentEventMethod(CHANGED_UPDATE_NAME);

    public DocumentEventMethod(String... methodNames) {
        super(methodNames);
    }
}
