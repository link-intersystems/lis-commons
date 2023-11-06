package com.link_intersystems.swing.view.layout;


public interface ViewLayoutContribution {

    default public String getViewLayoutId() {
        return ViewLayout.MAIN_ID;
    }

    void install(ViewLayout viewLayout);

    void uninstall(ViewLayout viewLayout);
}
