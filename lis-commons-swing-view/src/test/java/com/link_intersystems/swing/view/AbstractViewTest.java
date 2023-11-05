package com.link_intersystems.swing.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractViewTest {

    private AbstractView abstractView;
    private ViewSiteMock viewSiteMock;

    @Test
    void install() {
        abstractView = spy(AbstractView.class);

        assertNull(abstractView.getContextDsl());

        viewSiteMock = new ViewSiteMock();
        abstractView.install(viewSiteMock);

        verify(abstractView, times(1)).doInstall(viewSiteMock);

        assertEquals(viewSiteMock, abstractView.getViewSite());
        assertNotNull(abstractView.getContextDsl());
    }

    @Test
    void uninstall() {
        install();
        reset(abstractView);

        abstractView.uninstall();

        verify(abstractView, times(1)).doUninstall(viewSiteMock);
    }

    @Test
    void createSubViewSite() {
        install();

        ViewContent content = mock(ViewContent.class);
        ViewSite subViewSite = abstractView.createSubViewSite(content);

        assertInstanceOf(DefaultViewSite.class, subViewSite);
    }
}