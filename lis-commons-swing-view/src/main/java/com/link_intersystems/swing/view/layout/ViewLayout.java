package com.link_intersystems.swing.view.layout;

import com.link_intersystems.swing.view.View;

public interface ViewLayout {

    public static final String MAIN_ID = "main";

    public String getId();

    void install(String viewSiteName, View view);

    void remove(String viewSiteName);
}
