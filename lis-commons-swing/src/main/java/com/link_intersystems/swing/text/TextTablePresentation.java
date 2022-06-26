package com.link_intersystems.swing.text;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TextTablePresentation {

    public static final TextTablePresentation DEFAULT = new Builder().build();

    public static class Builder {
        public char topLeftCorner = '+';
        public char topRightCorner = '+';
        public char bottomLeftCorner = '+';
        public char bottomRightCorner = '+';
        public char horizontalLine = '-';
        public char verticalLine = '|';
        public char crossLines = '+';
        public char padChar = ' ';
        public int cellLeftPad = 1;
        public int cellRightPad = 1;
        public int titleLeftPad = 1;
        public int titleRightPad = 1;

        public char crossLinesTop = '+';
        public char crossLinesBottom = '+';
        public char crossLinesLeft = '+';
        public char crossLinesRight = '+';

        public Builder setTopLeftCorner(char topLeftCorner) {
            this.topLeftCorner = topLeftCorner;
            return this;
        }

        public Builder setTopRightCorner(char topRightCorner) {
            this.topRightCorner = topRightCorner;
            return this;
        }

        public Builder setBottomLeftCorner(char bottomLeftCorner) {
            this.bottomLeftCorner = bottomLeftCorner;
            return this;
        }

        public Builder setBottomRightCorner(char bottomRightCorner) {
            this.bottomRightCorner = bottomRightCorner;
            return this;
        }

        public Builder setHorizontalLine(char horizontalLine) {
            this.horizontalLine = horizontalLine;
            return this;
        }

        public Builder setVerticalLine(char verticalLine) {
            this.verticalLine = verticalLine;
            return this;
        }

        public Builder setCrossLines(char crossLines) {
            this.crossLines = crossLines;
            return this;
        }

        public Builder setPadChar(char padChar) {
            this.padChar = padChar;
            return this;
        }

        public Builder setCellLeftPad(int cellLeftPad) {
            this.cellLeftPad = cellLeftPad;
            return this;
        }

        public Builder setCellRightPad(int cellRightPad) {
            this.cellRightPad = cellRightPad;
            return this;
        }

        public Builder setTitleLeftPad(int titleLeftPad) {
            this.titleLeftPad = titleLeftPad;
            return this;
        }

        public Builder setTitleRightPad(int titleRightPad) {
            this.titleRightPad = titleRightPad;
            return this;
        }

        public Builder setCrossLinesTop(char crossLinesTop) {
            this.crossLinesTop = crossLinesTop;
            return this;
        }

        public Builder setCrossLinesBottom(char crossLinesBottom) {
            this.crossLinesBottom = crossLinesBottom;
            return this;
        }

        public Builder setCrossLinesLeft(char crossLinesLeft) {
            this.crossLinesLeft = crossLinesLeft;
            return this;
        }

        public Builder setCrossLinesRight(char crossLinesRight) {
            this.crossLinesRight = crossLinesRight;
            return this;
        }

        public TextTablePresentation build() {
            return new TextTablePresentation(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner, horizontalLine, verticalLine, crossLines, padChar, cellLeftPad, cellRightPad, titleLeftPad, titleRightPad, crossLinesTop, crossLinesBottom, crossLinesLeft, crossLinesRight);
        }
    }

    public static TextTablePresentation unicodeBox() {
        TextTablePresentation presentation = unicodeBoxBuilder()
                .build();
        return presentation;
    }

    public static Builder unicodeBoxBuilder() {
        return new Builder()
                .setCellLeftPad(1)
                .setCellRightPad(1)
                .setTitleLeftPad(1)
                .setTitleRightPad(1)
                .setVerticalLine('\u2503')
                .setHorizontalLine('\u2501')
                .setTopLeftCorner('\u250F')
                .setTopRightCorner('\u2513')
                .setBottomLeftCorner('\u2517')
                .setBottomRightCorner('\u251B')
                .setCrossLines('\u254B')
                .setCrossLinesTop('\u2533')
                .setCrossLinesBottom('\u253B')
                .setCrossLinesLeft('\u2523')
                .setCrossLinesRight('\u252B');
    }

    public final char topLeftCorner;
    public final char topRightCorner;
    public final char bottomLeftCorner;
    public final char bottomRightCorner;
    public final char horizontalLine;
    public final char verticalLine;
    public final char crossLines;
    public final char padChar;
    public final int cellLeftPad;
    public final int cellRightPad;
    public final int titleLeftPad;
    public final int titleRightPad;

    public final char crossLinesTop;
    public final char crossLinesBottom;
    public final char crossLinesLeft;
    public final char crossLinesRight;


    private TextTablePresentation(char topLeftCorner, char topRightCorner, char bottomLeftCorner, char bottomRightCorner, char horizontalLine, char verticalLine, char crossLines, char padChar, int cellLeftPad, int cellRightPad, int titleLeftPad, int titleRightPad, char crossLinesTop, char crossLinesBottom, char crossLinesLeft, char crossLinesRight) {
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
        this.horizontalLine = horizontalLine;
        this.verticalLine = verticalLine;
        this.crossLines = crossLines;
        this.padChar = padChar;
        this.cellLeftPad = cellLeftPad;
        this.cellRightPad = cellRightPad;
        this.titleLeftPad = titleLeftPad;
        this.titleRightPad = titleRightPad;
        this.crossLinesTop = crossLinesTop;
        this.crossLinesBottom = crossLinesBottom;
        this.crossLinesLeft = crossLinesLeft;
        this.crossLinesRight = crossLinesRight;
    }
}
