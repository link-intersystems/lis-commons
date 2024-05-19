package com.link_intersystems.beans;

import java.util.Optional;

public class BeanEventSupport<T, L> {

    private Optional<L> listener = Optional.empty();

    private Optional<AbstractBean<? extends T>> bean = Optional.empty();

    private boolean eventEnabled = true;

    public boolean isEventEnabled() {
        return eventEnabled;
    }

    public BeanEventSupport() {
    }

    public BeanEventSupport(AbstractBean<? extends T> bean) {
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
        return bean.map(AbstractBean::getBeanObject).orElse(null);
    }

    public void setBean(AbstractBean<? extends T> newBean) {
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
