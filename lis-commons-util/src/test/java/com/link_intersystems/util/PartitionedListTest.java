package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class PartitionedListTest {

    @Test
    void wrongPartitionSize() {
        assertThrows(IllegalArgumentException.class, () -> new PartitionedList<>(asList(1, 2), 0));
    }

    @Test
    void emptyPartition() {
        PartitionedList<Object> emptyPartitionedList = new PartitionedList<>(asList(), 1);

        assertEquals(0, emptyPartitionedList.size());
    }

    @Test
    void indexOutOfBounds() {
        PartitionedList<Object> emptyPartitionedList = new PartitionedList<>(asList(), 1);

        assertThrows(IndexOutOfBoundsException.class, () -> emptyPartitionedList.get(0));
    }


    @Test
    void partitioned() {
        PartitionedList<Integer> partitionedList = new PartitionedList<>(asList(1, 2, 3, 4, 5, 6, 7, 8), 3);

        assertEquals(3, partitionedList.size());

        assertEquals(asList(1, 2, 3), partitionedList.get(0));
        assertEquals(asList(4, 5, 6), partitionedList.get(1));
        assertEquals(asList(7, 8), partitionedList.get(2));
    }

    @Test
    void iterateWhileBaseListChanges() {

        List<Integer> baseList = new ArrayList<>(asList(1, 2, 3, 4, 5, 6, 7, 8));


        PartitionedList<Integer> partitionedList = new PartitionedList<>(baseList, 3);
        Iterator<List<Integer>> iterator = partitionedList.iterator();

        assertEquals(asList(1, 2, 3), iterator.next());
        baseList.add(2, 9);
        assertEquals(asList(3, 4, 5), iterator.next());
        baseList.add(2, 9);
        assertEquals(asList(5, 6, 7), iterator.next());
        baseList.add(9);
        assertEquals(asList(8, 9), iterator.next());
    }

}