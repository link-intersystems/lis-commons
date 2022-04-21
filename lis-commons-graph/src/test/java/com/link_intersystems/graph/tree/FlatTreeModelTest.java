package com.link_intersystems.graph.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FlatTreeModelTest {

    private TreeModelFixture fixture;

    @BeforeEach
    public void setUp() {
        fixture = new TreeModelFixture();
    }

    @Test
    void getChildren() {
        FlatTreeModel<Object> objectFlatTreeModel = new FlatTreeModel<>(new IterableTreeModel<>());

        Stream<?> children = objectFlatTreeModel.getChildren(fixture.forest);
        List<?> childrenList = children.collect(Collectors.toList());

        List<TreeModelFixture.NamedElement> expectedOrder = asList(
                fixture.leaf1,
                fixture.leaf2,
                fixture.leaf3,
                fixture.tree2,
                fixture.leaf4,
                fixture.branch4,
                fixture.leaf5);
        Assertions.assertEquals(expectedOrder, childrenList);
    }

    @Test
    void noChildren() {
        FlatTreeModel<Object> objectFlatTreeModel = new FlatTreeModel<>(new IterableTreeModel<>());

        Stream<?> children = objectFlatTreeModel.getChildren("");
        List<?> childrenList = children.collect(Collectors.toList());

        Assertions.assertEquals(Collections.emptyList(), childrenList);
    }
}