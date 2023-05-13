package com.link_intersystems.util.text;

import java.util.Arrays;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public enum StandardTextAlignments implements TextAlignment {
    LEFT {
        @Override
        public String align(String text, int width, char padChar) {
            char[] buff = new char[width];
            Arrays.fill(buff, padChar);
            System.arraycopy(text.toCharArray(), 0, buff, 0, text.length());
            return new String(buff);
        }
    },
    CENTER {
        @Override
        public String align(String text, int width, char padChar) {
            char[] buff = new char[width];
            Arrays.fill(buff, padChar);
            int offset = (int) Math.ceil((width - text.length()) / 2.0);
            System.arraycopy(text.toCharArray(), 0, buff, offset, text.length());
            return new String(buff);
        }

    },
    RIGHT {
        @Override
        public String align(String text, int width, char padChar) {
            char[] buff = new char[width];
            Arrays.fill(buff, padChar);
            int offset = width - text.length();
            System.arraycopy(text.toCharArray(), 0, buff, offset, text.length());
            return new String(buff);
        }
    };

}
