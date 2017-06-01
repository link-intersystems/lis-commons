package com.link_intersystems.lang.reflect;

import java.awt.Container;

import javax.swing.JComponent;

import org.junit.Before;

import com.link_intersystems.EqualsAndHashCodeTest;

public class Class2EqualsAndHashCodeTest extends EqualsAndHashCodeTest {

	@Before
	public void createTestInstances() throws Exception {
		super.createTestInstances();
	}

	@Override
	protected Object createInstance() throws Exception {
		return new Class2<JComponent>(JComponent.class);
	}

	@Override
	protected Object createNotEqualInstance() throws Exception {
		return new Class2<Container>(Container.class);
	}

}
