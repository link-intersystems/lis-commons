package com.link_intersystems.swing.view.window;

import com.link_intersystems.beans.mockito.BeanMatchers;
import com.link_intersystems.swing.view.View;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import static org.mockito.Mockito.*;

class WindowCloseActionTest {

    @Test
    void actionAdapter() {
        Action action = mock(Action.class);
        Window window = mock(Window.class);
        View view = mock(View.class);

        WindowCloseAction windowCloseAction = WindowCloseAction.actionAdapter(action);

        windowCloseAction.actionPerformed(new WindowEvent(window, WindowEvent.WINDOW_CLOSING), view);

        ActionEvent actionEvent = new ActionEvent(window, ActionEvent.ACTION_PERFORMED, "");

        verify(action, times(1)).actionPerformed(BeanMatchers.propertiesEqual(actionEvent));
    }
}