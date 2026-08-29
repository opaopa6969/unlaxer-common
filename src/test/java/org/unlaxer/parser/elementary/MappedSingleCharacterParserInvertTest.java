package org.unlaxer.parser.elementary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MappedSingleCharacterParserInvertTest {

	@Test
	public void invertMatchesCharactersNotInSet() {
		MappedSingleCharacterParser notA = new MappedSingleCharacterParser(true, "a");
		assertTrue(notA.isMatch('b'));
		assertTrue(notA.isMatch('c'));
	}

	@Test
	public void invertDoesNotMatchCharacterInSet() {
		MappedSingleCharacterParser notA = new MappedSingleCharacterParser(true, "a");
		assertFalse(notA.isMatch('a'));
	}

	@Test
	public void invertMatchesNonAsciiCharacterNotInSet() {
		MappedSingleCharacterParser notA = new MappedSingleCharacterParser(true, "a");
		assertTrue(notA.isMatch('\u00C8'));
	}

	@Test
	public void nonInvertMatchesCharacterInSet() {
		MappedSingleCharacterParser a = new MappedSingleCharacterParser(false, "a");
		assertTrue(a.isMatch('a'));
	}

	@Test
	public void nonInvertDoesNotMatchCharacterNotInSet() {
		MappedSingleCharacterParser a = new MappedSingleCharacterParser(false, "a");
		assertFalse(a.isMatch('b'));
	}

	@Test
	public void nonInvertDoesNotMatchNonAscii() {
		MappedSingleCharacterParser a = new MappedSingleCharacterParser(false, "a");
		assertFalse(a.isMatch('\u00C8'));
	}
}
