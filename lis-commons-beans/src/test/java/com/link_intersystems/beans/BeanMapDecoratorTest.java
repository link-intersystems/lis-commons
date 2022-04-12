package com.link_intersystems.beans;

import com.link_intersystems.beans.BeanMapDecorator.IndexedValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class BeanMapDecoratorTest {

    private BeanMapDecorator beanMapDecorator;
    private Container container;
    private JLabel label;

    @BeforeEach
    public void setupBeanMap() {
        container = new Container();
        label = new JLabel("test");
        container.add(label);

        container.setBackground(Color.BLUE);
        beanMapDecorator = new BeanMapDecorator(container);
    }

    @Test
    public void containsKey() {
        assertFalse(beanMapDecorator.containsKey(null));
        assertFalse(beanMapDecorator.containsKey("unknownProperty"));
        assertTrue(beanMapDecorator.containsKey("background"));
    }

    @Test
    public void isEmpty() {
        assertFalse(beanMapDecorator.isEmpty());
    }

    @Test
    public void size() {
        int size = beanMapDecorator.size();
        assertEquals(24, size);
    }

    @Test
    public void getNull() {
        Object value = beanMapDecorator.get(null);
        Assertions.assertNull(value);
    }

    @Test
    public void get() {
        Object background = beanMapDecorator.get("background");
        assertEquals(Color.BLUE, background);
    }

    @Test
    public void getIndexValue() {
        IndexedValue indexedValue = (IndexedValue) beanMapDecorator.get("component");
        assertNotNull(indexedValue);

        Object element = indexedValue.getElement(0);
        assertEquals(label, element);
    }

    @Test
    public void put() {
        beanMapDecorator.put("background", Color.RED);
        assertEquals(Color.RED, container.getBackground());
    }

}
