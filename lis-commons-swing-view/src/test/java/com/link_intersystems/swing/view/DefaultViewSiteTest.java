package com.link_intersystems.swing.view;

import com.link_intersystems.util.context.Context;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultViewSiteTest {

    @Test
    void contentAndContext() {
        ViewContent viewContent = mock(ViewContent.class);
        Context context = mock(Context.class);
        DefaultViewSite defaultViewSite = new DefaultViewSite(viewContent, context);

        assertEquals(viewContent, defaultViewSite.getViewContent());
        assertEquals(context, defaultViewSite.getViewContext());
    }

    @Test
    void contentOnly() {
        ViewContent viewContent = mock(ViewContent.class);

        DefaultViewSite defaultViewSite = new DefaultViewSite(viewContent);

        assertEquals(viewContent, defaultViewSite.getViewContent());
        assertNotNull(defaultViewSite.getViewContext());
    }

    @Test
    void contextOnly() {
        Context context = mock(Context.class);

        DefaultViewSite defaultViewSite = new DefaultViewSite(context);

        assertEquals(context, defaultViewSite.getViewContext());
        assertNotNull(defaultViewSite.getViewContent());
    }
}