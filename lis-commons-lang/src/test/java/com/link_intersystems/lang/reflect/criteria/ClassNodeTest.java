package com.link_intersystems.lang.reflect.criteria;

import com.link_intersystems.lang.reflect.criteria.ClassCriteria.ClassType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassNodeTest  {

    @Test
    void nonUniqueClassTypes() {
        assertThrows(IllegalArgumentException.class, () -> new ClassNode(ClassNode.class, ClassType.CLASSES, ClassType.INTERFACES, ClassType.INNER_CLASSES, ClassType.INTERFACES));
    }
}
