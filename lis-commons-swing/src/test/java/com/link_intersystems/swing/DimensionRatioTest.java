package com.link_intersystems.swing;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DimensionRatioTest {

    @Test
    void newDimensionRatioFromDimension() {
        Dimension dimension = new Dimension(640, 480);
        DimensionRatio ratio = new DimensionRatio(dimension);
        dimension.width = 2;

        assertEquals(4, ratio.getWidthValue());
        assertEquals(3, ratio.getHeightValue());
    }

    @Test
    void newDimentionRatioFromValue() {
        DimensionRatio ratio = new DimensionRatio(400, 240);

        assertEquals(5, ratio.getWidthValue());
        assertEquals(3, ratio.getHeightValue());
    }

    @Test
    void wqhd() {
        DimensionRatio ratio = new DimensionRatio(2560, 1440);

        assertEquals(16, ratio.getWidthValue());
        assertEquals(9, ratio.getHeightValue());
    }

    @Test
    void wrongWidth() {
        assertThrows(IllegalArgumentException.class, () -> new DimensionRatio(0, 480));
    }

    @Test
    void wrongHeight() {
        assertThrows(IllegalArgumentException.class, () -> new DimensionRatio(640, 0));
    }

    @Test
    void asDimension() {
        Dimension dimension = new Dimension(2560, 1440);
        DimensionRatio ratio = new DimensionRatio(dimension);

        assertEquals(new Dimension(16, 9), ratio.asDimension());
    }


    @Test
    void toStringTest() {
        DimensionRatio ratio = new DimensionRatio(2560, 1440);

        assertEquals("16:9", ratio.toString());
    }
}