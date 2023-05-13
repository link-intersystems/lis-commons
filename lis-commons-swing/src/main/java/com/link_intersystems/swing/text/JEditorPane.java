package com.link_intersystems.swing.text;

import com.link_intersystems.swing.scroll.ScrollPaneConfigurer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class JEditorPane extends JTextArea implements LineSupport {

    private PropertyChangeProxy propertyChangeProxy = new PropertyChangeProxy();
    private ComponentChangeProxy componentChangeProxy = new ComponentChangeProxy();
    private DocumentProxy documentProxy = new DocumentProxy();

    private ScrollPaneConfigurer lineNumberingConfigurer = new ScrollPaneConfigurer(this) {

        private JComponent lineNumberPanel;

        @Override
        protected void unconofigureScrollPane(JScrollPane scrollPane) {
            JViewport rowHeader = scrollPane.getRowHeader();
            if (rowHeader != null) {
                Component rowHeaderView = rowHeader.getView();
                if (rowHeaderView == lineNumberPanel) {
                    scrollPane.setRowHeaderView(null);
                }
            }
        }

        @Override
        protected void configureScrollPane(JScrollPane scrollPane) {
            lineNumberPanel = new LineNumberPanel(JEditorPane.this);
            lineNumberPanel.setPreferredSize(new Dimension(25, 500));
            scrollPane.setRowHeaderView(lineNumberPanel);
        }
    };

    private EventListenerList eventListenerList = new EventListenerList();

    public JEditorPane() {
        addPropertyChangeListener(propertyChangeProxy);
        addComponentListener(componentChangeProxy);
        getDocument().addDocumentListener(documentProxy);
    }

    @Override
    public void addNotify() {
        lineNumberingConfigurer.configure();
        super.addNotify();
    }

    @Override
    public void removeNotify() {
        lineNumberingConfigurer.unconfigure();
        super.removeNotify();
    }

    @Override
    public void addLineSupportListener(LineSupportListener l) {
        eventListenerList.add(LineSupportListener.class, l);
    }

    @Override
    public void removeLineSupportListener(LineSupportListener l) {
        eventListenerList.remove(LineSupportListener.class, l);
    }

    @Override
    public List<Integer> getYLineOffsets() {
        Rectangle visibleRect = getVisibleRect();
        if (visibleRect.width == 0 || visibleRect.height == 0) {
            return Collections.emptyList();
        }
        Rectangle bounds = getBounds();
        bounds.y = 0;
        BufferedImage bufferedImage = new BufferedImage(visibleRect.width, visibleRect.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        LineNumberCollector lineNumberCollector = new LineNumberCollector(graphics);
        lineNumberCollector.setClip(bufferedImage.getData().getBounds());
        View rootView = getUI().getRootView(this);
        rootView.paint(lineNumberCollector, bounds);
        return lineNumberCollector.getYLineOffsets();
    }

    class LineNumberCollector extends GraphicsDelegate {

        private java.util.List<Integer> yLineOffsets = new ArrayList<>();
        private OnceSettable yOffset = new OnceSettable();
        private OnceSettable startOffsetOnce = new OnceSettable();

        LinkedHashMap<Integer, Integer> offsetByLine = new LinkedHashMap<>();

        private class OnceSettable {

            private int value = -1;

            public void setValue(int value) {
                if (this.value == -1) {
                    this.value = value;
                }
            }

            public int reset() {
                int value = this.value;
                this.value = -1;
                return value;
            }
        }

        public LineNumberCollector(Graphics graphics) {
            super(graphics);
        }

        @Override
        public void drawString(String str, int x, int y) {
            super.drawString(str, x, y);
        }

        @Override
        public void drawChars(char[] data, int offset, int length, int x, int y) {
            try {
                int lineOfOffset = getLineOfOffset(offset);
                offsetByLine.computeIfAbsent(lineOfOffset, l -> y);
            } catch (BadLocationException e) {

            }
            startOffsetOnce.setValue(offset);
            yOffset.setValue(y);
            boolean lineEnd = isLineEnd(data, offset, length);
            if (lineEnd) {
                int lineYOffset = yOffset.reset();
                yLineOffsets.add(lineYOffset);
            }
            super.drawChars(data, offset, length, x, y);
        }

        private boolean isLineEnd(char[] data, int offset, int length) {
            int nextChar = offset + length;
            if (nextChar < data.length) {
                switch (data[nextChar]) {
                    case '\n':
                    case '\r':
                    case '\u0000':
                        return true;
                }
            }

            return nextChar >= data.length;
        }

        public List<Integer> getYLineOffsets() {
            return new ArrayList<>(offsetByLine.values());
        }
    }


    private class DocumentProxy implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {

            fireLineYOffsetChanged();
        }

        public void insertUpdate(DocumentEvent e) {

            fireLineYOffsetChanged();
        }

        public void removeUpdate(DocumentEvent e) {

            fireLineYOffsetChanged();
        }
    }


    private class ComponentChangeProxy extends ComponentAdapter {

        public void componentResized(ComponentEvent e) {

            fireLineYOffsetChanged();
        }
    }

    private class PropertyChangeProxy implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case "font":
                    fireFontChanged((Font) evt.getNewValue());
                    break;
                case "document":
                    Document oldValue = (Document) evt.getOldValue();
                    if (oldValue != null) {
                        oldValue.removeDocumentListener(documentProxy);
                    }

                    Document newValue = (Document) evt.getNewValue();
                    if (newValue != null) {
                        newValue.addDocumentListener(documentProxy);
                    }
                    break;
            }
        }


    }

    private void fireFontChanged(Font font) {
        for (LineSupportListener l : eventListenerList.getListeners(LineSupportListener.class)) {
            l.onFontChange(font);
        }
    }

    private void fireLineYOffsetChanged() {

        for (LineSupportListener l : eventListenerList.getListeners(LineSupportListener.class)) {
            l.onLineYOffsetChanged(Collections.emptyList());
        }
    }
}
