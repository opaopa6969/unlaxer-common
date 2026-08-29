package org.unlaxer.elementary;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.parser.elementary.WordParser;

public class WordParserEmptyBoundaryTest extends ParserTestBase {

	@Test
	public void emptyWordFailsOnNonEmptyInput() {
		WordParser emptyWord = new WordParser("");
		testUnMatch(emptyWord, "abc");
		testUnMatch(emptyWord, "a");
		testUnMatch(emptyWord, " ");
	}

	@Test
	public void emptyWordFailsOnEmptyInput() {
		WordParser emptyWord = new WordParser("");
		testUnMatch(emptyWord, "");
	}

	@Test
	public void emptyWordIgnoreCaseFailsOnAnyInput() {
		WordParser emptyWordIgnoreCase = new WordParser("", true);
		testUnMatch(emptyWordIgnoreCase, "abc");
		testUnMatch(emptyWordIgnoreCase, "");
	}
}
