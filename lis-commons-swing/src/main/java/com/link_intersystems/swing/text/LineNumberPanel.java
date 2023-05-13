package com.link_intersystems.swing.text;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

class LineNumberPanel extends JPanel {

    private LineSupportAdapter lineSupportAdapter = new LineSupportAdapter();

    private int lines;
    private int align;
    private int leftMargin;
    private int rightMargin;
    private int firstLineNumber;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    private boolean synchronizeSize;
    private boolean synchronizeStyle;
    private boolean synchronizeFamily;

    private LineSupport lineSupport;

    public LineNumberPanel() {

        this.lines = -1;
        this.align = ALIGN_LEFT;
        this.leftMargin = 2;
        this.rightMargin = 2;
        this.firstLineNumber = 1;
        this.synchronizeSize = false;
        this.synchronizeStyle = false;

        this.synchronizeFamily = false;
    }

    public LineNumberPanel(LineSupport lineSupport) {
        this.lines = -1;
        this.align = 0;
        this.leftMargin = 5;
        this.rightMargin = 5;
        this.firstLineNumber = 1;
        this.synchronizeSize = false;
        this.synchronizeStyle = false;
        this.synchronizeFamily = false;

        if (lineSupport != null) {

            setTextArea(lineSupport);
        }
    }

    public void setTextArea(LineSupport lineSupport) {
        if (this.lineSupport != null) {
            this.lineSupport.removeLineSupportListener(lineSupportAdapter);
        }

        this.lineSupport = lineSupport;

        if (this.lineSupport != null) {
            this.lineSupport.addLineSupportListener(lineSupportAdapter);
        }

        setFont(lineSupport.getFont());
    }

    public void setSynchronizeFontSize(boolean synchronizeSize) {

        this.synchronizeSize = synchronizeSize;
    }

    public void setSynchronizeFontStyle(boolean synchronizeStyle) {

        this.synchronizeStyle = synchronizeStyle;
    }

    public void setSynchronizeFontFamily(boolean synchronizeFamily) {

        this.synchronizeFamily = synchronizeFamily;
    }

    public void setAlign(int align) {

        this.align = align;
    }

    public void setWidth(int width) {

        Dimension dim = getSize();

        dim.setSize(width, dim.height);

        setPreferredSize(dim);

        setSize(dim);
    }

    public void setLeftMargin(int leftMargin) {

        this.leftMargin = (leftMargin < 0) ? 0 : leftMargin;
    }

    public int getLeftMargin() {

        return this.leftMargin;
    }

    public void setRightMargin(int rightMargin) {

        this.rightMargin = (rightMargin < 0) ? 0 : rightMargin;
    }

    public int getRightMargin() {

        return this.rightMargin;
    }

    public void setVisible(boolean v) {

        super.setVisible(v);

        ensurePreferredSize();
    }

    public void setFirstLineNumber(int lineNumber) {

        this.firstLineNumber = lineNumber;
    }

    public int getFirstLineNumber() {

        return this.firstLineNumber;
    }

    public void update(Graphics g) {

        ensurePreferredSize();

        super.update(g);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        lines = -1;
        ensurePreferredSize();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        paintLineNumbers(g);
    }

    private void paintLineNumbers(Graphics g) {
        Rectangle drawHere = g.getClipBounds();
//        g.setColor(new Color(230, 163, 4));
//        g.fillRect(drawHere.x, drawHere.y, drawHere.width, getHeight());
        List<Integer> lineOffsets = lineSupport.getYLineOffsets();

        FontMetrics fm = g.getFontMetrics(getFont());

        int end = 0;
        int start = 0;
        start = drawHere.y;
        end = drawHere.y + drawHere.height;


        for (int i = 0; i < lineOffsets.size(); i++) {
            int lineYOffset = lineOffsets.get(i);
            if(lineYOffset >= start && lineYOffset <= end){

                String num = Integer.toString(i + 1);
                g.drawString(num, getXPosition(fm, g, num), lineYOffset);
            }
        }

    }

    private int getXPosition(FontMetrics fm, Graphics g, String str) {
        Rectangle r;

        int viewAreaWidth, left = getLeftMargin();

        int right = getRightMargin();

        int pos = left;


        switch (this.align) {
            case ALIGN_RIGHT:

                r = fm.getStringBounds(str, g).getBounds();

                viewAreaWidth = (getBounds()).width - right - left;

                pos += viewAreaWidth - r.width;
                break;

            case ALIGN_CENTER:

                r = fm.getStringBounds(str, g).getBounds();

                viewAreaWidth = (getBounds()).width - right - left;

                pos += viewAreaWidth / 2 - r.width / 2;
                break;
        }


        return pos;
    }

    private void ensurePreferredSize() {
        if (lineSupport == null) {
            return;
        }

        Graphics g = getGraphics();

        Dimension dim = getPreferredSize();

        if (g != null) {
            List<Integer> yLineOffsets = lineSupport.getYLineOffsets();
            lines = yLineOffsets.size();

            FontMetrics fm = g.getFontMetrics(getFont());

            Rectangle2D maxLineNumberBounds = fm.getStringBounds(Integer.toString(lines), g);

            int minWidth = (int) (maxLineNumberBounds.getWidth()) + leftMargin + rightMargin;
            int fontHeight = fm.getHeight();
            int minHeight = yLineOffsets.size() > 0 ? yLineOffsets.get(yLineOffsets.size() - 1) + fontHeight : 0;

            Insets insets = getInsets();
            dim.setSize(minWidth + insets.right + insets.left, Math.max(dim.height, minHeight));

            setPreferredSize(dim);
        }

        setPreferredSize(dim);

        Dimension dim2 = getSize();

        if (dim.width != dim2.width) setSize(dim);
    }


    private class LineSupportAdapter implements LineSupportListener {

        @Override
        public void onLineYOffsetChanged(List<Integer> lineYOffsets) {
            repaint();
        }

        @Override
        public void onFontChange(Font newFont) {
            Font myFont = getFont();

            int size = myFont.getSize();
            int style = myFont.getStyle();
            String family = myFont.getFamily();


            if (synchronizeSize) {
                size = newFont.getSize();
            }

            if (synchronizeStyle) {
                style = newFont.getStyle();
            }

            if (synchronizeFamily) {
                family = newFont.getFamily();
            }


            myFont = new Font(family, style, size);
            setFont(myFont);
        }

    }
}
