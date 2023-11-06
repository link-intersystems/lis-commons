package com.link_intersystems.swing.view.layout;

import com.link_intersystems.swing.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractViewLayoutContributionTest {

    private AbstractViewLayoutContribution viewLayoutContribution;
    private ViewLayout viewLayout;
    private View view;

    @BeforeEach
    void setUp() {
        viewLayoutContribution = spy(AbstractViewLayoutContribution.class);

        viewLayout = spy(ViewLayout.class);
        doReturn(ViewLayout.MAIN_ID).when(viewLayout).getId();

        view = mock(View.class);
    }

    @Test
    void contributeToWrongViewLayout() {
        when(viewLayout.getId()).thenReturn("UNKNOWN");

        assertThrows(IllegalArgumentException.class, () -> viewLayoutContribution.install(viewLayout));
    }

    @Test
    void install() {
        doAnswer((Answer<Void>) invocation -> {
            ViewLayout layout = invocation.getArgument(0, ViewLayout.class);
            assertEquals(ViewLayout.MAIN_ID, layout.getId());
            layout.install("A", view);
            return null;
        }).when(viewLayoutContribution).doInstall(Mockito.any(ViewLayout.class));

        viewLayoutContribution.install(viewLayout);

        verify(viewLayout, times(1)).install("A", view);
    }

    @Test
    void installMultipleView() {
        View view2 = mock(View.class);

        doAnswer((Answer<Void>) invocation -> {
            ViewLayout layout = invocation.getArgument(0, ViewLayout.class);
            layout.install("A", view);
            layout.install("B", view2);
            return null;
        }).when(viewLayoutContribution).doInstall(Mockito.any(ViewLayout.class));

        viewLayoutContribution.install(viewLayout);

        verify(viewLayout, times(1)).install("A", view);
        verify(viewLayout, times(1)).install("B", view2);
    }

    @Test
    void removePreviousViewOnInstall() {
        install();

        doAnswer((Answer<Void>) invocation -> {
            ViewLayout layout = invocation.getArgument(0, ViewLayout.class);
            layout.remove("A");
            layout.install("B", view);
            return null;
        }).when(viewLayoutContribution).doInstall(Mockito.any(ViewLayout.class));

        viewLayoutContribution.install(viewLayout);

        verify(viewLayout, times(1)).install("B", view);
    }

    @Test
    void uninstall() {
        install();

        viewLayoutContribution.uninstall(viewLayout);

        verify(viewLayout, times(1)).remove("A");
    }

    @Test
    void testToString() {

        assertNotNull(viewLayoutContribution.toString());
    }
}