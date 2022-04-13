package com.link_intersystems.util;

import org.junit.jupiter.api.Test;

import static com.link_intersystems.util.MultiplicityAssert.assertParseToStringEqual;
import static com.link_intersystems.util.MultiplicityAssert.assertRange;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MultiplicityTest  {

    @Test
    void any() {
        Multiplicity multiplicity = Multiplicity.valueOf("*");

        assertRange(multiplicity, 0, Integer.MAX_VALUE);
    }

    @Test
    void one() {
        Multiplicity multiplicity = Multiplicity.valueOf("1");

        assertRange(multiplicity, 1, 1);
    }

    @Test
    void zero() {
        Multiplicity multiplicity = Multiplicity.valueOf("0");

        assertRange(multiplicity, 0, 0);
    }

    @Test
    void zeroOrOne() {
        Multiplicity multiplicity = Multiplicity.valueOf("?");

        assertRange(multiplicity, 0, 1);
    }

    @Test
    void threeToFive() {
        Multiplicity multiplicity = Multiplicity.valueOf("3..5");

        assertRange(multiplicity, 3, 5);
    }

    @Test
    void thirteenToTwentyTwo() {
        Multiplicity multiplicity = Multiplicity.valueOf("13..22");

        assertRange(multiplicity, 13, 22);
    }

    @Test
    void thirteenToUpperEnd() {
        Multiplicity multiplicity = Multiplicity.valueOf("13..*");

        assertRange(multiplicity, 13, Integer.MAX_VALUE);
    }

    @Test
    void parseFromString() {
        assertParseToStringEqual("11..323");
        assertParseToStringEqual(Multiplicity._0_OR_1.toString());
        assertParseToStringEqual(Multiplicity._0_OR_MORE.toString());
        assertParseToStringEqual(Multiplicity._1.toString());
        assertParseToStringEqual(Multiplicity._1_OR_MORE.toString());
    }

    @Test
    void unsupportedMultiplicity() {
        assertThrows(IllegalArgumentException.class, () -> Multiplicity.valueOf("?..22"));
    }

}
