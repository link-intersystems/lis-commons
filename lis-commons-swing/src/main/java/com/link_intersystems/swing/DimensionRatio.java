package com.link_intersystems.swing;

import java.awt.*;

public class DimensionRatio {

    private Dimension ratio;

    public DimensionRatio(Dimension dimension) {
        this(dimension.width, dimension.height);
    }

    public DimensionRatio(int width, int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width must be greater then 0");
        }
        if (height < 1) {
            throw new IllegalArgumentException("height must be greater then 0");
        }

        this.ratio = calcRatio(width, height);
    }

    private Dimension calcRatio(int width, int height) {
        int gcd = greatestCommonDivisor(width, height);
        return new Dimension(width / gcd, height / gcd);
    }

    private int greatestCommonDivisor(int x, int y) {
        if (y == 0) return x;
        else return greatestCommonDivisor(y, x % y);
    }

    public int getWidthValue() {
        return ratio.width;
    }

    public int getHeightValue() {
        return ratio.height;
    }

    public Dimension asDimension() {
        return new Dimension(ratio);
    }

    @Override
    public String toString() {
        return ratio.width + ":" + ratio.height;
    }
}
