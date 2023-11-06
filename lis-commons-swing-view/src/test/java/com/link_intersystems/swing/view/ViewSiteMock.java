package com.link_intersystems.swing.view;

import com.link_intersystems.util.context.Context;
import com.link_intersystems.util.context.DefaultContext;

import javax.swing.*;
import java.awt.*;

public class ViewSiteMock implements ViewSite {

    private ViewContent viewContent = new ViewContent() {
        @Override
        public void setComponent(Component component) {
            content = component;
        }

        @Override
        public Component getParent() {
            return parent;
        }
    };

    private DefaultContext context = new DefaultContext();

    private Component content;
    private Component parent;

    public Component getContent() {
        return content;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    @Override
    public ViewContent getViewContent() {
        return viewContent;
    }

    @Override
    public Context getViewContext() {
        return context;
    }

    public <T> void addContextObject(Class<T> type, String name, T contextObject) {
        context.put(type, name, contextObject);
    }
}
