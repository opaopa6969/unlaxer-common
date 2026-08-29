package org.unlaxer.combinator;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.parser.Parser;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.Choice;

public class EmptyChildrenCombinatorTest extends ParserTestBase {

	@Test
	public void emptyChoiceFailsOnAnyInput() {
		Choice emptyChoice = new Choice(new Parser[0]);
		testUnMatch(emptyChoice, "abc");
		testUnMatch(emptyChoice, "");
		testUnMatch(emptyChoice, "x");
	}

	@Test
	public void emptyChainSucceedsWithZeroConsumptionOnAnyInput() {
		Chain emptyChain = new Chain(new Parser[0]);
		testSucceededOnly(emptyChain, "abc");
		testSucceededOnly(emptyChain, "");
		testSucceededOnly(emptyChain, "x");
	}
}
