package com.link_intersystems.lang.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class ArrayTypeTest {

    @Test
    void primitiveArrayType() {
        ArrayType<Integer> integerArrayType = new ArrayType<>(int.class);

        assertEquals(int[].class, integerArrayType.getType());
    }

    @Test
    void objectArrayType() {
        ArrayType<String> stringArrayType = new ArrayType<>(String.class);

        assertEquals(String[].class, stringArrayType.getType());
    }

    @Test
    void newInstance() {
        ArrayType<String> stringArrayType = new ArrayType<>(String.class);

        String[] strings = stringArrayType.newInstance(10);

        Assertions.assertEquals(10, strings.length);

        Arrays.fill(strings, "T");
    }

    @Test
    void getNextDimension() {
        ArrayType<Object> objectArrayType = new ArrayType<>(Object.class);
        ArrayType<Object[]> threeDimensional = objectArrayType.getNextDimension();
        assertEquals(Object[][].class, threeDimensional.getType());
    }
}