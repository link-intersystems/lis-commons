package com.link_intersystems.swing.view.window;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class WindowViewContentTest {

    private WindowViewContent windowViewContent;

    @BeforeEach
    void setUp() {
        windowViewContent = new WindowViewContent();


    }

    @Test
    void setComponent() {
        Component component = mock(Component.class);
        windowViewContent.setComponent(component);

        verify(component, times(1)).setVisible(true);
    }

    @Test
    void resetComponent() {
        setComponent();

        Component component = mock(Component.class);
        windowViewContent.setComponent(component);

        verify(component, times(1)).setVisible(true);
    }

    @Test
    void getParent() {

        assertNull(windowViewContent.getParent());
    }
}