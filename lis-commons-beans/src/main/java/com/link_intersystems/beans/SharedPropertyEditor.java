package com.link_intersystems.beans;

import java.beans.PropertyEditor;

public interface SharedPropertyEditor extends PropertyEditor {

	public void release();

}
