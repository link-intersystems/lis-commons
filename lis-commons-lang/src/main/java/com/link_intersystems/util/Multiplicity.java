package com.link_intersystems.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Multiplicity implements Serializable {

	public static final Multiplicity _1 = new Multiplicity(1, 1);
	public static final Multiplicity _0_OR_MORE = new Multiplicity(0, Integer.MAX_VALUE);
	public static final Multiplicity _0_OR_1 = new Multiplicity(0, 1);
	public static final Multiplicity _1_OR_MORE = new Multiplicity(1, Integer.MAX_VALUE);

	private static final long serialVersionUID = -9057903788091843682L;

	private static final Map<String, Multiplicity> MOSTLY_USED_MULTIPLICITIES;
	private static final Pattern MULTIPLICITY_PATTERN = Pattern.compile("([0-9]+)(\\.\\.([0-9]+|[*]))?");

	static {
		Map<String, Multiplicity> mostlyUsedMultiplicities = new HashMap<String, Multiplicity>();

		mostlyUsedMultiplicities.put("1", _1);
		mostlyUsedMultiplicities.put("*", _0_OR_MORE);
		mostlyUsedMultiplicities.put("?", _0_OR_1);
		mostlyUsedMultiplicities.put("+", _1_OR_MORE);

		MOSTLY_USED_MULTIPLICITIES = Collections.unmodifiableMap(mostlyUsedMultiplicities);
	}

	private final int min;
	private final int max;

	Multiplicity(int minMax) {
		this(minMax, minMax);
	}

	Multiplicity(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * true if the given number is between {@linkplain #getMin() min} and
	 * {@linkplain #getMax() max}, inclusive.
	 *
	 * @return
	 */
	public boolean isWithinBounds(int num) {
		return getMin() <= num && num <= getMax();
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public static Multiplicity valueOf(String str) {
		str = str.trim();

		Multiplicity multiplizitaet = MOSTLY_USED_MULTIPLICITIES.get(str);

		if (multiplizitaet == null) {
			multiplizitaet = parseMultiplicity(str);
		}

		if (multiplizitaet == null) {
			throw new IllegalArgumentException("Unsupported multiplicity " + str);
		}

		return multiplizitaet;
	}

	private static Multiplicity parseMultiplicity(String str) {
		Multiplicity multiplicity = null;
		Matcher matcher = MULTIPLICITY_PATTERN.matcher(str);
		if (matcher.matches()) {
			String minGroup = matcher.group(1);
			int min = Integer.parseInt(minGroup);

			String maxGroup = matcher.group(3);
			if (maxGroup != null) {
				int max = Integer.MAX_VALUE;
				if (!"*".equals(maxGroup)) {
					max = Integer.parseInt(maxGroup);
				}
				multiplicity = new Multiplicity(min, max);
			} else {
				multiplicity = new Multiplicity(min);
			}
		}
		return multiplicity;
	}

	@Override
	public int hashCode() {
		return min ^ max;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Multiplicity other = (Multiplicity) obj;
		return this.min == other.min && this.max == other.max;
	}

	public String toRangeString() {
		return min + ".." + max;
	}

	public String toString() {
		if (this.equals(_0_OR_MORE)) {
			return "*";
		} else if (this.equals(_0_OR_1)) {
			return "?";
		} else if (this.equals(_1_OR_MORE)) {
			return "+";
		} else if (min == max) {
			return String.valueOf(min);
		} else {
			return min + ".." + max;
		}
	}
}
