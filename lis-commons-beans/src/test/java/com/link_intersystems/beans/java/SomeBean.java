/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.beans.java;

import javax.swing.event.EventListenerList;
import java.beans.*;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class SomeBean {

    public static final List<String> PROPERTY_NAMES;

    static {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class, Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            PROPERTY_NAMES = unmodifiableList(
                    stream(propertyDescriptors)
                            .map(PropertyDescriptor::getName)
                            .collect(toList()));
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private EventListenerList listeners = new EventListenerList();

    @SuppressWarnings("unused")
    private String writeOnlyProperty;

    private String readOnlyProperty;

    private String stringProperty;

    private String[] writeOnlyIndexedProperty = new String[0];

    private String[] readOnlyIndexedProperty = new String[0];

    private String[] arrayPropertyNoIndexAccess = new String[0];

    private String[] indexedPropertyWriteOnlyIndexOnlyAccess = new String[0];

    private String[] indexedPropertyReadOnlyIndexOnlyAccess = new String[2];

    private String[] stringArrayProperty = new String[0];

    private String[] indexedOnlyProperty = new String[10];

    public static EventSetDescriptor getPropertyChangeEventDescriptor() {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(SomeBean.class);
            return stream(beanInfo.getEventSetDescriptors())
                    .filter(ed -> ed.getName().equals("propertyChange"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);

        }
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        firePropertyChanged("stringProperty", this.stringProperty, this.stringProperty = stringProperty);
    }

    public String[] getStringArrayProperty() {
        return stringArrayProperty;
    }

    public void setStringArrayProperty(String[] stringArray) {
        this.stringArrayProperty = stringArray.clone();
    }

    public String getStringArrayProperty(int index) {
        return stringArrayProperty[index];
    }

    public void setStringArrayProperty(int index, String stringValue) {
        this.stringArrayProperty[index] = stringValue;
    }

    public String getReadOnlyProperty() {
        return readOnlyProperty;
    }

    public void setWriteOnlyProperty(String writeOnlyProperty) {
        firePropertyChanged("writeOnlyProperty", this.writeOnlyProperty, this.writeOnlyProperty = writeOnlyProperty);
    }

    public void setWriteOnlyIndexedProperty(String[] writeOnlyIndexedProperty) {
        this.writeOnlyIndexedProperty = writeOnlyIndexedProperty;
    }

    public void setWriteOnlyIndexedProperty(int index, String value) {
        this.writeOnlyIndexedProperty[index] = value;
    }

    public String getReadOnlyIndexedProperty(int index) {
        return readOnlyIndexedProperty[index];
    }

    public String[] getReadOnlyIndexedProperty() {
        return readOnlyIndexedProperty;
    }

    public String[] getArrayPropertyNoIndexAccess() {
        return arrayPropertyNoIndexAccess;
    }

    public void setArrayPropertyNoIndexAccess(String[] arrayPropertyNoIndexAccess) {
        this.arrayPropertyNoIndexAccess = arrayPropertyNoIndexAccess;
    }

    public void setIndexedPropertyWriteOnlyIndexOnlyAccess(int index, String value) {
        this.indexedPropertyWriteOnlyIndexOnlyAccess[index] = value;
    }

    public String getIndexedPropertyReadOnlyIndexOnlyAccess(int index) {
        return indexedPropertyReadOnlyIndexOnlyAccess[index];
    }

    public void setIndexedOnlyProperty(int index, String value) {
        indexedOnlyProperty[index] = value;
    }

    public String getIndexedOnlyProperty(int index) {
        return indexedOnlyProperty[index];
    }

    protected void setIndexedPropertyReadOnlyIndexOnlyAccess(String[] indexedPropertyReadOnlyIndexOnlyAccess) {
        firePropertyChanged("indexedPropertyReadOnlyIndexOnlyAccess", this.indexedPropertyReadOnlyIndexOnlyAccess, this.indexedPropertyReadOnlyIndexOnlyAccess = indexedPropertyReadOnlyIndexOnlyAccess);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.listeners.add(PropertyChangeListener.class, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.listeners.remove(PropertyChangeListener.class, l);
    }

    private void firePropertyChanged(String property, Object oldValue, Object newValue) {
        PropertyChangeEvent e = new PropertyChangeEvent(this, property, oldValue, newValue);

        stream(listeners.getListeners(PropertyChangeListener.class)).forEach(l -> l.propertyChange(e));
    }


}
