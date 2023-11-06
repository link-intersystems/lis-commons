package com.link_intersystems.swing.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContainerViewContentTest {

    private JPanel container;
    private LayoutManager2 layoutManager;
    private ContainerViewContent containerViewContent;

    @BeforeEach
    void setUp() {
        container = new JPanel();
        layoutManager = mock(LayoutManager2.class);
        container.setLayout(layoutManager);

        containerViewContent = new ContainerViewContent(container, BorderLayout.SOUTH);
    }


    @Test
    void getParent() {

        assertEquals(container, containerViewContent.getParent());
    }

    @Test
    void setComponent() {
        JLabel viewContent = new JLabel();
        containerViewContent.setComponent(viewContent);

        assertEquals(1, container.getComponents().length);
        assertEquals(viewContent, container.getComponents()[0]);
        verify(layoutManager, times(1)).addLayoutComponent(viewContent, BorderLayout.SOUTH);
    }

    @Test
    void resetComponent() {
        setComponent();

        JLabel anotherLabel = new JLabel();
        containerViewContent.setComponent(anotherLabel);

        assertEquals(1, container.getComponents().length);
        assertEquals(anotherLabel, container.getComponents()[0]);

        verify(layoutManager, times(1)).addLayoutComponent(anotherLabel, BorderLayout.SOUTH);
    }
}