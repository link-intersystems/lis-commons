package com.link_intersystems.beans;

import java.util.Optional;

public class BeanEventSupport<B, T> {

    private Optional<T> listener = Optional.empty();

    private Optional<Bean<B>> bean = Optional.empty();

    private boolean eventEnabled = true;

    public boolean isEventEnabled() {
        return eventEnabled;
    }

    public void setEventEnabled(boolean enabled) {
        if (enabled) {
            bean.ifPresent(b -> listener.ifPresent(b::addListener));
        } else {
            bean.ifPresent(b -> listener.ifPresent(b::removeListener));
        }

        this.eventEnabled = enabled;
    }

    public T getListener() {
        return listener.orElse(null);
    }

    public void setListener(T newListener) {
        bean.ifPresent(b -> listener.ifPresent(b::removeListener));

        listener = Optional.ofNullable(newListener);

        if (eventEnabled) {
            bean.ifPresent(b -> listener.ifPresent(b::addListener));
        }
    }

    public B getBean() {
        return bean.map(Bean::getBeanObject).orElse(null);
    }

    public void setBean(Bean<B> newBean) {
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
