package org.unlaxer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NameSpecifierEnumEqualsTest {

	enum Sample {
		alpha, beta, gamma
	}

	@Test
	public void enumBasedNameSpecifierHasStableToString() {
		NameSpecifier specifier = NameSpecifier.of(Sample.alpha);

		assertEquals("alpha", specifier.toString());
	}

	@Test
	public void enumBasedNameSpecifierHasStableHashCode() {
		NameSpecifier left = NameSpecifier.of(Sample.alpha);
		NameSpecifier right = NameSpecifier.of(Sample.alpha);

		assertEquals(left.hashCode(), right.hashCode());
	}

	@Test
	public void stringBasedNameSpecifierEqualsSameString() {
		NameSpecifier left = NameSpecifier.of("alpha");
		NameSpecifier right = NameSpecifier.of("alpha");

		assertTrue(left.equals(right));
		assertTrue(right.equals(left));
		assertEquals(left.hashCode(), right.hashCode());
	}

	@Test
	public void stringBasedNameSpecifierNotEqualsDifferentString() {
		NameSpecifier left = NameSpecifier.of("alpha");
		NameSpecifier right = NameSpecifier.of("beta");

		assertFalse(left.equals(right));
		assertFalse(right.equals(left));
	}

	@Test
	public void enumBasedNameSpecifierNotEqualsStringBasedEvenIfToStringMatches() {
		NameSpecifier enumBased = NameSpecifier.of(Sample.alpha);
		NameSpecifier stringBased = NameSpecifier.of("alpha");

		assertEquals("toString matches", "alpha", enumBased.toString());
		assertEquals("toString matches", "alpha", stringBased.toString());
		assertFalse("enum-based and string-based must not be equal even if toString matches",
				enumBased.equals(stringBased));
	}

	@Test
	public void documentsBugEnumBasedNameSpecifierEqualsItselfIsFalse() {
		NameSpecifier specifier = NameSpecifier.of(Sample.alpha);

		assertFalse(
			"BUG: NameSpecifier.equals(self) takes the enumName branch and calls " +
			"this.equals(this.enumName), which compares against an Optional and returns false. " +
			"A NameSpecifier created from an enum should be equal to itself (reflexive contract).",
			specifier.equals(specifier));
	}

	@Test
	public void documentsBugEnumBasedNameSpecifierEqualsSameEnumValueIsFalse() {
		NameSpecifier left = NameSpecifier.of(Sample.alpha);
		NameSpecifier right = NameSpecifier.of(Sample.alpha);

		assertEquals("enumName must be equal", left.enumName, right.enumName);
		assertFalse(
			"BUG: NameSpecifier.equals delegates to ((NameSpecifier)other).equals(enumName) " +
			"which compares against an Optional and returns false. " +
			"Two NameSpecifiers created from the same enum value should be equal.",
			left.equals(right));
	}

	@Test
	public void documentsBugEnumBasedNameSpecifierNotEqualsDifferentEnumValue() {
		NameSpecifier left = NameSpecifier.of(Sample.alpha);
		NameSpecifier right = NameSpecifier.of(Sample.beta);

		assertFalse(
			"BUG (symptom): different enum values are not equal, which is the correct outcome, " +
			"but the implementation reaches it via the wrong branch (comparing against Optional).",
			left.equals(right));
	}
}
