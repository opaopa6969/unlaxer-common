package org.unlaxer.parser.combinator;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.parser.elementary.WordParser;

public class NonOrderedPartialConsumeRollbackTest extends ParserTestBase {

	@Test
	public void allChildrenSucceedInNonOrderedOrder() {
		NonOrdered nonOrdered = new NonOrdered(
			new Chain(new WordParser("ab"), new WordParser("cd")),
			new WordParser("ef")
		);

		testAllMatch(nonOrdered, "abcdef");
		testAllMatch(nonOrdered, "efabcd");
	}

	@Test
	public void childThatPartiallyConsumesThenFailsDoesNotLeakConsumption() {
		NonOrdered nonOrdered = new NonOrdered(
			new Chain(new WordParser("abc"), new WordParser("def")),
			new WordParser("ghi")
		);

		testAllMatch(nonOrdered, "abcdefghi");
		testAllMatch(nonOrdered, "ghiabcdef");
	}

	@Test
	public void documentsBugPartialConsumeLeakBreaksSubsequentMatch() {
		NonOrdered nonOrdered = new NonOrdered(
			new Chain(new WordParser("abc"), new WordParser("def")),
			new WordParser("ab")
		);

		testUnMatch(nonOrdered, "abx");
	}

	@Test
	public void documentsBugFailingChainChildCorruptsSubsequentMatch() {
		NonOrdered nonOrdered = new NonOrdered(
			new Chain(new WordParser("abc"), new WordParser("xyz")),
			new WordParser("a")
		);

		testUnMatch(nonOrdered, "abx");
	}

	@Test
	public void documentsBugNestedChoiceInsideNonOrderedDoesNotBacktrackCleanly() {
		NonOrdered nonOrdered = new NonOrdered(
			new Choice(
				new Chain(new WordParser("ab"), new WordParser("cd")),
				new WordParser("xyz")
			),
			new WordParser("xy")
		);

		testUnMatch(nonOrdered, "xyz");
	}

	@Test
	public void twoChainsThatPartiallyConsumeBothFailThenNonOrderedFails() {
		NonOrdered nonOrdered = new NonOrdered(
			new Chain(new WordParser("abc"), new WordParser("def")),
			new Chain(new WordParser("abc"), new WordParser("ghi"))
		);

		testUnMatch(nonOrdered, "abcx");
	}
}
