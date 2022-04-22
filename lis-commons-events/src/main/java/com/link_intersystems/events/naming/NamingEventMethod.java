package com.link_intersystems.events.naming;

import com.link_intersystems.events.EventMethod;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamingListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class NamingEventMethod extends EventMethod<NamingListener, NamingExceptionEvent> {

    public static final String EXCEPTION_THROWN_NAME = "namingExceptionThrown";

    public static final NamingEventMethod EXCEPTION_THROWN = new NamingEventMethod(EXCEPTION_THROWN_NAME);

    public NamingEventMethod(String... methodNames) {
        super(methodNames);
    }
}
