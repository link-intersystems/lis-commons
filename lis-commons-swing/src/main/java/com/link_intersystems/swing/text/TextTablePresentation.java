package com.link_intersystems.swing.text;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TextTablePresentation {

    public static final TextTablePresentation unicodeBox() {
        TextTablePresentation presentation = new TextTablePresentation();
        presentation.cellLeftPad = 3;
        presentation.cellRightPad = 2;
        presentation.verticalLine = '\u2503';
        presentation.horizontalLine = '\u2501';
        presentation.topLeftCorner = '\u250F';
        presentation.topRightCorner = '\u2513';
        presentation.bottomLeftCorner = '\u2517';
        presentation.bottomRightCorner = '\u251B';
        presentation.crossLines = '\u254B';
        presentation.crossLinesTop = '\u2533';
        presentation.crossLinesBottom = '\u253B';
        presentation.crossLinesLeft = '\u2523';
        presentation.crossLinesRight = '\u252B';
        return presentation;
    }

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

    public char crossLinesTop = '+';
    public char crossLinesBottom = '+';
    public char crossLinesLeft = '+';
    public char crossLinesRight = '+';
}
