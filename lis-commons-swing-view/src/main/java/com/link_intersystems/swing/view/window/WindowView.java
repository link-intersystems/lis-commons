package com.link_intersystems.swing.view.window;

import com.link_intersystems.events.awt.WindowEventMethod;
import com.link_intersystems.swing.view.AbstractView;
import com.link_intersystems.swing.view.ViewContent;
import com.link_intersystems.swing.view.ViewSite;
import com.link_intersystems.util.context.Context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public abstract class WindowView extends AbstractView {

    public static final String DEFAULT_CLOSE_ACTION = WindowView.class.getName() + ".closeAction";
    private Window window;

    private int closeOperation;
    private WindowListener closeHandler;

    public WindowView() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setWindowCloseMethod(WindowEventMethod.CLOSING);
    }

    @Override
    protected void doInstall(ViewSite viewSite) {
        window = createWindow(viewSite);
        setDefaultCloseOperation(window, closeOperation);
        window.addWindowListener(closeHandler);
        ViewContent viewContent = viewSite.getViewContent();
        viewContent.setComponent(window);
    }

    /**
     * @param windowConstantsValue a value of {@link WindowConstants}.
     */
    public void setDefaultCloseOperation(int windowConstantsValue) {
        setDefaultCloseOperation(window, windowConstantsValue);
    }

    /**
     * Sets the event method that will cause a {@link WindowCloseAction}, registered to the {@link Context}
     * with the {@link #DEFAULT_CLOSE_ACTION} name,  to be performed.
     *
     * @param windowCloseMethod a constant of {@link WindowEventMethod}. Default is #{@link WindowEventMethod#CLOSING}.
     */
    public void setWindowCloseMethod(WindowEventMethod windowCloseMethod) {
        requireNonNull(windowCloseMethod);

        if (window != null) {
            window.removeWindowListener(closeHandler);
        }

        closeHandler = windowCloseMethod.listener((e) -> onCloseWindow(e, getViewSite()));

        if (window != null) {
            window.addWindowListener(closeHandler);
        }
    }

    protected void setDefaultCloseOperation(Window window, int closeOperation) {
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.setDefaultCloseOperation(closeOperation);
        } else if (window instanceof JDialog) {
            JDialog dialog = (JDialog) window;
            dialog.setDefaultCloseOperation(closeOperation);
        }
    }

    protected abstract Window createWindow(ViewSite viewSite);

    @Override
    protected void doUninstall(ViewSite viewSite) {
        super.doUninstall(viewSite);

        window.removeWindowListener(closeHandler);
        closeHandler = null;

        window.dispose();
        window = null;
    }

    protected void onCloseWindow(WindowEvent windowEvent, ViewSite viewSite) {
        Context viewContext = viewSite.getViewContext();

        Optional<WindowCloseAction> windowCloseAction = viewContext.find(WindowCloseAction.class, DEFAULT_CLOSE_ACTION);

        windowCloseAction.orElse(WindowCloseAction.DEFAULT).actionPerformed(windowEvent, this);
    }
}
