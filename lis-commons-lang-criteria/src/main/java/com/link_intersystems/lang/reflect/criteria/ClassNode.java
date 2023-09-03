package com.link_intersystems.lang.reflect.criteria;

import com.link_intersystems.graph.Node;
import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * A {@link Node} adaption to a class hierarchy.
 *
 * @author René Link <a
 * href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 * intersystems.com]</a>
 * @since 1.2.0;
 */
class ClassNode implements Node {

    private Class<?> clazz;
    private ClassType[] classTypes;
    private Map<ClassType, Comparator<Class<?>>> classesComparators = new HashMap<>();

    /**
     * Constructs a {@link ClassNode} that returns it's referenced classes
     * (super class, inner classes and interfaces) in the classTypes order.
     *
     * @param classTypes the order in which the referenced classes are returned by
     *                   {@link #getReferences()}. The classTypes must be unique.
     */
    public ClassNode(Class<?> clazz, ClassType... classTypes) {
        requireNonNull(clazz);
        requireNonNull(classTypes);
        Set<ClassType> uniqueClassTypes = new HashSet<>(asList(classTypes));
        if (uniqueClassTypes.size() != classTypes.length) {
            throw new IllegalArgumentException("classTypes must be unique: "
                    + Arrays.toString(classTypes));
        }
        this.classTypes = classTypes;
        this.clazz = clazz;
    }

    /**
     * @return the referenced classes of this {@link ClassNode}'s {@link Class}
     * in the order the {@link ClassType}s were specified in the
     * constructor {@link ClassNode#ClassNode(Class, ClassType...)}.
     */
    public Collection<Node> getReferences() {
        Collection<Node> subNodes = new ArrayList<>();
        List<ClassType> remainingTypes = new ArrayList<>(asList(ClassType.values()));
        for (int i = 0; i < classTypes.length; i++) {
            ClassType classType = classTypes[i];
            remainingTypes.remove(classType);

            Collection<Node> classNodes = getClassNodes(classType);
            subNodes.addAll(classNodes);
        }

        for (ClassType classType : remainingTypes) {
            Collection<Node> classNodes = getClassNodes(classType);
            subNodes.addAll(classNodes);
        }

        return subNodes;
    }

    private Collection<Node> getClassNodes(ClassType classType) {
        Class<?>[] classes = classType.getClasses(clazz);
        Comparator<Class<?>> classComparator = classesComparators.get(classType);
        if (classComparator != null) {
            Arrays.sort(classes, classComparator);
        }
        Collection<Node> innerClassNodes = toNodes(classes);
        return innerClassNodes;
    }

    public void setInterfacesOrder(Comparator<Class<?>> interfacesComparator) {
        this.classesComparators.put(ClassType.INTERFACES, interfacesComparator);
    }

    public void setInnerClassesOrder(Comparator<Class<?>> innerClassesComparator) {
        this.classesComparators.put(ClassType.INNER_CLASSES, innerClassesComparator);
    }

    private ClassNode newClassNode(Class<?> clazz) {
        ClassNode classNode = new ClassNode(clazz, classTypes);

        Comparator<Class<?>> interfacesComparator = classesComparators.get(ClassType.INTERFACES);
        classNode.setInterfacesOrder(interfacesComparator);

        Comparator<Class<?>> innerClassesComparator = classesComparators.get(ClassType.INNER_CLASSES);
        classNode.setInnerClassesOrder(innerClassesComparator);
        return classNode;
    }

    private Collection<Node> toNodes(Class<?>[] classes) {
        Collection<Node> nodes = new ArrayList<>();
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
