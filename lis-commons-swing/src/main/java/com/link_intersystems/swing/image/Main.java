package com.link_intersystems.swing.image
        ;


import com.link_intersystems.swing.image.OffScreenImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage imgBuf = ImageIO.read(new File("D:\\link-intersystems.com\\SynologyDrive\\main\\Unternehmenspräsentation\\Logos\\Firmenlogo\\72dpi\\Link-Intersystems_RGB_150x40.png"));
        DirectColorModel colorModel = new DirectColorModel(24,
                0x00ff0000,   // Red
                0x0000ff00,   // Green
                0x000000ff,   // Blue
                0x0           // Alpha
        );
        WritableRaster raster = colorModel.createCompatibleWritableRaster(imgBuf.getWidth(), imgBuf.getHeight());
        OffScreenImage offScreenImage = new OffScreenImage(colorModel, raster, true);
        Graphics2D graphics = offScreenImage.createGraphics();
        graphics.drawImage(imgBuf, 0, 0, null);
        graphics.dispose();

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(offScreenImage, 0, 0, null);
            }
        };
        jFrame.setSize(new Dimension(200, 100));
        jFrame.getContentPane().add(pane);


        jFrame.setVisible(true);
    }
}
