package com.link_intersystems.util;

import static com.link_intersystems.util.MultiplicityAssert.assertParseToStringEqual;
import static com.link_intersystems.util.MultiplicityAssert.assertRange;

import org.junit.Test;

public class MultiplicityTest {

	@Test
	public void any() {
		Multiplicity multiplicity = Multiplicity.valueOf("*");

		assertRange(multiplicity, 0, Integer.MAX_VALUE);
	}

	@Test
	public void one() {
		Multiplicity multiplicity = Multiplicity.valueOf("1");

		assertRange(multiplicity, 1, 1);
	}

	@Test
	public void zero() {
		Multiplicity multiplicity = Multiplicity.valueOf("0");

		assertRange(multiplicity, 0, 0);
	}

	@Test
	public void zeroOrOne() {
		Multiplicity multiplicity = Multiplicity.valueOf("?");

		assertRange(multiplicity, 0, 1);
	}

	@Test
	public void threeToFive() {
		Multiplicity multiplicity = Multiplicity.valueOf("3..5");

		assertRange(multiplicity, 3, 5);
	}

	@Test
	public void thirteenToTwentyTwo() {
		Multiplicity multiplicity = Multiplicity.valueOf("13..22");

		assertRange(multiplicity, 13, 22);
	}

	@Test
	public void thirteenToUpperEnd() {
		Multiplicity multiplicity = Multiplicity.valueOf("13..*");

		assertRange(multiplicity, 13, Integer.MAX_VALUE);
	}

	@Test
	public void parseFromString() {
		assertParseToStringEqual("11..323");
		assertParseToStringEqual(Multiplicity._0_OR_1.toString());
		assertParseToStringEqual(Multiplicity._0_OR_MORE.toString());
		assertParseToStringEqual(Multiplicity._1.toString());
		assertParseToStringEqual(Multiplicity._1_OR_MORE.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void unsupportedMultiplicity() {
		Multiplicity.valueOf("?..22");
	}

}
