package org.unlaxer.referencer;

import org.junit.Test;
import org.unlaxer.Name;
import org.unlaxer.ParserTestBase;
import org.unlaxer.listener.OutputLevel;
import org.unlaxer.parser.combinator.Chain;
import org.unlaxer.parser.combinator.NonOrdered;
import org.unlaxer.parser.combinator.OneOrMore;
import org.unlaxer.parser.elementary.ParenthesesParser;
import org.unlaxer.parser.posix.AlphabetParser;
import org.unlaxer.parser.posix.DigitParser;
import org.unlaxer.parser.posix.PunctuationParser;
import org.unlaxer.parser.referencer.MatchedNonOrderedParser;

public class MatchedNonOrderedParserTest extends ParserTestBase{

	@Test
	public void testReferenceChoicedElement() {
		
		setLevel(OutputLevel.simple);
		
		Name elementsName = Name.of("nonOrdered");
		Chain parser = new Chain(
			new ParenthesesParser(//
				new NonOrdered(elementsName,//
					new OneOrMore(new AlphabetParser()),//
					new OneOrMore(new DigitParser()),//
					// punctuation contains '(' and ')' -> excludes '()'//
					new OneOrMore(//
						new PunctuationParser().newWithout("()")//
					)
				)
			),
			new MatchedNonOrderedParser(current-> 
				current.getName().equals(elementsName)
			)
		);

		testAllMatch(parser, "(a1%)b2&");
		testAllMatch(parser, "('&gh69)!\"#asd765");
		testUnMatch(parser, "(a1)b2");
		testUnMatch(parser, "(a1%)2b&");
	}

}
