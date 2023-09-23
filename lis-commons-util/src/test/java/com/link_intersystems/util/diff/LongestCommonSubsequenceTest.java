package com.link_intersystems.util.diff;

import com.link_intersystems.util.Sequence;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LongestCommonSubsequenceTest {


    static Stream<Arguments> lcsTestData() {
        return Stream.of( //
                Arguments.of("KTEURFJS", "TKWIDEUJ", "KEUJ"), //
                Arguments.of("ACADB", "CBDA", "CA"), //
                Arguments.of("AGGTAB", "GXTXAYB", "GTAB"), //
                Arguments.of("FATHER", "VATER", "ATER"), //
                Arguments.of("MOTHER", "MUTTER", "MTER"), //
                Arguments.of("DAVID", "DANIEL", "DAI"), //
                Arguments.of("CGATAATTGAGA", "GTTCCTAATA", "CTAATA"), //
                Arguments.of("AAACCGTGAGTTATTCGTTCTAGAA", "CACCCCTAAGGTACCTTTGGTTC", "ACCTGGTTTTGTTC") //
        );
    }

    @ParameterizedTest
    @MethodSource("lcsTestData")
    void lcs(String seq1, String seq2, String expectedLcs) {
        Sequence<Character> lcs = LongestCommonSubsequence.of(seq1, seq2);
        assertEquals(expectedLcs, toString(lcs));
    }


    private String toString(Sequence<Character> characterSequence) {
        return characterSequence.stream().map(String::valueOf).collect(Collectors.joining());
    }

}