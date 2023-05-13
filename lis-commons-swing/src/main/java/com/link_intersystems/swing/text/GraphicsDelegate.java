package com.link_intersystems.swing.text;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import static java.util.Objects.*;

public class GraphicsDelegate extends Graphics2D {

    private Graphics graphics;

    public GraphicsDelegate(Graphics graphics) {
        this.graphics = requireNonNull(graphics);
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D) graphics;
    }

    public void draw(Shape s) {
        getGraphics2D().draw(s);
    }

    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return getGraphics2D().drawImage(img, xform, obs);
    }

    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        getGraphics2D().drawImage(img, op, x, y);
    }

    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        getGraphics2D().drawRenderedImage(img, xform);
    }

    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        getGraphics2D().drawRenderableImage(img, xform);
    }

    public void drawString(String str, float x, float y) {
        getGraphics2D().drawString(str, x, y);
    }

    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        getGraphics2D().drawString(iterator, x, y);
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
        getGraphics2D().drawGlyphVector(g, x, y);
    }

    public void fill(Shape s) {
        getGraphics2D().fill(s);
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return getGraphics2D().hit(rect, s, onStroke);
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        return getGraphics2D().getDeviceConfiguration();
    }

    public void setComposite(Composite comp) {
        getGraphics2D().setComposite(comp);
    }

    public void setPaint(Paint paint) {
        getGraphics2D().setPaint(paint);
    }

    public void setStroke(Stroke s) {
        getGraphics2D().setStroke(s);
    }

    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        getGraphics2D().setRenderingHint(hintKey, hintValue);
    }

    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return getGraphics2D().getRenderingHint(hintKey);
    }

    public void setRenderingHints(Map<?, ?> hints) {
        getGraphics2D().setRenderingHints(hints);
    }

    public void addRenderingHints(Map<?, ?> hints) {
        getGraphics2D().addRenderingHints(hints);
    }

    public RenderingHints getRenderingHints() {
        return getGraphics2D().getRenderingHints();
    }

    public void translate(double tx, double ty) {
        getGraphics2D().translate(tx, ty);
    }

    public void rotate(double theta) {
        getGraphics2D().rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        getGraphics2D().rotate(theta, x, y);
    }

    public void scale(double sx, double sy) {
        getGraphics2D().scale(sx, sy);
    }

    public void shear(double shx, double shy) {
        getGraphics2D().shear(shx, shy);
    }

    public void transform(AffineTransform Tx) {
        getGraphics2D().transform(Tx);
    }

    public void setTransform(AffineTransform Tx) {
        getGraphics2D().setTransform(Tx);
    }

    public AffineTransform getTransform() {
        return getGraphics2D().getTransform();
    }

    public Paint getPaint() {
        return getGraphics2D().getPaint();
    }

    public Composite getComposite() {
        return getGraphics2D().getComposite();
    }

    public void setBackground(Color color) {
        getGraphics2D().setBackground(color);
    }

    public Color getBackground() {
        return getGraphics2D().getBackground();
    }

    public Stroke getStroke() {
        return getGraphics2D().getStroke();
    }

    public void clip(Shape s) {
        getGraphics2D().clip(s);
    }

    public FontRenderContext getFontRenderContext() {
        return getGraphics2D().getFontRenderContext();
    }


    public Graphics create() {
        return getGraphics().create();
    }

    public Graphics create(int x, int y, int width, int height) {
        return getGraphics().create(x, y, width, height);
    }

    public void translate(int x, int y) {
        getGraphics().translate(x, y);
    }

    public Color getColor() {
        return getGraphics().getColor();
    }

    public void setColor(Color c) {
        getGraphics().setColor(c);
    }

    public void setPaintMode() {
        getGraphics().setPaintMode();
    }

    public void setXORMode(Color c1) {
        getGraphics().setXORMode(c1);
    }

    public Font getFont() {
        return getGraphics().getFont();
    }

    public void setFont(Font font) {
        getGraphics().setFont(font);
    }

    public FontMetrics getFontMetrics() {
        return getGraphics().getFontMetrics();
    }

    public FontMetrics getFontMetrics(Font f) {
        return getGraphics().getFontMetrics(f);
    }

    public Rectangle getClipBounds() {
        return getGraphics().getClipBounds();
    }

    public void clipRect(int x, int y, int width, int height) {
        getGraphics().clipRect(x, y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        getGraphics().setClip(x, y, width, height);
    }

    public Shape getClip() {
        return getGraphics().getClip();
    }

    public void setClip(Shape clip) {
        getGraphics().setClip(clip);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        getGraphics().copyArea(x, y, width, height, dx, dy);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        getGraphics().drawLine(x1, y1, x2, y2);
    }

    public void fillRect(int x, int y, int width, int height) {
        getGraphics().fillRect(x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height) {
        getGraphics().drawRect(x, y, width, height);
    }

    public void clearRect(int x, int y, int width, int height) {
        getGraphics().clearRect(x, y, width, height);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        getGraphics().drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        getGraphics().fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        getGraphics().draw3DRect(x, y, width, height, raised);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        getGraphics().fill3DRect(x, y, width, height, raised);
    }

    public void drawOval(int x, int y, int width, int height) {
        getGraphics().drawOval(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height) {
        getGraphics().fillOval(x, y, width, height);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        getGraphics().drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        getGraphics().fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        getGraphics().drawPolyline(xPoints, yPoints, nPoints);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        getGraphics().drawPolygon(xPoints, yPoints, nPoints);
    }

    public void drawPolygon(Polygon p) {
        getGraphics().drawPolygon(p);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        getGraphics().fillPolygon(xPoints, yPoints, nPoints);
    }

    public void fillPolygon(Polygon p) {
        getGraphics().fillPolygon(p);
    }

    public void drawString(String str, int x, int y) {
        getGraphics().drawString(str, x, y);
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        getGraphics().drawString(iterator, x, y);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y) {
        getGraphics().drawChars(data, offset, length, x, y);
    }

    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        getGraphics().drawBytes(data, offset, length, x, y);
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return getGraphics().drawImage(img, x, y, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return getGraphics().drawImage(img, x, y, width, height, observer);
    }

    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return getGraphics().drawImage(img, x, y, bgcolor, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return getGraphics().drawImage(img, x, y, width, height, bgcolor, observer);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return getGraphics().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return getGraphics().drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    public void dispose() {
        getGraphics().dispose();
    }

    @Deprecated
    public Rectangle getClipRect() {
        return getGraphics().getClipRect();
    }

    public boolean hitClip(int x, int y, int width, int height) {
        return getGraphics().hitClip(x, y, width, height);
    }

    public Rectangle getClipBounds(Rectangle r) {
        return getGraphics().getClipBounds(r);
    }


}
