package com.link_intersystems.beans;

import java.util.Optional;

public class BeanEventSupport<B, T> {

    private Optional<T> listener = Optional.empty();

    private Optional<Bean<B>> bean = Optional.empty();

    private boolean eventDisabled;

    public boolean isEventDisabled() {
        return eventDisabled;
    }

    public void setEventDisabled(boolean eventDisabled) {
        if (eventDisabled) {
            bean.ifPresent(b -> listener.ifPresent(l -> removeListener(b, l)));
        } else {
            bean.ifPresent(b -> listener.ifPresent(l -> addListener(b, l)));
        }

        this.eventDisabled = eventDisabled;
    }

    public T getListener() {
        return listener.orElse(null);
    }

    public void setListener(T newListener) {
        bean.ifPresent(b -> listener.ifPresent(l -> removeListener(b, l)));

        listener = Optional.ofNullable(newListener);

        if (!eventDisabled) {
            bean.ifPresent(b -> listener.ifPresent(l -> addListener(b, l)));
        }
    }

    private void addListener(Bean<B> actualBean, T actualListener) {
        actualBean.addListener(actualListener);
    }

    private void removeListener(Bean<B> actualBean, T actualListener) {
        actualBean.removeListener(actualListener);
    }

    public B getBean() {
        return bean.map(Bean::getObject).orElse(null);
    }

    public void setBean(Bean<B> newBean) {
        if (bean.orElse(null) == newBean) {
            return;
        }


        bean.ifPresent(b -> listener.ifPresent(l -> removeListener(b, l)));

        bean = Optional.ofNullable(newBean);

        if (!eventDisabled) {
            bean.ifPresent(b -> listener.ifPresent(l -> addListener(b, l)));
        }
    }

}
