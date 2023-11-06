package com.link_intersystems.swing.view.menu;

import com.link_intersystems.util.context.Context;

import javax.swing.*;

public interface MenuContribution {
    String getMenuPath();

    Action getAction(Context context);
}
