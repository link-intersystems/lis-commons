package com.link_intersystems.events.beans;

import com.link_intersystems.events.EventMethod;

import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServiceRevokedListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanContextServiceRevokedEventMethod extends EventMethod<BeanContextServiceRevokedListener, BeanContextServiceRevokedEvent> {

    public static final String REVOKED_NAME = "serviceRevoked";
    public static final String CHILDREN_REMOVED_NAME = "childrenRemoved";

    public static final BeanContextServiceRevokedEventMethod REVOKED = new BeanContextServiceRevokedEventMethod(REVOKED_NAME);

    public BeanContextServiceRevokedEventMethod(String... methodNames) {
        super(methodNames);
    }
}
