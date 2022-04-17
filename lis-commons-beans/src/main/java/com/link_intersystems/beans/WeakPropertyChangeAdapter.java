package com.link_intersystems.beans;

import com.link_intersystems.lang.reflect.Class2;
import com.link_intersystems.lang.reflect.Invokable;
import com.link_intersystems.lang.reflect.Method2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;

public abstract class WeakPropertyChangeAdapter<E> implements PropertyChangeListener {

    private WeakReference<E> weakReferencedElement;

    public WeakPropertyChangeAdapter(E weakReferencedElement) {
        this.weakReferencedElement = new WeakReference<>(weakReferencedElement);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        E element = weakReferencedElement.get();
        if (element == null) {
            Object source = evt.getSource();
            if (source instanceof PropertyChangeSource) {
                PropertyChangeSource propertyChangeSource = (PropertyChangeSource) source;
                propertyChangeSource.removePropertyChangeListener(this);
            } else {
                Class2<?> sourceClass = Class2.get(source.getClass());
                try {
                    Method2 applicableMethod = sourceClass.getApplicableMethod("removePropertyChangeListener",
                            PropertyChangeListener.class);
                    Invokable invokable = applicableMethod.getInvokable(source);
                    invokable.invoke(this);
                } catch (Exception e) {
                    String msg = MessageFormat.format(
                            "Unable to remove property change listener {0} from {1}."
                                    + " Event source should either implement {2} or"
                                    + " declare a removePropertyChangeListener(PropertyChangeListener) method.",
                            this, source, PropertyChangeSource.class);
                    throw new IllegalStateException(msg, e);
                }
            }
            return;
        }

        onPropertyChange(element, evt);
    }

    protected abstract void onPropertyChange(E element, PropertyChangeEvent evt);

}
