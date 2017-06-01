package com.link_intersystems.beans;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.link_intersystems.beans.BeanMapDecorator.IndexedValue;

public class BeanMapDecoratorTest {

	private BeanMapDecorator beanMapDecorator;
	private Container container;
	private JLabel label;

	@Before
	public void setupBeanMap() {
		container = new Container();
		label = new JLabel("test");
		container.add(label);

		container.setBackground(Color.BLUE);
		beanMapDecorator = new BeanMapDecorator(container);
	}

	@Test
	public void containsKey() {
		Assert.assertFalse(beanMapDecorator.containsKey(null));
		Assert.assertFalse(beanMapDecorator.containsKey("unknownProperty"));
		Assert.assertTrue(beanMapDecorator.containsKey("background"));
	}

	@Test
	public void isEmpty() {
		Assert.assertFalse(beanMapDecorator.isEmpty());
	}

	@Test
	public void size() {
		int size = beanMapDecorator.size();
		Assert.assertEquals(24, size);
	}

	@Test
	public void getNull() {
		Object value = beanMapDecorator.get(null);
		Assert.assertNull(value);
	}

	@Test
	public void get() {
		Object background = beanMapDecorator.get("background");
		Assert.assertEquals(Color.BLUE, background);
	}

	@Test
	public void getIndexValue() {
		IndexedValue indexedValue = (IndexedValue) beanMapDecorator.get("component");
		Assert.assertNotNull(indexedValue);

		Object element = indexedValue.getElement(0);
		Assert.assertEquals(label, element);
	}

	@Test
	public void put() {
		beanMapDecorator.put("background", Color.RED);
		Assert.assertEquals(Color.RED, container.getBackground());
	}

}
