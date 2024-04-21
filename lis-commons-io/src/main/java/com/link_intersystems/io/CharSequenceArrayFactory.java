package com.link_intersystems.io;

/**
 * This interface provides methods to convert a CharSequence to a char array.
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface CharSequenceArrayFactory {

    /**
     * Converts a CharSequence to a char array.
     *
     * @param charSequence the CharSequence to convert
     * @return the char array representing the CharSequence
     */
    default char[] toArray(CharSequence charSequence) {
        return toArray(charSequence, 0, charSequence.length());
    }

    /**
     * Converts a subsequence of a CharSequence to a char array.
     *
     * @param charSequence the CharSequence from which to extract the subsequence and convert to a char array
     * @param start        the starting index (inclusive) of the subsequence
     * @param end          the ending index (exclusive) of the subsequence
     * @return the char array representing the subsequence
     */
    char[] toArray(CharSequence charSequence, int start, int end);
}
