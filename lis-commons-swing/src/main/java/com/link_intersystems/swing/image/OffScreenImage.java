package com.link_intersystems.swing.image;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class OffScreenImage extends BufferedImage {
    private OffScreenImageSource osis;
    private Font defaultFont;

    public OffScreenImage(ColorModel var2, WritableRaster var3, boolean var4) {
        super(var2, var3, var4, (Hashtable) null);
        this.initSurface(var3.getWidth(), var3.getHeight());
    }

    public Graphics getGraphics() {
        return this.createGraphics();
    }

    public Graphics2D createGraphics() {
        GraphicsEnvironment var4 = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return var4.createGraphics(this);
    }

    private void initSurface(int var1, int var2) {
        Graphics2D var3 = this.createGraphics();

        try {
            var3.clearRect(0, 0, var1, var2);
        } finally {
            var3.dispose();
        }

    }

    public ImageProducer getSource() {
        if (this.osis == null) {
            this.osis = new OffScreenImageSource(this);
        }

        return this.osis;
    }
}
