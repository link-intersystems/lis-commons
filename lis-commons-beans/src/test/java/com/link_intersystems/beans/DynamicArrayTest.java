package com.link_intersystems.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private DynamicArray<String> dynamicArray;

    @BeforeEach
    void setUp() {
        dynamicArray = new DynamicArray<>(String.class, 3);
    }

    @Test
    void emptyArray() {
        assertArrayEquals(new String[0], dynamicArray.getArray());
    }

    @Test
    void increasedArray() {
        dynamicArray.set(0, "a");
        dynamicArray.set(1, "b");
        dynamicArray.set(2, "c");
        dynamicArray.set(3, "d");

        assertArrayEquals(new String[]{"a", "b", "c", "d"}, dynamicArray.getArray());
    }

    @Test
    void fittingArray() {
        dynamicArray.set(0, "a");
        dynamicArray.set(1, "b");
        dynamicArray.set(2, "c");

        assertArrayEquals(new String[]{"a", "b", "c"}, dynamicArray.getArray());
    }

    @Test
    void smallerArray() {
        dynamicArray.set(0, "a");
        dynamicArray.set(1, "b");

        assertArrayEquals(new String[]{"a", "b"}, dynamicArray.getArray());
    }

    @Test
    void defaultIncreaseSize() {
        dynamicArray = new DynamicArray<>(String.class);

        for (int i = 0; i < DynamicArray.INCREASE_ARRAY_SIZE * 2; i++) {
            dynamicArray.set(i, "A");
        }

        String[] expected = new String[DynamicArray.INCREASE_ARRAY_SIZE * 2];
        Arrays.fill(expected, "A");

        assertArrayEquals(expected, dynamicArray.getArray());
    }
}