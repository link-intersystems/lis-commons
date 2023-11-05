package com.link_intersystems.swing.view.window;

import com.link_intersystems.events.awt.WindowEventMethod;
import com.link_intersystems.swing.view.View;
import com.link_intersystems.swing.view.ViewSite;
import com.link_intersystems.swing.view.ViewSiteMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WindowViewTest {

    private JFrame frame;
    private WindowView windowView;
    private ViewSiteMock viewSiteMock;

    @BeforeEach
    void setUp() {
        frame = new JFrame();
        windowView = new WindowView() {
            @Override
            protected Window createWindow(ViewSite viewSite) {

                return frame;
            }
        };

        viewSiteMock = new ViewSiteMock();
    }

    @Test
    void install() {
        windowView.install(viewSiteMock);

        assertEquals(frame, viewSiteMock.getContent());
    }

    @Test
    void uninstall() {
        install();

        WindowCloseAction closeAction = spy(WindowCloseAction.class);
        viewSiteMock.addContextObject(WindowCloseAction.class, WindowView.DEFAULT_CLOSE_ACTION, closeAction);

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        verify(closeAction, times(1)).actionPerformed(any(WindowEvent.class), any(View.class));

        assertNull(viewSiteMock.getContent(), "frame uninstalled");
    }

    @Test
    void setWindowCloseMethod() {
        install();

        windowView.setWindowCloseMethod(WindowEventMethod.CLOSED);

        WindowCloseAction closeAction = spy(WindowCloseAction.class);
        viewSiteMock.addContextObject(WindowCloseAction.class, WindowView.DEFAULT_CLOSE_ACTION, closeAction);

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSED));

        verify(closeAction, times(1)).actionPerformed(any(WindowEvent.class), any(View.class));

        assertNull(viewSiteMock.getContent(), "frame uninstalled");
    }
}