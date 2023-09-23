package com.link_intersystems.util.diff;

import com.link_intersystems.util.CharacterSequence;
import com.link_intersystems.util.ListSequence;
import com.link_intersystems.util.Sequence;

import java.util.List;

import static java.util.Objects.*;

/**
 * Implements a longest common subsequence algorithm.
 *
 * @param <E>
 * @see <a href="https://en.wikipedia.org/wiki/Longest_common_subsequence">Longest common subsequence</a>
 */
public class LongestCommonSubsequence<E> implements Sequence<E> {

    public static Sequence<Character> of(CharSequence charSequence1, CharSequence charSequence2) {
        return new LongestCommonSubsequence<>(new CharacterSequence(charSequence1), new CharacterSequence(charSequence2));
    }

    private Sequence<E> sequence1;
    private Sequence<E> sequence2;

    private Sequence<E> lcs;

    public LongestCommonSubsequence(List<E> list1, List<E> list2) {
        this(new ListSequence<E>(list1), new ListSequence<E>(list2));
    }

    public LongestCommonSubsequence(Sequence<E> sequence1, Sequence<E> sequence2) {
        this.sequence1 = requireNonNull(sequence1);
        this.sequence2 = requireNonNull(sequence2);
    }

    protected Sequence<E> getLcs() {
        if (lcs == null) {
            LongestCommonSubsequenceTable longestCommonSubsequenceTable = new LongestCommonSubsequenceTable(sequence1, sequence2);
            lcs = longestCommonSubsequenceTable.getLcs();
        }
        return lcs;
    }

    @Override
    public E elementAt(int index) {
        return getLcs().elementAt(index);
    }

    @Override
    public int length() {
        return getLcs().length();
    }


}
