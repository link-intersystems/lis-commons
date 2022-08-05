package com.link_intersystems.io;

import java.io.IOException;
import java.io.PushbackReader;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class CharSequenceDetector {

    private CharSequenceArrayFactory charSequenceArrayFactory;

    public CharSequenceDetector() {
        this(DefaultCharSequenceArrayFactory.INSTANCE);
    }

    public CharSequenceDetector(CharSequenceArrayFactory charSequenceArrayFactory) {
        this.charSequenceArrayFactory = requireNonNull(charSequenceArrayFactory);
    }

    /**
     * @param pushbackReader the pushbackReader to use to detect the char sequence. The {@link PushbackReader} must
     *                       have at least a pushback buffer size of 1.
     * @return true if the char sequence was detected or false otherwise. If the char sequence was detected all
     * chars that have been read during detection are gone and the {@link PushbackReader} is at the end position,
     * so that it will return the next char after the detected char sequence on the next read call.
     */
    public boolean detect(PushbackReader pushbackReader, CharSequence sequence) throws IOException {

        if (sequence.length() == 0) {
            return !hasMoreChars(pushbackReader);
        }

        int longestMatchCharCount = findLongestMatchCount(pushbackReader, sequence);

        boolean completeMatch = sequence.length() == longestMatchCharCount;
        if (!completeMatch) {
            char[] sequenceAsArray = charSequenceArrayFactory.toArray(sequence, 0, longestMatchCharCount);
            pushbackReader.unread(sequenceAsArray);
        }
        return completeMatch;
    }

    protected int findLongestMatchCount(PushbackReader pushbackReader, CharSequence sequence) throws IOException {
        int matchPosition = 0;

        int read;
        while (matchPosition < sequence.length() &&
                (read = pushbackReader.read()) != -1) {

            char charAt = sequence.charAt(matchPosition);

            if (charAt != read) {
                pushbackReader.unread(read);
                break;
            }

            matchPosition++;
        }

        return matchPosition;
    }

    protected boolean hasMoreChars(PushbackReader pushbackReader) throws IOException {
        int read = pushbackReader.read();
        if (read == -1) {
            return false;
        }
        pushbackReader.unread(read);
        return true;
    }

}
