package com.link_intersystems.graph.tree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class TransformedIterableTreeModelTest {

    @Test
    void getChildren() {
        TransformedIterableTreeModel<Object> treeModel = new TransformedIterableTreeModel<>(s -> toChars(String.valueOf(s)));

        Stream<?> childrenStream = treeModel.getChildren("test");

        List<?> children = childrenStream.collect(Collectors.toList());

        assertEquals(4, children.size());
        assertEquals("t", children.get(0));
        assertEquals("e", children.get(1));
        assertEquals("s", children.get(2));
        assertEquals("t", children.get(3));
    }

    private List<String> toChars(String s) {
        List<String> characters = new ArrayList<>();
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            characters.add(Character.toString(aChar));
        }

        return characters;
    }
}