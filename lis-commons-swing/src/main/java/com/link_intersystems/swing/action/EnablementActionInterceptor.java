package com.link_intersystems.swing.action;

import javax.swing.*;

public class EnablementActionInterceptor implements ActionInterceptor {


    @Override
    public void execute(ActionJoinPoint ajp) {
        Action action = ajp.getAction();
        if (!action.isEnabled()) {
            return;
        }

        action.setEnabled(false);
        Runnable enableAction = () -> action.setEnabled(true);
        ajp.proceed(enableAction);
    }
}
