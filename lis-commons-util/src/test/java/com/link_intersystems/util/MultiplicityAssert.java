package com.link_intersystems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiplicityAssert {

	public static void assertRange(Multiplicity multiplicity, int minValue, int maxValue) {
		int max = multiplicity.getMax();
		assertEquals(maxValue, max);

		int min = multiplicity.getMin();
		assertEquals(minValue, min);

		assertTrue(multiplicity.isWithinBounds(minValue));
		assertFalse(multiplicity.isWithinBounds(minValue - 1));

		assertTrue(multiplicity.isWithinBounds(maxValue));
		assertFalse(multiplicity.isWithinBounds(maxValue + 1));
	}

	public static void assertParseToStringEqual(String multiplicitySpec) {
		Multiplicity multiplicity = Multiplicity.valueOf(multiplicitySpec);
		Multiplicity multiplicity2 = Multiplicity.valueOf(multiplicity.toString());

		assertEquals(multiplicity, multiplicity2);

		assertRange(multiplicity2, multiplicity.getMin(), multiplicity.getMax());

		assertEquals(multiplicity.hashCode(), multiplicity2.hashCode(), "hashCode");
	}

}