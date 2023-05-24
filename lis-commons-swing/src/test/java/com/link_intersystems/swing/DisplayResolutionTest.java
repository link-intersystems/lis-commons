package com.link_intersystems.swing;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.link_intersystems.swing.DisplayResolution.*;
import static org.junit.jupiter.api.Assertions.*;

class DisplayResolutionTest {

    @Test
    void wrongWidth() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayResolution(0, 480));
    }

    @Test
    void wrongHeight() {
        assertThrows(IllegalArgumentException.class, () -> new DisplayResolution(640, 0));
    }


    @Test
    void getDimension() {
        assertEquals(640, VGA.getDimension().width);
        assertEquals(480, VGA.getDimension().height);
    }

    @Test
    void getRatio() {
        DimensionRatio ratio = VGA.getRatio();

        assertEquals("4:3", ratio.toString());
    }

    @Test
    void getPixelCount() {
        assertEquals(2_073_600, DisplayResolution.FHD.getPixelCount());
        assertEquals(141_557_760, DisplayResolution._16K_FULL_FORMAT.getPixelCount());
    }


    @Test
    void stream() {
        Optional<DisplayResolution> resolution = DisplayResolution.stream().filter(FHD::equals).findFirst();

        assertEquals(new DisplayResolution(1920, 1080), resolution.get());
    }

    @Test
    void hashCodeTest() {
        assertEquals(new DisplayResolution(1920, 1080).hashCode(), FHD.hashCode());
    }
}