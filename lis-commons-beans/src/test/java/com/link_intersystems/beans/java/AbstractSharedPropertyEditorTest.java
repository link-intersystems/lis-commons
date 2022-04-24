package com.link_intersystems.beans.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyEditor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class AbstractSharedPropertyEditorTest {

    private PropertyEditor propertyEditor;
    private AbstractSharedPropertyEditor sharedPropertyEditor;

    @BeforeEach
    void setUp() {
        propertyEditor = Mockito.mock(PropertyEditor.class);
        sharedPropertyEditor = new AbstractSharedPropertyEditor(propertyEditor) {
        };
    }

    @Test
    void getValue() {
        sharedPropertyEditor.getValue();

        verify(propertyEditor, times(1)).getValue();
    }

    @Test
    void setValue() {
        sharedPropertyEditor.setValue("");

        verify(propertyEditor, times(1)).setValue("");
    }

    @Test
    void isPaintable() {
        sharedPropertyEditor.isPaintable();

        verify(propertyEditor, times(1)).isPaintable();
    }

    @Test
    void paintValue() {
        sharedPropertyEditor.paintValue(null, null);

        verify(propertyEditor, times(1)).paintValue(null, null);
    }

    @Test
    void getJavaInitializationString() {
        sharedPropertyEditor.getJavaInitializationString();

        verify(propertyEditor, times(1)).getJavaInitializationString();
    }

    @Test
    void getAsText() {
        sharedPropertyEditor.getAsText();

        verify(propertyEditor, times(1)).getAsText();
    }

    @Test
    void setAsText() {
        sharedPropertyEditor.setAsText("");

        verify(propertyEditor, times(1)).setAsText("");
    }

    @Test
    void getTags() {
        sharedPropertyEditor.getTags();

        verify(propertyEditor, times(1)).getTags();
    }

    @Test
    void getCustomEditor() {
        sharedPropertyEditor.getCustomEditor();

        verify(propertyEditor, times(1)).getCustomEditor();
    }

    @Test
    void supportsCustomEditor() {
        sharedPropertyEditor.supportsCustomEditor();

        verify(propertyEditor, times(1)).supportsCustomEditor();
    }

    @Test
    void addPropertyChangeListener() {
        sharedPropertyEditor.addPropertyChangeListener(null);

        verify(propertyEditor, times(1)).addPropertyChangeListener(any());
    }

    @Test
    void removePropertyChangeListener() {
        sharedPropertyEditor.removePropertyChangeListener(null);
        verify(propertyEditor, times(0)).removePropertyChangeListener(any());
    }

    @Test
    void release() {
        sharedPropertyEditor.release();


        verify(propertyEditor, times(1)).removePropertyChangeListener(any());
    }

    @Test
    void releaseListeners() {
        sharedPropertyEditor.releaseListeners();


        verify(propertyEditor, times(0)).removePropertyChangeListener(any());
    }
}