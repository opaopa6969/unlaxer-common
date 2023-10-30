package org.unlaxer.parser.elementary;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.unlaxer.ParserTestBase;
import org.unlaxer.TestResult;
import org.unlaxer.Token;
import org.unlaxer.parser.combinator.OneOrMore;

public class WildCardLineParserTest extends ParserTestBase{

	@Test
	public void test() {
		OneOrMore oneOrMore = new OneOrMore(WildCardLineParser.class);
		String text=
				"\n"+
				"  asda \n"+
				"#asda \n"+
				"\n"+
				"asdsad \n";
		TestResult testAllMatch = testAllMatch(oneOrMore, text);
		List<Token> filteredChildren = testAllMatch.parsed.getRootToken().filteredChildren;
		for (Token token : filteredChildren) {
			System.out.print(token.getParser());
			System.out.println(":"+token.getSource().orElse("null"));
		}

		assertEquals(6, filteredChildren.size());
		
	}

}
