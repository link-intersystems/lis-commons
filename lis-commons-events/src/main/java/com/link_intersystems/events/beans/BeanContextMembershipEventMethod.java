package com.link_intersystems.events.beans;

import com.link_intersystems.events.EventMethod;

import java.beans.beancontext.BeanContextMembershipEvent;
import java.beans.beancontext.BeanContextMembershipListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class BeanContextMembershipEventMethod extends EventMethod<BeanContextMembershipListener, BeanContextMembershipEvent> {

    public static final String CHILDREN_ADDED_NAME = "childrenAdded";
    public static final String CHILDREN_REMOVED_NAME = "childrenRemoved";

    public static final BeanContextMembershipEventMethod CHILDREN_ADDED = new BeanContextMembershipEventMethod(CHILDREN_ADDED_NAME);
    public static final BeanContextMembershipEventMethod CHILDREN_REMOVED = new BeanContextMembershipEventMethod(CHILDREN_REMOVED_NAME);

    public BeanContextMembershipEventMethod(String... methodNames) {
        super(methodNames);
    }
}
