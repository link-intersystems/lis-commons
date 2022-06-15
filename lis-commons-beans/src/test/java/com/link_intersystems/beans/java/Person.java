package com.link_intersystems.beans.java;

import java.beans.PropertyChangeListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class Person implements Named {

    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
    }
}
