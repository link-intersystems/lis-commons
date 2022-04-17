package com.link_intersystems.beans.java;

import java.beans.PropertyEditor;

public interface SharedPropertyEditor extends PropertyEditor {

    public void release();

}
