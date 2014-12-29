package com.link_intersystems.lang.reflect.criteria;

import org.junit.Test;

import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;

public class ClassNodeTest {

	@Test(expected = IllegalArgumentException.class)
	public void nonUniqueClassTypes() {
		new ClassNode(ClassNode.class, ClassType.CLASSES, ClassType.INTERFACES,
				ClassType.INNER_CLASSES, ClassType.INTERFACES);
	}
}
