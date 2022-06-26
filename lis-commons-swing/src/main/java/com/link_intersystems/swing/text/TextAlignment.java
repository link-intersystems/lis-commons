package com.link_intersystems.swing.text;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface TextAlignment {

    default String align(String text, int width) {
        return align(text, width, ' ');
    }

    String align(String text, int width, char padChar);
}
