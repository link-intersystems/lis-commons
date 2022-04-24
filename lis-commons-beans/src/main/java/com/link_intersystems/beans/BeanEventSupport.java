package com.link_intersystems.beans;

import java.util.Optional;

public class BeanEventSupport<T, L> {

    private Optional<L> listener = Optional.empty();

    private Optional<Bean<? extends T>> bean = Optional.empty();

    private boolean eventEnabled = true;

    public boolean isEventEnabled() {
        return eventEnabled;
    }

    public BeanEventSupport() {
    }

    public BeanEventSupport(Bean<? extends T> bean) {
        setBean(bean);
    }

    public void setEventEnabled(boolean enabled) {
        if (enabled) {
            bean.ifPresent(b -> listener.ifPresent(b::addListener));
        } else {
            bean.ifPresent(b -> listener.ifPresent(b::removeListener));
        }

        this.eventEnabled = enabled;
    }

    public L getListener() {
        return listener.orElse(null);
    }

    public void setListener(L newListener) {
        bean.ifPresent(b -> listener.ifPresent(b::removeListener));

        listener = Optional.ofNullable(newListener);

        if (eventEnabled) {
            bean.ifPresent(b -> listener.ifPresent(b::addListener));
        }
    }

    public T getBean() {
        return bean.map(Bean::getBeanObject).orElse(null);
    }

    public void setBean(Bean<? extends T> newBean) {
        if (bean.orElse(null) == newBean) {
            return;
        }

        bean.ifPresent(b -> listener.ifPresent(b::removeListener));

        bean = Optional.ofNullable(newBean);

        if (eventEnabled) {
            bean.ifPresent(b -> listener.ifPresent(b::addListener));
        }
    }

}
