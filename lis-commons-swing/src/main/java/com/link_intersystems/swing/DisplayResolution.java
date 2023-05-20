package com.link_intersystems.swing;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class DisplayResolution {

    public static final DisplayResolution QQVGA = new DisplayResolution(160, 120);
    public static final DisplayResolution HQVGA = new DisplayResolution(240, 160);
    public static final DisplayResolution QVGA = new DisplayResolution(320, 240);
    public static final DisplayResolution HVGA = new DisplayResolution(480, 320);
    public static final DisplayResolution VGA = new DisplayResolution(640, 480);
    public static final DisplayResolution SVGA = new DisplayResolution(800, 600);
    public static final DisplayResolution qHD = new DisplayResolution(960, 540);
    public static final DisplayResolution DVGA = new DisplayResolution(960, 640);
    public static final DisplayResolution XGA = new DisplayResolution(1024, 768);
    public static final DisplayResolution XGA_PLUS = new DisplayResolution(1152, 864);
    public static final DisplayResolution WXGA = new DisplayResolution(1280, 800);
    public static final DisplayResolution WSXGA = new DisplayResolution(1440, 900);
    public static final DisplayResolution SXGA = new DisplayResolution(1280, 1024);
    public static final DisplayResolution SXGA_PLUS = new DisplayResolution(1400, 1050);
    public static final DisplayResolution WSXGA_PLUS = new DisplayResolution(1680, 1050);
    public static final DisplayResolution FHD = new DisplayResolution(1920, 1080);
    public static final DisplayResolution UXGA = new DisplayResolution(1600, 1200);
    public static final DisplayResolution WUXGA = new DisplayResolution(1920, 1200);
    public static final DisplayResolution TXGA = new DisplayResolution(1920, 1400);
    public static final DisplayResolution QXGA = new DisplayResolution(2048, 1536);
    public static final DisplayResolution WQHD = new DisplayResolution(2560, 1440);
    public static final DisplayResolution WQXGA = new DisplayResolution(2560, 1600);
    public static final DisplayResolution QSXGA = new DisplayResolution(2560, 2048);
    public static final DisplayResolution WQXGA_PLUS = new DisplayResolution(3200, 1800);
    public static final DisplayResolution WQSXGA = new DisplayResolution(3200, 2048);
    public static final DisplayResolution QUXGA = new DisplayResolution(3200, 2400);
    public static final DisplayResolution _4K_UHD = new DisplayResolution(3840, 2160);
    public static final DisplayResolution WQUXGA = new DisplayResolution(3840, 2400);
    public static final DisplayResolution HXGA = new DisplayResolution(4096, 3072);
    public static final DisplayResolution _5K = new DisplayResolution(5120, 2880);
    public static final DisplayResolution WHXGA = new DisplayResolution(5120, 3200);
    public static final DisplayResolution HSXGA = new DisplayResolution(5120, 4096);
    public static final DisplayResolution _6K = new DisplayResolution(6016, 3384);
    public static final DisplayResolution WHSXGA = new DisplayResolution(6400, 4096);
    public static final DisplayResolution HUXGA = new DisplayResolution(6400, 4800);
    public static final DisplayResolution _8K_UHD2 = new DisplayResolution(7680, 4320);
    public static final DisplayResolution WHUXGA = new DisplayResolution(7680, 4800);
    public static final DisplayResolution _8K_FULL = new DisplayResolution(8192, 4320);
    public static final DisplayResolution UW10K = new DisplayResolution(10240, 4320);
    public static final DisplayResolution _8K_FULLDOME = new DisplayResolution(8192, 8192);
    public static final DisplayResolution _16K = new DisplayResolution(15360, 8640);
    public static final DisplayResolution _16K_FULL_FORMAT = new DisplayResolution(16384, 8640);

    public static Stream<DisplayResolution> stream() {
        Field[] declaredFields = DisplayResolution.class.getDeclaredFields();

        return Arrays.asList(declaredFields).stream().filter(f -> f.getType().equals(DisplayResolution.class)).filter(DisplayResolution::isConstant).map(f -> {
            try {
                return (DisplayResolution) f.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static boolean isConstant(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public final Dimension dimension;

    public DisplayResolution(int width, int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width must be greater then 0");
        }
        if (height < 1) {
            throw new IllegalArgumentException("height must be greater then 0");
        }

        dimension = new Dimension(width, height);
    }

    public Dimension getDimension() {
        return new Dimension(dimension);
    }

    public DimensionRatio getRatio() {
        return new DimensionRatio(dimension.width, dimension.height);
    }

    public int getPixelCount() {
        return dimension.width * dimension.height;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayResolution that = (DisplayResolution) o;
        return Objects.equals(getDimension(), that.getDimension());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDimension());
    }
}
