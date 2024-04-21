package com.link_intersystems.io;

/**
 * Default implementation of the CharSequenceArrayFactory interface.
 * This class provides methods to convert a CharSequence to a char array.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultCharSequenceArrayFactory implements CharSequenceArrayFactory {


    public static final CharSequenceArrayFactory INSTANCE = new DefaultCharSequenceArrayFactory();

    @Override
    public char[] toArray(CharSequence charSequence, int start, int end) {
        char[] chars = new char[end - start];

        while (start < end) {
            chars[start] = charSequence.charAt(start++);
        }

        return chars;
    }

}
