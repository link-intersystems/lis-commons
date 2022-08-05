package com.link_intersystems.io;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public interface CharSequenceArrayFactory {

    default char[] toArray(CharSequence charSequence) {
        return toArray(charSequence, 0, charSequence.length());
    }

    char[] toArray(CharSequence charSequence, int start, int end);
}
