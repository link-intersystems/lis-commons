package com.link_intersystems.swing.view.layout;

import com.link_intersystems.swing.view.View;
import com.link_intersystems.swing.view.ViewSite;
import com.link_intersystems.util.context.DefaultContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultViewLayoutTest {

    private DefaultViewLayout defaultViewLayout;
    private Container container;
    private DefaultContext context;
    private View view;

    @BeforeEach
    void setUp() {
        context = new DefaultContext();
        container = mock(Container.class);
        defaultViewLayout = new DefaultViewLayout(context, container);

        defaultViewLayout.addViewSite("N", BorderLayout.NORTH);
        defaultViewLayout.addViewSite("S", BorderLayout.SOUTH);
        defaultViewLayout.addViewSite("C", BorderLayout.CENTER);

        view = mock(View.class);
    }

    @Test
    void installUnknownViewSite() {
        assertThrows(IllegalArgumentException.class, () -> defaultViewLayout.install("NORTH", view));
    }

    @Test
    void install() {
        defaultViewLayout.install("N", view);

        verify(view, times(1)).install(Mockito.any(ViewSite.class));
        verify(container, times(1)).revalidate();
    }

    @Test
    void dispose() {
        install();
        reset(container);

        defaultViewLayout.dispose();

        verify(view, times(1)).uninstall();
        verify(container, times(1)).revalidate();
    }
}