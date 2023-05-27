package com.link_intersystems.swing.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

import static java.util.Objects.*;

public class ActionDecorator extends AbstractAction {

    public enum EnablementStrategy {
        DECORATOR, DECORATED;
    }

    private Optional<Action> decoratedAction = Optional.empty();
    private EnablementStrategy enablementStrategy = EnablementStrategy.DECORATOR;

    public ActionDecorator() {
    }

    public ActionDecorator(Action decoratedAction) {
        setDecoratedAction(decoratedAction);
    }

    public void setDecoratedAction(Action decoratedAction) {
        this.decoratedAction = Optional.ofNullable(decoratedAction);
    }

    public void setEnablementStrategy(EnablementStrategy enablementStrategy) {
        this.enablementStrategy = requireNonNull(enablementStrategy);
    }

    @Override
    public void setEnabled(boolean newValue) {
        switch (enablementStrategy) {
            case DECORATOR:
                super.setEnabled(newValue);
                break;
            case DECORATED:
                decoratedAction.ifPresent(a -> a.setEnabled(newValue));
                break;
        }
    }

    @Override
    public boolean isEnabled() {
        switch (enablementStrategy) {
            case DECORATOR:
                return super.isEnabled();
            case DECORATED:
                return decoratedAction.map(a -> a.isEnabled()).orElse(false);
        }

        return false;
    }

    @Override
    public Object getValue(String key) {
        Object[] keys = super.getKeys();
        if (keys != null && Arrays.asList(keys).contains(key)) {
            return super.getValue(key);
        } else {
            return decoratedAction.map(a -> a.getValue(key)).orElse(null);
        }
    }

    @Override
    public Object[] getKeys() {
        Object[] thisKeys = super.getKeys();
        Object[] decoratedActionKeys = decoratedAction.map(a -> {
            ActionKeys actionKeys;
            if (a instanceof AbstractAction) {
                AbstractAction abstractDecoratedAction = (AbstractAction) a;
                actionKeys = new AbstractActionActionKeys(abstractDecoratedAction);
            } else {
                actionKeys = new GeneralActionActionKeys(a);
            }
            return actionKeys.getKeys();
        }).orElse(null);


        if (thisKeys == null) {
            return decoratedActionKeys;
        }

        if (decoratedActionKeys == null) {
            return thisKeys;
        }

        return mergeKeys(thisKeys, decoratedActionKeys);
    }

    private Object[] mergeKeys(Object[] thisKeys, Object[] decoratedActionKeys) {
        LinkedHashSet<Object> mergedKeys = new LinkedHashSet<>(Arrays.asList(thisKeys));
        mergedKeys.addAll(new LinkedHashSet<>(Arrays.asList(decoratedActionKeys)));
        return mergedKeys.toArray();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isEnabled() && decoratedAction != null) {
            decoratedAction.ifPresent(a -> a.actionPerformed(e));
        }
    }


    private static interface ActionKeys {

        public Object[] getKeys();
    }

    private static class AbstractActionActionKeys implements ActionKeys {

        private AbstractAction abstractAction;

        public AbstractActionActionKeys(AbstractAction abstractAction) {
            this.abstractAction = abstractAction;
        }

        @Override
        public Object[] getKeys() {
            return abstractAction.getKeys();
        }
    }

    private static class GeneralActionActionKeys implements ActionKeys {

        private Action action;

        public GeneralActionActionKeys(Action action) {
            this.action = action;
        }

        @Override
        public Object[] getKeys() {
            List<Object> keys = new ArrayList<>();

            addValue(keys, ACCELERATOR_KEY);
            addValue(keys, ACTION_COMMAND_KEY);
            addValue(keys, DEFAULT);
            addValue(keys, DISPLAYED_MNEMONIC_INDEX_KEY);
            addValue(keys, LARGE_ICON_KEY);
            addValue(keys, LONG_DESCRIPTION);
            addValue(keys, MNEMONIC_KEY);
            addValue(keys, NAME);
            addValue(keys, SELECTED_KEY);
            addValue(keys, SHORT_DESCRIPTION);
            addValue(keys, SMALL_ICON);

            return keys.toArray();
        }

        private void addValue(List<Object> keys, String propertyName) {
            Object value = action.getValue(propertyName);

            if (value == null) {
                return;
            }

            keys.add(propertyName);
        }
    }
}
