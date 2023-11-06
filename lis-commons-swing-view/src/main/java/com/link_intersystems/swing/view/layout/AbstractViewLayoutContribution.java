package com.link_intersystems.swing.view.layout;

import com.link_intersystems.swing.view.View;

import java.util.*;

/**
 * A {@link ViewLayoutContribution} that keeps track of all installed views and therefore
 * doesn't need an implementor to handle {@link #uninstall(ViewLayout)}.
 */
public abstract class AbstractViewLayoutContribution implements ViewLayoutContribution {

    private static class ViewInstallation {
        private ViewLayout viewLayout;
        private String viewSiteName;

        public ViewInstallation(ViewLayout viewLayout, String viewSiteName) {
            this.viewLayout = viewLayout;
            this.viewSiteName = viewSiteName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ViewInstallation that = (ViewInstallation) o;
            return Objects.equals(viewLayout, that.viewLayout) && Objects.equals(viewSiteName, that.viewSiteName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(viewLayout, viewSiteName);
        }
    }

    private String viewLayoutId = ViewLayout.MAIN_ID;

    private Map<ViewInstallation, View> installedViews = new HashMap<>();

    @Override
    public String getViewLayoutId() {
        return viewLayoutId;
    }

    @Override
    public final void install(ViewLayout viewLayout) {
        if (!getViewLayoutId().equals(viewLayout.getId())) {
            throw new IllegalArgumentException("Can not install " + this + " in " + viewLayout);
        }

        doInstall(new ViewLayout() {

            @Override
            public String getId() {
                return viewLayout.getId();
            }

            @Override
            public void install(String viewSiteName, View view) {
                viewLayout.install(viewSiteName, view);
                installedViews.put(new ViewInstallation(viewLayout, viewSiteName), view);
            }

            @Override
            public void remove(String viewSiteName) {
                viewLayout.remove(viewSiteName);
                installedViews.remove(new ViewInstallation(viewLayout, viewSiteName));
            }
        });
    }

    protected abstract void doInstall(ViewLayout viewLayout);

    @Override
    public final void uninstall(ViewLayout viewLayout) {
        Set<Map.Entry<ViewInstallation, View>> installedViewEntries = installedViews.entrySet();

        for (Map.Entry<ViewInstallation, View> installedViewEntry : installedViewEntries) {
            ViewInstallation viewInstallation = installedViewEntry.getKey();
            viewInstallation.viewLayout.remove(viewInstallation.viewSiteName);
        }
    }

    @Override
    public String toString() {
        return "AbstractViewLayoutContribution{" + "viewLayoutId='" + viewLayoutId + '\'' + '}';
    }
}
