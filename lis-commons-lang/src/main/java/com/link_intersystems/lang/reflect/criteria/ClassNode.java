package com.link_intersystems.lang.reflect.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.link_intersystems.lang.Assert;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import com.link_intersystems.util.graph.Node;

/**
 * A {@link Node} adaption to a class hierarchy.
 * 
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 * @since 1.2.0;
 * 
 */
class ClassNode implements Node {

	private Class<?> clazz;
	private ClassType[] classTypes;
	private Comparator<Class<?>> interfacesComparator;
	private Comparator<Class<?>> innerClassesComparator;

	/**
	 * Constructs a {@link ClassNode} that returns it's referenced classes
	 * (super class, inner classes and interfaces) in the classTypes order.
	 * 
	 * @param clazz
	 * @param classTypes
	 *            the order in which the referenced classes are returned by
	 *            {@link #getReferences()}. The classTypes must be unique.
	 */
	public ClassNode(Class<?> clazz, ClassType... classTypes) {
		Assert.notNull("clazz", clazz);
		Assert.notNull("classTypes", classTypes);
		Set<ClassType> uniqueClassTypes = new HashSet<ClassType>(
				Arrays.asList(classTypes));
		if (uniqueClassTypes.size() != classTypes.length) {
			throw new IllegalArgumentException("classTypes must be unique: "
					+ Arrays.toString(classTypes));
		}
		this.classTypes = classTypes;
		this.clazz = clazz;
	}

	/**
	 * 
	 * @inherited <br/>
	 * @return the referenced classes of this {@link ClassNode}'s {@link Class}
	 *         in the order the {@link ClassType}s were specified in the
	 *         constructor {@link ClassNode#ClassNode(Class, ClassType...)}.
	 */
	public Collection<Node> getReferences() {
		Collection<Node> subNodes = new ArrayList<Node>();
		List<ClassType> remainingTypes = new ArrayList<ClassType>(
				Arrays.asList(ClassType.values()));
		for (int i = 0; i < classTypes.length; i++) {
			ClassType classType = classTypes[i];
			remainingTypes.remove(classType);
			switch (classType) {
			case INNER_CLASSES:
				Class<?>[] classes = clazz.getClasses();
				if (innerClassesComparator != null) {
					Arrays.sort(classes, innerClassesComparator);
				}
				Collection<Node> innerClassNodes = toNodes(classes);
				subNodes.addAll(innerClassNodes);
				break;
			case INTERFACES:
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfacesComparator != null) {
					Arrays.sort(interfaces, interfacesComparator);
				}
				Collection<Node> interfaceNodes = toNodes(interfaces);
				subNodes.addAll(interfaceNodes);
				break;
			case CLASSES:
				Class<?> superclass = clazz.getSuperclass();
				if (superclass == null) {
					continue;
				}
				ClassNode superclassNode = newClassNode(superclass);
				subNodes.add(superclassNode);
				break;
			}
		}

		for (ClassType classType : remainingTypes) {
			switch (classType) {
			case INNER_CLASSES:
				Class<?>[] classes = clazz.getClasses();
				if (innerClassesComparator != null) {
					Arrays.sort(classes, innerClassesComparator);
				}
				Collection<Node> innerClassNodes = toNodes(classes);
				subNodes.addAll(innerClassNodes);
				break;
			case INTERFACES:
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfacesComparator != null) {
					Arrays.sort(interfaces, interfacesComparator);
				}
				Collection<Node> interfaceNodes = toNodes(interfaces);
				subNodes.addAll(interfaceNodes);
				break;
			case CLASSES:
				Class<?> superclass = clazz.getSuperclass();
				if (superclass == null) {
					continue;
				}
				ClassNode superclassNode = newClassNode(superclass);
				subNodes.add(superclassNode);
				break;
			}
		}

		return subNodes;
	}

	public void setInterfacesOrder(Comparator<Class<?>> interfacesComparator) {
		this.interfacesComparator = interfacesComparator;
	}

	public void setInnerClassesOrder(Comparator<Class<?>> innerClassesComparator) {
		this.innerClassesComparator = innerClassesComparator;
	}

	private ClassNode newClassNode(Class<?> clazz) {
		ClassNode classNode = new ClassNode(clazz, classTypes);
		classNode.setInterfacesOrder(interfacesComparator);
		classNode.setInnerClassesOrder(innerClassesComparator);
		return classNode;
	}

	private Collection<Node> toNodes(Class<?>[] classes) {
		Collection<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < classes.length; i++) {
			Class<?> clazz = classes[i];
			ClassNode classNode = newClassNode(clazz);
			nodes.add(classNode);
		}
		return nodes;
	}

	public Object getUserObject() {
		return clazz;
	}

	public void addReference(Node node) {
	}

}
