package com.link_intersystems.swing.scroll;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.*;
import static javax.swing.SwingUtilities.*;

public abstract class ScrollPaneConfigurer {

    private Component viewportView;

    public ScrollPaneConfigurer(Component viewportView) {
        this.viewportView = requireNonNull(viewportView);
    }

    public void configure() {
        Container parent = viewportView.getParent();
        if (parent instanceof JViewport) {
            JViewport port = (JViewport) parent;
            Container gp = port.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || getUnwrappedView(viewport) != viewportView) {
                    return;
                }

                configureScrollPane(scrollPane);
            }
        }
    }

    public void unconfigure() {
        Container parent = SwingUtilities.getUnwrappedParent(viewportView);
        if (parent instanceof JViewport) {
            JViewport port = (JViewport) parent;
            Container gp = port.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || SwingUtilities.getUnwrappedView(viewport) != viewportView) {
                    return;
                }

                unconofigureScrollPane(scrollPane);
            }
        }
    }

    protected abstract void unconofigureScrollPane(JScrollPane scrollPane);

    protected abstract void configureScrollPane(JScrollPane scrollPane);
}
