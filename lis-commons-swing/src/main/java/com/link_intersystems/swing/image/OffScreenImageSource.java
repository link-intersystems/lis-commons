//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.link_intersystems.swing.image;


import sun.awt.image.ByteComponentRaster;
import sun.awt.image.BytePackedRaster;

import java.awt.image.*;
import java.util.Hashtable;

public class OffScreenImageSource implements ImageProducer {
    BufferedImage image;
    int width;
    int height;
    Hashtable properties;
    private ImageConsumer theConsumer;

    public OffScreenImageSource(BufferedImage var1, Hashtable var2) {
        this.image = var1;
        if (var2 != null) {
            this.properties = var2;
        } else {
            this.properties = new Hashtable();
        }

        this.width = var1.getWidth();
        this.height = var1.getHeight();
    }

    public OffScreenImageSource(BufferedImage bufferedImage) {
        this(bufferedImage, (Hashtable) null);
    }

    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        this.theConsumer = imageConsumer;
        this.produce();
    }

    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return imageConsumer == this.theConsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        if (this.theConsumer == imageConsumer) {
            this.theConsumer = null;
        }

    }

    public void startProduction(ImageConsumer var1) {
        this.addConsumer(var1);
    }

    public void requestTopDownLeftRightResend(ImageConsumer var1) {
    }

    private void sendPixels() {
        ColorModel imageColorModel = this.image.getColorModel();
        WritableRaster imageRaster = this.image.getRaster();
        int numDataElements = imageRaster.getNumDataElements();
        int dataType = imageRaster.getDataBuffer().getDataType();
        int[] imageData = new int[this.width * numDataElements];

        if (imageColorModel instanceof IndexColorModel) {
            byte[] var7 = new byte[this.width];
            this.theConsumer.setColorModel(imageColorModel);
            if (imageRaster instanceof ByteComponentRaster) {
                for (int var8 = 0; var8 < this.height; ++var8) {
                    imageRaster.getDataElements(0, var8, this.width, 1, var7);
                    this.theConsumer.setPixels(0, var8, this.width, 1, imageColorModel, var7, 0, this.width);
                }
            } else if (imageRaster instanceof BytePackedRaster) {
                for (int var8 = 0; var8 < this.height; ++var8) {
                    imageRaster.getPixels(0, var8, this.width, 1, imageData);

                    for (int var9 = 0; var9 < this.width; ++var9) {
                        var7[var9] = (byte) imageData[var9];
                    }

                    this.theConsumer.setPixels(0, var8, this.width, 1, imageColorModel, var7, 0, this.width);
                }
            } else if (dataType == 2 || dataType == 3) {
                for (int var8 = 0; var8 < this.height; ++var8) {
                    imageRaster.getPixels(0, var8, this.width, 1, imageData);
                    this.theConsumer.setPixels(0, var8, this.width, 1, imageColorModel, imageData, 0, this.width);
                }
            }
        } else if (imageColorModel instanceof DirectColorModel) {
            this.theConsumer.setColorModel(imageColorModel);
            dataTypeSwitch:
            switch (dataType) {
                case DataBuffer.TYPE_BYTE:
                    byte[] dataBufferByte = new byte[this.width];
                    int y = 0;

                    while (true) {
                        if (y >= this.height) {
                            break dataTypeSwitch;
                        }

                        imageRaster.getDataElements(0, y, this.width, 1, dataBufferByte);

                        for (int x = 0; x < this.width; ++x) {
                            imageData[x] = dataBufferByte[x] & 255;
                        }

                        this.theConsumer.setPixels(0, y, this.width, 1, imageColorModel, imageData, 0, this.width);
                        ++y;
                    }
                case DataBuffer.TYPE_USHORT:
                    short[] dataBufferShort = new short[this.width];
                    int x = 0;

                    while (true) {
                        if (x >= this.height) {
                            break dataTypeSwitch;
                        }

                        imageRaster.getDataElements(0, x, this.width, 1, dataBufferShort);

                        for (int py = 0; py < this.width; ++py) {
                            imageData[py] = dataBufferShort[py] & '\uffff';
                        }

                        this.theConsumer.setPixels(0, x, this.width, 1, imageColorModel, imageData, 0, this.width);
                        ++x;
                    }
                case DataBuffer.TYPE_SHORT:
                default:
                    ColorModel colorModel = ColorModel.getRGBdefault();
                    this.theConsumer.setColorModel(colorModel);

                    for (int py = 0; py < this.height; ++py) {
                        for (int px = 0; px < this.width; ++px) {
                            imageData[px] = this.image.getRGB(px, py);
                        }

                        this.theConsumer.setPixels(0, py, this.width, 1, colorModel, imageData, 0, this.width);
                    }
                    break;
                case DataBuffer.TYPE_INT:
                    for (int py = 0; py < this.height; ++py) {
                        imageRaster.getDataElements(0, py, this.width, 1, imageData);
                        this.theConsumer.setPixels(0, py, this.width, 1, imageColorModel, imageData, 0, this.width);
                    }
                    break;
            }
        }
    }

    private void produce() {
        try {
            this.theConsumer.setDimensions(this.image.getWidth(), this.image.getHeight());
            this.theConsumer.setProperties(this.properties);
            this.sendPixels();
            this.theConsumer.imageComplete(2);
            this.theConsumer.imageComplete(3);
        } catch (NullPointerException var2) {
            if (this.theConsumer != null) {
                this.theConsumer.imageComplete(1);
            }
        }

    }
}
