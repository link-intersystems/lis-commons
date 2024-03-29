package com.link_intersystems.lang.reflect;

import java.awt.Container;

import javax.swing.JComponent;

import com.link_intersystems.test.EqualsAndHashCodeTest;
import org.junit.jupiter.api.BeforeEach;

class Class2EqualsAndHashCodeTest extends EqualsAndHashCodeTest {

	@BeforeEach
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
