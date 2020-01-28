package com.link_intersystems.beans;

import java.beans.VetoableChangeListener;

public interface VetoablePropertyChangeSource {

	void addVetoableChangeListener(VetoableChangeListener listener);

	void removeVetoableChangeListener(VetoableChangeListener listener);

	void addVetoableChangeListener(String propertyName, VetoableChangeListener listener);

	void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener);

}
